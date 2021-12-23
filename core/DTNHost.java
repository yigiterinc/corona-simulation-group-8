/*
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details.
 */
package core;

import java.util.*;

import temporal.DailyBehaviour;
import movement.MovementModel;
import movement.Path;
import routing.MessageRouter;
import routing.util.RoutingInfo;

/**
 * A DTN capable host.
 */
public class DTNHost implements Comparable<DTNHost> {
    private static int nextAddress = 0;
    private int address;
    private double lastExposedTime;

    private MessageRouter router;
    private Path path;
    private String name;
    private List<MessageListener> msgListeners;
    private List<NetworkInterface> net;
    private ModuleCommunicationBus comBus;

    private HealthStatus healthStatus;
    private Role role;

    public DailyBehaviour getDailyBehaviour() {
        return dailyBehaviour;
    }

    private final DailyBehaviour dailyBehaviour;

    private final Random random = new Random();
    private double INFECTION_PROBABILITY = 0.4;

    static {
        DTNSim.registerForReset(DTNHost.class.getCanonicalName());
        reset();
    }

    /**
     * Creates a new DTNHost.
     *  @param msgLs        Message listeners
     * @param movLs        Movement listeners
     * @param groupId      GroupID of this host
     * @param interf       List of NetworkInterfaces for the class
     * @param comBus       Module communication bus object
     * @param mmProto      Prototype of the movement model of this host
     * @param mRouterProto Prototype of the message router of this host
     * @param healthStatus
     */
    public DTNHost(List<MessageListener> msgLs,
                   List<MovementListener> movLs,
                   String groupId, List<NetworkInterface> interf,
                   ModuleCommunicationBus comBus,
                   MovementModel mmProto,
                   MessageRouter mRouterProto,
                   Role role,
                   HealthStatus healthStatus) {

        this.comBus = comBus;
        this.address = getNextAddress();
        this.name = groupId + address;
        this.net = new ArrayList<NetworkInterface>();
        this.role = role;
        this.healthStatus = healthStatus;

        this.dailyBehaviour = new DailyBehaviour(this, mmProto, movLs);
        this.dailyBehaviour.chooseLectures();        //Select Lectures taken through out the day
        //this.dailyBehaviour.printLectures();

        for (NetworkInterface i : interf) {
            NetworkInterface ni = i.replicate();
            ni.setHost(this);
            net.add(ni);
        }

        // TODO - think about the names of the interfaces and the nodes
        //this.name = groupId + ((NetworkInterface)net.get(1)).getAddress();

        this.msgListeners = msgLs;

        // create instances by replicating the prototypes
        //this.movement = mmProto.replicate();
        setRouter(mRouterProto.replicate());

        this.path = null;
    }

    public boolean isMovementActive() {
        return dailyBehaviour.isMovementActive();
    }
    //TODO: AcctionListener for connection

    public void addConnection(DTNHost host) {
        dailyBehaviour.addConnection(host);
    }

    //public Map<DTNHost,Double> getConnectedHosts(){
    //	return connectedHosts;
    //}
    public void removeConnection(DTNHost host) {
        dailyBehaviour.removeConnection(host);
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Returns a new network interface address and increments the address for
     * subsequent calls.
     *
     * @return The next address.
     */
    private synchronized static int getNextAddress() {
        return nextAddress++;
    }


    /**
     * Reset the host and its interfaces
     */
    public static void reset() {
        nextAddress = 0;
    }

    /**
     * Returns true if this node's radio is active (false if not)
     *
     * @return true if this node's radio is active (false if not)
     */
    public boolean isRadioActive() {
        /* TODO: make this work for multiple interfaces */
        return this.getInterface(1).isActive();
    }

    /**
     * Set a router for this host
     *
     * @param router The router to set
     */
    private void setRouter(MessageRouter router) {
        router.init(this, msgListeners);
        this.router = router;
    }

    /**
     * Returns the router of this host
     *
     * @return the router of this host
     */
    public MessageRouter getRouter() {
        return this.router;
    }

    /**
     * Returns the network-layer address of this host.
     */
    public int getAddress() {
        return this.address;
    }

    /**
     * Returns this hosts's ModuleCommunicationBus
     *
     * @return this hosts's ModuleCommunicationBus
     */
    public ModuleCommunicationBus getComBus() {
        return this.comBus;
    }

    /**
     * Informs the router of this host about state change in a connection
     * object.
     *
     * @param con The connection object whose state changed
     */
    public void connectionUp(Connection con) {
        this.router.changedConnection(con);
    }

    public void connectionDown(Connection con) {
        this.router.changedConnection(con);
    }

    /**
     * Returns a copy of the list of connections this host has with other hosts
     *
     * @return a copy of the list of connections this host has with other hosts
     */
    public List<Connection> getConnections() {
        List<Connection> lc = new ArrayList<Connection>();

        for (NetworkInterface i : net) {
            lc.addAll(i.getConnections());
        }

        return lc;
    }

    /**
     * Returns the current location of this host.
     *
     * @return The location
     */
    public Coord getLocation() {
        return dailyBehaviour.getLocation();
    }

    /**
     * Returns the Path this node is currently traveling or null if no
     * path is in use at the moment.
     *
     * @return The path this node is traveling
     */
    public Path getPath() {
        return this.path;
    }


    /**
     * Sets the Node's location overriding any location set by movement model
     *
     * @param location The location to set
     */
    public void setLocation(Coord location) {
        dailyBehaviour.setLocation(location.clone());
    }

    /**
     * Sets the Node's name overriding the default name (groupId + netAddress)
     *
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public void overrideNameWith(String location) {
        name = String.format("%s %s", location, healthStatus);
    }

    public String getName() {
        return this.name;
    }

    public Role getRole() {
        return this.role;
    }


    /**
     * Returns the messages in a collection.
     *
     * @return Messages in a collection
     */
    public Collection<Message> getMessageCollection() {
        return this.router.getMessageCollection();
    }

    /**
     * Returns the number of messages this node is carrying.
     *
     * @return How many messages the node is carrying currently.
     */
    public int getNrofMessages() {
        return this.router.getNrofMessages();
    }

    /**
     * Returns the buffer occupancy percentage. Occupancy is 0 for empty
     * buffer but can be over 100 if a created message is bigger than buffer
     * space that could be freed.
     *
     * @return Buffer occupancy percentage
     */
    public double getBufferOccupancy() {
        double bSize = router.getBufferSize();
        double freeBuffer = router.getFreeBufferSize();
        return 100 * ((bSize - freeBuffer) / bSize);
    }

    /**
     * Returns routing info of this host's router.
     *
     * @return The routing info.
     */
    public RoutingInfo getRoutingInfo() {
        return this.router.getRoutingInfo();
    }

    /**
     * Returns the interface objects of the node
     */
    public List<NetworkInterface> getInterfaces() {
        return net;
    }

    /**
     * Find the network interface based on the index
     */
    public NetworkInterface getInterface(int interfaceNo) {
        NetworkInterface ni = null;
        try {
            ni = net.get(interfaceNo - 1);
        } catch (IndexOutOfBoundsException ex) {
            throw new SimError("No such interface: " + interfaceNo +
                    " at " + this);
        }
        return ni;
    }

    /**
     * Find the network interface based on the interfacetype
     */
    protected NetworkInterface getInterface(String interfacetype) {
        for (NetworkInterface ni : net) {
            if (ni.getInterfaceType().equals(interfacetype)) {
                return ni;
            }
        }
        return null;
    }

    /**
     * Force a connection event
     */
    public void forceConnection(DTNHost anotherHost, String interfaceId,
                                boolean up) {
        NetworkInterface ni;
        NetworkInterface no;

        if (interfaceId != null) {
            ni = getInterface(interfaceId);
            no = anotherHost.getInterface(interfaceId);

            assert (ni != null) : "Tried to use a nonexisting interfacetype " + interfaceId;
            assert (no != null) : "Tried to use a nonexisting interfacetype " + interfaceId;
        } else {
            ni = getInterface(1);
            no = anotherHost.getInterface(1);

            assert (ni.getInterfaceType().equals(no.getInterfaceType())) :
                    "Interface types do not match.  Please specify interface type explicitly";
        }

        if (up) {
            ni.createConnection(no);
        } else {
            ni.destroyConnection(no);
        }
    }

    /**
     * for tests only --- do not use!!!
     */
    public void connect(DTNHost h) {
        Debug.p("WARNING: using deprecated DTNHost.connect(DTNHost)" +
                "Use DTNHost.forceConnection(DTNHost,null,true) instead");
        forceConnection(h, null, true);
    }

    /**
     * Updates node's network layer and router.
     *
     * @param simulateConnections Should network layer be updated too
     */
    public void update(boolean simulateConnections) {
        if (!isRadioActive()) {
            // Make sure inactive nodes don't have connections
            tearDownAllConnections();
            return;
        }

        if (simulateConnections) {
            for (NetworkInterface i : net) {
                i.update();
            }
        }
        this.router.update();
    }

    /**
     * Tears down all connections for this host.
     */
    private void tearDownAllConnections() {
        for (NetworkInterface i : net) {
            // Get all connections for the interface
            List<Connection> conns = i.getConnections();
            if (conns.size() == 0) continue;

            // Destroy all connections
            List<NetworkInterface> removeList =
                    new ArrayList<NetworkInterface>(conns.size());
            for (Connection con : conns) {
                removeList.add(con.getOtherInterface(i));
            }
            for (NetworkInterface inf : removeList) {
                i.destroyConnection(inf);
            }
        }
    }

    /**
     * Moves the node towards the next waypoint or waits if it is
     * not time to move yet
     *
     * @param timeIncrement How long time the node moves
     */
    public void move(double timeIncrement) {
        this.dailyBehaviour.move(timeIncrement);


    }

    /**
     * Sends a message from this host to another host
     *
     * @param id Identifier of the message
     * @param to Host the message should be sent to
     */
    public void sendMessage(String id, DTNHost to) {
        this.router.sendMessage(id, to);
    }

    /**
     * Start receiving a message from another host
     *
     * @param m    The message
     * @param from Who the message is from
     * @return The value returned by
     * {@link MessageRouter#receiveMessage(Message, DTNHost)}
     */
    public int receiveMessage(Message m, DTNHost from) {
        int retVal = this.router.receiveMessage(m, from);

        if (retVal == MessageRouter.RCV_OK) {
            m.addNodeOnPath(this);    // add this node on the messages path
        }

        return retVal;
    }

    /**
     * Requests for deliverable message from this host to be sent trough a
     * connection.
     *
     * @param con The connection to send the messages trough
     * @return True if this host started a transfer, false if not
     */
    public boolean requestDeliverableMessages(Connection con) {
        return this.router.requestDeliverableMessages(con);
    }

    /**
     * Informs the host that a message was successfully transferred.
     *
     * @param id   Identifier of the message
     * @param from From who the message was from
     */
    public void messageTransferred(String id, DTNHost from) {
        this.router.messageTransferred(id, from);
    }

    /**
     * Informs the host that a message transfer was aborted.
     *
     * @param id             Identifier of the message
     * @param from           From who the message was from
     * @param bytesRemaining Nrof bytes that were left before the transfer
     *                       would have been ready; or -1 if the number of bytes is not known
     */
    public void messageAborted(String id, DTNHost from, int bytesRemaining) {
        this.router.messageAborted(id, from, bytesRemaining);
    }

    /**
     * Creates a new message to this host's router
     *
     * @param m The message to create
     */
    public void createNewMessage(Message m) {
        this.router.createNewMessage(m);
    }

    /**
     * Deletes a message from this host
     *
     * @param id   Identifier of the message
     * @param drop True if the message is deleted because of "dropping"
     *             (e.g. buffer is full) or false if it was deleted for some other reason
     *             (e.g. the message got delivered to final destination). This effects the
     *             way the removing is reported to the message listeners.
     */
    public void deleteMessage(String id, boolean drop) {
        this.router.deleteMessage(id, drop);
    }

    /**
     * Returns a string presentation of the host.
     *
     * @return Host's name
     */
    public String toString() {
        return name;
    }

    /**
     * Checks if a host is the same as this host by comparing the object
     * reference
     *
     * @param otherHost The other host
     * @return True if the hosts objects are the same object
     */
    public boolean equals(DTNHost otherHost) {
        return this == otherHost;
    }

    /**
     * Compares two DTNHosts by their addresses.
     *
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(DTNHost h) {
        return this.getAddress() - h.getAddress();
    }


    public void update() {
        this.dailyBehaviour.update();
    }

    public boolean isStudent() {
    	return this.role == Role.STUDENT;
	}

	public boolean isInfected() {
        return this.healthStatus == HealthStatus.INFECTED;
    }

    public void getExposedToVirus() {
        if (healthStatus == HealthStatus.INFECTED) return;

        double timePassedUntilLastExposition = SimClock.getTime() - lastExposedTime;
        int THRESHOLD = 250;
        if (timePassedUntilLastExposition < THRESHOLD) {
            return;
        }

        var rand = random.nextDouble(); // Between [0,1]  0.5
        boolean shouldGetInfected = rand <= INFECTION_PROBABILITY;
        if (shouldGetInfected) {
            this.healthStatus = HealthStatus.INFECTED;
        }

        lastExposedTime = SimClock.getTime();
    }

    public void setInfectionProbability(double infectionProbability) {
        INFECTION_PROBABILITY = infectionProbability;
    }

    public HealthStatus getHealthStatus() {
        return healthStatus;
    }
}

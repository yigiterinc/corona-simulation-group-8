package temporal;

import core.Coord;
import core.DTNHost;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class State {

    protected Coord destination;
    protected DailyBehaviour dailyBehaviour;
    protected double speed;
    protected boolean destinationChanged = true;
    protected Random random = new Random();

    protected java.util.Map<DTNHost,Double> connectedHosts = new HashMap<>();

    public State(DailyBehaviour dailyBehaviour, State state){
        this.dailyBehaviour = dailyBehaviour;
        if(state != null)
            this.connectedHosts = state.getConnectedHosts();
    }

    public Map<DTNHost, Double> getConnectedHosts(){
        return connectedHosts;
    }

    public abstract Coord getDestination();
    public abstract void reachedDestination();


    public abstract void update();


    public abstract void initConnection(DTNHost host);
    public abstract void removeConnection(DTNHost host);

    public double suggestTimeToMeet(DTNHost other) {
        Random rand = new Random();
        return rand.nextDouble()*1000;
    }

    public double getSpeed() {
        speed = 1;
        return speed;
    }

    public  boolean destinationChanged(){
        return destinationChanged;
    }
}

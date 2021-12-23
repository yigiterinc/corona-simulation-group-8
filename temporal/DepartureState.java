package temporal;

import core.Coord;
import core.DTNHost;
import core.SimClock;

public class DepartureState extends State {

    public static Coord DEPARTURE_COORDS = new Coord(150, 0);

    public DepartureState(DailyBehaviour dailyBehaviour, State state){
        super(dailyBehaviour, state);
    }

    @Override
    public Coord getDestination() {
        return DEPARTURE_COORDS;
    }

    @Override
    public void reachedDestination() {
        if(dailyBehaviour.getLocation().equals(DEPARTURE_COORDS))
            dailyBehaviour.changeState(new IdleState(dailyBehaviour, this));
        else
            dailyBehaviour.changeState(new DepartureState(dailyBehaviour, this));
    }

    @Override
    public void update() {

    }


    public int distributionTime = 200;


    @Override
    public void initConnection(DTNHost otherHost) {}

    @Override
    public void removeConnection(DTNHost otherHost) {}
}

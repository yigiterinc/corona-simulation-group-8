package temporal;

import core.Coord;
import core.DTNHost;
import core.SimClock;

public class IdleState extends State {

    public IdleState(DailyBehaviour dailyBehaviour, State state){
        super(dailyBehaviour, state);
        dailyBehaviour.getMovement().setActive(false);
    }

    @Override
    public Coord getDestination() {
        return null;
    }

    @Override
    public void reachedDestination() {
        dailyBehaviour.changeState(new ArrivalState(dailyBehaviour, this));
    }

    @Override
    public void update() {

    }

    @Override
    public void initConnection(DTNHost otherHost) {}

    @Override
    public void removeConnection(DTNHost otherHost) {}

}

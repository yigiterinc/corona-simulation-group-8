package temporal;

import core.Coord;
import core.DTNHost;
import core.SimClock;

public class InitState extends State {

    public InitState(DailyBehaviour dailyBehaviour, State state){
        super(dailyBehaviour, state);
    }

    @Override
    public Coord getDestination() {
        return null;
    }

    @Override
    public void reachedDestination() {
    }

    @Override
    public void update() {

    }

    @Override
    public void initConnection(DTNHost otherHost) {
    }

    @Override
    public void removeConnection(DTNHost otherHost) {
    }
}

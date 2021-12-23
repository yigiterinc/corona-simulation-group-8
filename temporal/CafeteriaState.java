package temporal;

import core.Coord;
import core.DTNHost;
import core.SimClock;

import java.util.ArrayList;

public class CafeteriaState extends State {
    private double stateEnterTime = 0;
    private Coord CAFETERIA = new Coord(70,50);

    public CafeteriaState(DailyBehaviour dailyBehaviour, State state){
        super(dailyBehaviour, state);
        stateEnterTime = SimClock.getTime();
    }

    @Override
    public Coord getDestination() {
        destinationChanged = false;
        return CAFETERIA;
    }

    @Override
    public void reachedDestination() {
        var host = this.dailyBehaviour.getHost();
        host.overrideNameWith("Cafe");
        dailyBehaviour.getMovement().setInactive(800);
        State state = new FreetimeState(dailyBehaviour, this);
        dailyBehaviour.changeState(state);
    }

    @Override
    public void update() {
        ArrayList<Lecture> lectures = dailyBehaviour.getLecturesAtTime(SimClock.getTime());
        if( lectures.size() > 0){
            dailyBehaviour.changeState(new LectureState(dailyBehaviour, this, lectures.get(0)));
        }
    }

    public int distributionTime = 200;

    @Override
    public void initConnection(DTNHost otherHost) {
        if (SimClock.getTime() > stateEnterTime + distributionTime) {
            dailyBehaviour.getHost().isStudent();
        }

    }

    @Override
    public void removeConnection(DTNHost otherHost) {
    }
}

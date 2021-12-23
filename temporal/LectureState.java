package temporal;

import core.Coord;
import core.DTNHost;
import core.SimClock;

import java.util.Random;

public class LectureState extends State {

    private Lecture lecture;

    public LectureState(DailyBehaviour dailyBehaviour, State state, Lecture lecture){
        super(dailyBehaviour, state);
        this.lecture = lecture;
    }

    @Override
    public Coord getDestination() {
        destinationChanged = false;
        return lecture.getCoord();
    }

    @Override
    public void reachedDestination() {
        var host = this.dailyBehaviour.getHost();
        host.overrideNameWith("Lec");
        dailyBehaviour.getMovement().setActive(false);
        dailyBehaviour.getMovement().setInactive(lecture.getEndTime()-SimClock.getTime());
        Random random = new Random();
        double rand = random.nextDouble();
        State state;
        if(rand < 0.40)
            state = new CafeteriaState(dailyBehaviour, this);
        else
            state = new FreetimeState(dailyBehaviour, this);
        dailyBehaviour.changeState(state);
    }

    public  boolean destinationChanged() {
        return destinationChanged;
    }

    @Override
    public void update() {

    }

    @Override
    public void initConnection(DTNHost host) {

    }

    @Override
    public void removeConnection(DTNHost host) {

    }
}

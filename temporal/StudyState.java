package temporal;

import core.Coord;
import core.DTNHost;
import core.SimClock;

import java.util.ArrayList;

public class StudyState extends State {
    private double stateEnterTime;

    public StudyState(DailyBehaviour dailyBehaviour, State state) {

        super(dailyBehaviour, state);
        stateEnterTime = SimClock.getTime();
    }
    private Coord c;

    @Override
    public Coord getDestination() {
        destinationChanged = false;
        if(c == null)
            c = selectPlaceToStudy();   // generate new random position
        return c;
    }
    private Coord selectPlaceToStudy(){
        if(random.nextDouble()<0.5){
            return new Coord(15,57);    // Library
        }
        return new Coord(80,27);        // Hall
    }

    @Override
    public void reachedDestination() {
        var host = this.dailyBehaviour.getHost();
        host.overrideNameWith("Study");
        dailyBehaviour.getMovement().setInactive(1500);
        State state = new FreetimeState(dailyBehaviour, this);
        dailyBehaviour.changeState(state);
    }

    @Override
    public void update() {
        System.out.println("update");
        ArrayList<Lecture> lectures= dailyBehaviour.getLecturesAtTime(SimClock.getTime());
        if( lectures.size() > 0){
            System.out.println("\t\tupdate lecture!!");
            this.dailyBehaviour.getMovement().setActive(true);
            dailyBehaviour.changeState(new LectureState(dailyBehaviour, this, lectures.get(0)));
        }
    }


    @Override
    public void initConnection(DTNHost host) {

    }

    @Override
    public void removeConnection(DTNHost host) {

    }
}

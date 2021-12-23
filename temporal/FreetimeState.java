package temporal;

import core.Coord;
import core.DTNHost;
import core.SimClock;

import java.util.ArrayList;

public class FreetimeState extends State {

    private double stateEnterTime = 0;
    private Coord c = dailyBehaviour.getMovement().randomCoord();

    public FreetimeState(DailyBehaviour dailyBehaviour, State state){
        super(dailyBehaviour, state);
        stateEnterTime = SimClock.getTime();
    }

    @Override
    public Coord getDestination() {
        destinationChanged = false;
        if(c == null)
            reachedDestination();   // generate new random position
        return c;
    }

    @Override
    public void reachedDestination() {
        var host = this.dailyBehaviour.getHost();
        host.overrideNameWith("Free");
        dailyBehaviour.getMovement().setInactive(1000);
        double r = random.nextDouble();
        if(r < 0.3){
            dailyBehaviour.changeState(new CafeteriaState(dailyBehaviour, this));
        } else if(r < 0.6) {
            dailyBehaviour.changeState(new StudyState(dailyBehaviour, this));
        }
    }

    @Override
    public void update() {
        ArrayList<Lecture> lectures = dailyBehaviour.getLecturesAtTime(SimClock.getTime());
        if( lectures.size() > 0){
            dailyBehaviour.changeState(new LectureState(dailyBehaviour, this, lectures.get(0)));
        }
    }

    public int distributionTime = 150;

    @Override
    public void initConnection(DTNHost otherHost) {
        if (SimClock.getTime() > stateEnterTime + distributionTime) {
            dailyBehaviour.getHost().isStudent();
        }
    }

    @Override
    public void removeConnection(DTNHost otherHost) {
        this.connectedHosts.remove(otherHost);

        if (connectedHosts.size() == 0){
            this.dailyBehaviour.getMovement().setActive(true);
        }
    }
}

package temporal;

import java.util.ArrayList;

public class RoomPlan {
    private ArrayList<Lecture> lectures = new ArrayList<>();

    public RoomPlan(){}


    public void addLecture(Lecture lecture){
        lectures.add(lecture);
    }
    public void removeLecture(Lecture lecture){
        lectures.remove(lecture);
    }

    public ArrayList<Lecture> getLectures() {
        return lectures;
    }
}

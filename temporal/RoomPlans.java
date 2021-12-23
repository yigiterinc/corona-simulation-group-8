package temporal;

import core.Coord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomPlans {

    Map<Coord, RoomPlan> roomPlansMap = new HashMap<>();

    private static RoomPlans roomPlans = null;
    public static final Coord HOERSAAL1 = new Coord(118,42);
    public static final Coord ROOMFINGER1 = new Coord(83,65);
    public static final Coord ROOMFINGER2 = new Coord(28,15);

    public static RoomPlans getRoomPlans(){
        if(roomPlans == null){
            roomPlans = new RoomPlans();
            roomPlans.initRooms();
        }
        return roomPlans;
    }

    public RoomPlans(){

    }
    public static void initRooms(){
        // HOERSAAL
        roomPlans.addLecture(new Lecture(DailyBehaviour.START_BLOCK1, DailyBehaviour.LECTURE_LENGTH, HOERSAAL1));
        roomPlans.addLecture(new Lecture(DailyBehaviour.START_BLOCK2, DailyBehaviour.LECTURE_LENGTH, HOERSAAL1));
        roomPlans.addLecture(new Lecture(DailyBehaviour.START_BLOCK3, DailyBehaviour.LECTURE_LENGTH, HOERSAAL1));
        roomPlans.addLecture(new Lecture(DailyBehaviour.START_BLOCK4, DailyBehaviour.LECTURE_LENGTH, HOERSAAL1));
        // ROOMFINGER1
        roomPlans.addLecture(new Lecture(DailyBehaviour.START_BLOCK1, DailyBehaviour.LECTURE_LENGTH, ROOMFINGER1));
        roomPlans.addLecture(new Lecture(DailyBehaviour.START_BLOCK4, DailyBehaviour.LECTURE_LENGTH, ROOMFINGER1));
        roomPlans.addLecture(new Lecture(DailyBehaviour.START_BLOCK5, DailyBehaviour.LECTURE_LENGTH, ROOMFINGER1));
        // ROOMFINGER2
        roomPlans.addLecture(new Lecture(DailyBehaviour.START_BLOCK1, DailyBehaviour.LECTURE_LENGTH, ROOMFINGER2));
        roomPlans.addLecture(new Lecture(DailyBehaviour.START_BLOCK2, DailyBehaviour.LECTURE_LENGTH, ROOMFINGER2));
        roomPlans.addLecture(new Lecture(DailyBehaviour.START_BLOCK3, DailyBehaviour.LECTURE_LENGTH, ROOMFINGER2));
        roomPlans.addLecture(new Lecture(DailyBehaviour.START_BLOCK4, DailyBehaviour.LECTURE_LENGTH, ROOMFINGER2));
    }

    public void addLecture(Lecture lecture){
        RoomPlan roomPlan = roomPlansMap.get(lecture.getCoord());
        if(roomPlan == null){
            roomPlan = new RoomPlan();
            roomPlansMap.put(lecture.getCoord(),roomPlan);
        }
        roomPlan.addLecture(lecture);
    }
    public ArrayList<Lecture> getAllLecturesAtTime(double time){
        ArrayList<Lecture>lecturesAtTime = new ArrayList<>();
        for(RoomPlan roomPlan : roomPlansMap.values()){
            for(Lecture lecture : roomPlan.getLectures()){
                if(lecture.lectureTakesPlace(time))
                    lecturesAtTime.add(lecture);
            }
        }
        return lecturesAtTime;
    }



}

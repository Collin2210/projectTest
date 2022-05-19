package Controller;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MapObject {
    /**
     * Interface for each object on the map.
     * Creates a unique id for each object and establishes a getCoordinates method.
     */
    private static AtomicInteger uniqueId=new AtomicInteger();
    private final int id;

    ArrayList<Integer> coords = new ArrayList<Integer>();

    ArrayList<int[]> points = new ArrayList<>();

    public MapObject(){
        id = uniqueId.getAndIncrement();
    }

    public ArrayList<Integer> getCoords(){
        return coords;
    }

    public ArrayList<int[]> getPoints(){
        return this.points;
    }

    public int getId() {
        return id;
    }

    /*
    This structure will enable polymorphism later:
    mapObj(AC)
    -> rect (AC)
        +wall C
        +shade C
    -> entrance (AC)
        +door C (like a portal)
        +portal C
    -> texture C

    I- interface, AC - abstract class, C - class
     */

    //static int ID;
}

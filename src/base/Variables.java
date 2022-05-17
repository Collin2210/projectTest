package base;

public class Variables {

    public static final int
            MAP_HEIGHT = 20,
            MAP_WIDTH = 20,
            AGENT_VISION_RANGE = 8;

    public static final double
            FIELD_OF_VIEW = Math.toRadians(90);


    public int getMapHeight() {
        return MAP_HEIGHT;
    }
    public int getMapWidth() {
        return MAP_WIDTH;
    }
}

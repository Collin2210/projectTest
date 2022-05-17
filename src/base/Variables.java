package base;

import java.util.ArrayList;

public class Variables {

    public static  int MAP_HEIGHT = 20;
    public static int MAP_WIDTH = 20;
    public static final int AGENT_VISION_RANGE = 8;
    private float scaling;
    private int numberOfGuards;
    private int numberOfIntruders;
    private float walkingSpeedGuard;
    private int numberWalls;
    private float sprintingSpeedGuard;
    private float walkingSpeedIntruder;
    private int numberTeleports;
    private float sprintingSpeedIntruder;
    private float timeStep;
    private int teleport;
    private String[] spawnAreaGuards;
    private String[] walls;

    public static final double FIELD_OF_VIEW = Math.toRadians(90);






    //public void setSpawnAreaGuards(int x1, int y1, int x2, int y2){this.spawnAreaGuards = new Spawn(x1,y1,x2,y2);}
    public int getMapHeight() {
        return MAP_HEIGHT;
    }
    public int getMapWidth() {
        return MAP_WIDTH;
    }
    public void setHeight(int height) {
        this.MAP_HEIGHT = height;
    }
    public int setNumberWalls(int numberWalls){
        return numberWalls;
    }
    public int setTeleport(int teleport){ return teleport; }

    public void setWidth(int width) {
        this.MAP_WIDTH = width;
    }

    public void setScaling(float scaling) {
        this.scaling = scaling;
    }

    public void setNumberOfGuards(int numberOfGuards) {
        this.numberOfGuards = numberOfGuards;
    }

    public void setNumberOfIntruders(int numberOfIntruders) {
        this.numberOfIntruders = numberOfIntruders;
    }

    public void setWalkingSpeedGuard(float walkingSpeedGuard) {
        this.walkingSpeedGuard = walkingSpeedGuard;
    }

    public void setSprintingSpeedGuard(float sprintingSpeedGuards) {
        this.sprintingSpeedGuard = sprintingSpeedGuards;
    }

    public void setWalkingSpeedIntruder(float walkingSpeedIntruder) { this.walkingSpeedIntruder = walkingSpeedIntruder; }

    public void setSpawnAreaGuards(){
        int[] separate=new int[spawnAreaGuards.length];
        for (int i = 0; i < spawnAreaGuards.length; i++) {
            separate[i]=Integer.parseInt(spawnAreaGuards[i]);
        }}
    public void setWalls(){
        int[] separate=new int[walls.length];
        for (int i = 0; i < walls.length; i++) {
            separate[i]=Integer.parseInt(walls[i]);
        }}

}

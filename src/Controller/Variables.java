package Controller;
import java.util.ArrayList;

/**
 * TO DO: add a getter and a setter for @distanceViewing and adapt the parser accordingly
 */

public class Variables {

    //simple type variables
    private boolean unlock=true;

    private String name;
    private String gameFile;
    private int mode;
    private int height;
    private int width;
    private float scaling;
    private int numberOfGuards;
    private int numberOfIntruders;
    private float walkingSpeedGuard;
    private float sprintingSpeedGuard;
    private float walkingSpeedIntruder;
    private float sprintingSpeedIntruder;
    private float timeStep;
    private int visionRange;
    public static double FIELD_OF_VIEW = 150.0;

    private Teleport teleport1;
    private Shade shade1;
    private Texture texture1;

    private int distanceViewing;
    private int distanceSmelling;
    private int distanceHearingWalking;
    private int distanceHearingSprinting;
    private int numberMarkers;


    /**
     * TODO:how to define texture type?
     */

    //complex type variables
    private ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<Teleport> portals = new ArrayList<Teleport>();
    private ArrayList<Shade> shades = new ArrayList<Shade>();
    private ArrayList<Texture> textures = new ArrayList<Texture>();
    private Target target;
    private Spawn spawnAreaGuards;
    private Spawn spawnAreaIntruders;
    private Goal goal;



    public Variables(){
        /**
         * constructor for TODO
         */
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
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

    public void setVisionRange (int visionRange) {this.visionRange = visionRange;}

    public void setSpawnAreaGuards(int x1, int y1, int x2, int y2){this.spawnAreaGuards = new Spawn(x1,y1,x2,y2);}

    public void setSpawnAreaIntruders(int x1, int y1, int x2, int y2){this.spawnAreaIntruders = new Spawn(x1,y1,x2,y2);}

    public void setDistanceSmelling(int distance){
        this.distanceSmelling = distance;
    }

    public void setDistanceHearingWalking(int distance){
        this.distanceHearingWalking = distance;
    }

    public void setDistanceHearingSprinting(int distance){
        this.distanceHearingSprinting = distance;
    }

    public void setNumberMarkers(int nr){
        this.numberMarkers = nr;
    }

    public void setWalkingSpeedGuard(float walkingSpeedGuard) {
        this.walkingSpeedGuard = walkingSpeedGuard;
    }

    public void setSprintingSpeedGuard(float sprintingSpeedGuards) {
        this.sprintingSpeedGuard = sprintingSpeedGuards;
    }

    public void setWalkingSpeedIntruder(float walkingSpeedIntruder) {
        this.walkingSpeedIntruder = walkingSpeedIntruder;
    }


    public void setTargetArea(int x1, int y1, int x2, int y2){
        this.target = new Target(x1,y1,x2,y2);
    }

    public void setDistanceViewing(int distanceViewing) {
        this.distanceViewing = distanceViewing;
    }



    /**
     * TODO:methods for first todo
     */
    public ArrayList <Wall> getWalls() {return walls;} //to be PARSED

    public ArrayList<Teleport> getPortals(){return portals;}

    public ArrayList<Shade> getShades(){return shades;}

    public int getMode() {
        return mode;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getNumberOfGuards() {
        return numberOfGuards;
    }

    public int getNumberOfIntruders() {
        return numberOfIntruders;
    }

    public Spawn getSpawnAreaGuards(){return spawnAreaGuards;}
    //we'll have to edit methods in Agent later, spawn is a rectangle like a wall

    public Spawn getSpawnAreaIntruders(){return  spawnAreaIntruders;}

    public double getWalkingSpeedGuard() {
        return walkingSpeedGuard;
    }

    public double getSprintingSpeedGuard() {
        return sprintingSpeedGuard;
    }

    public double getWalkingSpeedIntruder() {
        return walkingSpeedIntruder;
    }

    public double getSprintingSpeedIntruder() {
        return sprintingSpeedIntruder;
    }

    public double getScaling() {
        return scaling;
    }

    public double getTimeStep() {
        return timeStep;
    }

    public int getVisionRange(){ return visionRange;}



    public int getDistanceSmelling(){return this.distanceSmelling;}

    public int getDistanceHearingWalking(){return this.distanceHearingWalking;}

    public int getDistanceHearingSprinting(){return this.distanceHearingSprinting;}

    public int getNumberMakers(){return this.numberMarkers;}

    public int getDistanceViewing(){return this.distanceViewing;}


    /** POINT GETTERS*/

    public ArrayList<int[]> getWallPoints(){
        ArrayList<int[]> points = new ArrayList<>();
        for(Wall w: walls){
            points.addAll(w.getPoints());
        }
        return points;
    }

    public ArrayList<int[]> getShadePoints(){
        ArrayList<int[]> points = new ArrayList<>();
        for(Shade s: shades){
            points.addAll(s.getPoints());
        }
        return points;
    }

    public ArrayList<int[]> getGuardSpawnPoints(){
        return spawnAreaGuards.getPoints();
    }

    public ArrayList<int[]> getIntruderSpawnPoints(){
        return spawnAreaIntruders.getPoints();
    }

    public ArrayList<int[]> getGoalPoints(){
        return goal.getPoints();
    }




    public void createWall(int x1, int y1, int x2, int y2){
        if(unlock){
            this.walls.add(new Wall(x1,y1,x2,y2));
        }
    }

    public void createSpawnAreaGuards(int x1, int y1, int x2, int y2){
        if(unlock){
            this.spawnAreaGuards = new Spawn(x1,y1,x2,y2);
        }
    }

    public void createSpawnAreaIntruders(int x1, int y1, int x2, int y2){
        if(unlock){
            this.spawnAreaIntruders = new Spawn(x1,y1,x2,y2);
        }
    }


    public void createTeleport(int x1, int y1, int x2, int y2, int x3, int y3, double degree){
        if(unlock){
            this.portals.add(new Teleport(x1,y1,x2,y2,x3,y3,degree));
        }
    }

    public void createShade(int x1, int y1, int x2, int y2){
        if(unlock){
            this.shades.add(new Shade(x1,y1,x2,y2));
        }
    }


    public void createTexture(int x1, int x2, int x3, int x4, int x5, int x6) {
        if(unlock){
            this.textures.add(new Texture(x1,x2,x3,x4,x5,x6));
        }
    }


    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("walls: "+walls.size());
        return s.toString();
    }


    public void createGoal(int x1, int y1, int x2, int y2) {
        if(unlock){
            this.goal = new Goal(x1,y1,x2,y2);
        }
    }
}

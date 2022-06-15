package base;

import java.util.ArrayList;

import Controller.Map;
import Controller.Variables;
import rayTracer.RayCaster;

public class Agent {

    public Variables v=new Variables();
    private final int[] position;
    private double angleDeg;
    private int visionRange;

    RayCaster rayEngine;
    public ArrayList<int[]> visionT; //make a getter

    private final ArrayList<int[]> savedPath;
    private byte actionPerformed;
    private int[] previousState;
    private int[] spawnPosition;

    private Trace trace;

    private AudioObject audioObject = null;

    private boolean isOnTower;

    public Agent(int[] position) {
        this.position = position;
        this.angleDeg = 180;
        this.rayEngine = new RayCaster(this);
        this.visionT = new ArrayList<>();
        this.visionRange = GameController.variables.getVisionRange();

        spawnPosition = new int[] {position[0], position[1]};
        savedPath = new ArrayList<>();
        savedPath.add(new int[] {position[0], position[1]});
        previousState = new int[2];

        trace = new Trace(this);

        isOnTower = false;
    }

    public void putBackOnSpawn() {
        // reset position
        setPosition(spawnPosition[0], spawnPosition[1]);
        savedPath.add(new int[] {spawnPosition[0], spawnPosition[1]});
        previousState = new int[]{,};

        // reset trace
        trace.resetTrace();

        // reset guard params
        if(this.getClass() == Guard.class){
            ((Guard) this).resetParam();
        }

        // reset audio object
        audioObject = null;
    }

    public int[][] getAllNeighbours(){
        ArrayList<int[]> validNeighbours = new ArrayList<>();

        int
                x = getX(),
                y = getY();

        int[]
                right = {x+1, y},
                left = {x-1, y},
                up = {x, y+1},
                down = {x, y-1};

        if(Map.inMap(right) && !GameController.map.hasWall(right))
            validNeighbours.add(right);
        if(Map.inMap(left) && !GameController.map.hasWall(left))
            validNeighbours.add(left);
        if(Map.inMap(up) && !GameController.map.hasWall(up))
            validNeighbours.add(up);
        if(Map.inMap(down) && !GameController.map.hasWall(down))
            validNeighbours.add(down);

        int[][] neighbours = new int[validNeighbours.size()][2];

        for (int i = 0; i < validNeighbours.size(); i++) {
            neighbours[i] = validNeighbours.get(i);
        }
        return neighbours;
    }

    public int[] getPosition () {
        return position;
    }

    public void setPosition ( int x, int y){
        position[0] = x;
        position[1] = y;
    }

    public double getAngleDeg () {
        return this.angleDeg;
    }

    public int getX () {
        return position[0];
    }

    public int getY () {
        return position[1];
    }

    public void setX(int x) {this.position[0] = x;}

    public void setY(int y) {this.position[1] = y;}

    public void setAngleDeg ( double a){
        this.angleDeg = a;
    }

    public ArrayList<int[]> getVisionT () {
        return visionT;
    }

    public RayCaster getRayEngine () {
        return rayEngine;
    }

    public int[] getSpawnPosition () {
        return spawnPosition;
    }

    public void setSpawnPosition ( int[] spawnPosition){
        this.spawnPosition = spawnPosition;
    }

    public int[] getPreviousState () {
        return previousState;
    }

    public void setPreviousState ( int[] previousState){
        this.previousState = previousState;
    }

    public byte getActionPerformed () {
        return actionPerformed;
    }

    public void setActionPerformed ( byte actionPerformed){
        this.actionPerformed = actionPerformed;
    }

    public ArrayList<int[]> getSavedPath () {
        return savedPath;
    }

    public boolean isOnTower() {
        return isOnTower;
    }

    public void setOnTower(boolean onTower) {
        isOnTower = onTower;
    }

    public int getVisionRange() {
        return visionRange;
    }

    public void setVisionRange(int visionRange) {
        this.visionRange = visionRange;
    }

    public Trace getTrace() {
        return trace;
    }

    public void setTrace(Trace trace) {
        this.trace = trace;
    }

    public void updateStressLevel(byte numbOfOpponentsSeen){
        if(numbOfOpponentsSeen == 0)
            trace.setStressLevel(Trace.NO_STRESS);
        else if(numbOfOpponentsSeen == 1)
            trace.setStressLevel(Trace.MID_STRESS);
        else trace.setStressLevel(Trace.HIGH_STRESS);
    }

    public boolean hearsYell(){
        return this.audioObject != null;
    }

    public void setHeardAudio(AudioObject object){
        if (object instanceof Yell){
            this.audioObject = object;
        }else if (object instanceof Footstep){
            if (this.audioObject instanceof Yell){

            }else{
                this.audioObject = object;
            }
        }
    }

    public AudioObject getAudioObject() {
        return audioObject;
    }

    public void removeYell(){
        this.audioObject = null;
    }
}
package base;

import java.util.ArrayList;

import Controller.Tile;
import Controller.Variables;
import rayTracer.RayCaster;

public class Agent {

    public Variables v=new Variables();
    public Tile t;
    private final int[] position;
    private double angleDeg;
    private int visionRange;

    RayCaster rayEngine;
    public ArrayList<int[]> visionT; //make a getter

    private ArrayList<int[]> savedPath;
    private byte actionPerformed;
    private int[] previousState;
    private int[] spawnPosition;

    private ArrayList<int[]> visionArea = new ArrayList<>();
    private int agentId;
    private final int footsteps=5;
    private boolean[][] yellArray=new boolean[GameController.variables.getHeight()][GameController.variables.getWidth()];

    private boolean isOnTower;


    public Agent(int[] position) {
        this.position = position;
        this.angleDeg = 180;
        this.rayEngine = new RayCaster(this);
        this.visionT = new ArrayList<>();
        this.visionRange = GameController.variables.getVisionRange();

        spawnPosition = new int[] {position[0], position[1]};
        savedPath = new ArrayList<>();
        savedPath.add(position);
        previousState = new int[2];

        isOnTower = false;
    }

    public void putBackOnSpawn() {
        setPosition(spawnPosition[0], spawnPosition[1]);
        previousState = new int[]{,};
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

    public void setVisionArea (ArrayList < int[]>visionArea){
        this.visionArea = visionArea;
    }

    public int getID () {
        return agentId;
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
}
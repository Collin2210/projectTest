package base;

import java.util.ArrayList;

import Controller.Map;
import Controller.Tile;
import Controller.Variables;
import rayTracer.RayCaster;

public class Agent {

    public Variables v=new Variables();
    public Tile t;
    private final int[] position;
    private int angleDeg;

    private ArrayList<Trace> trace;


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


    public Agent(int[] position) {
        this.position = position;
        this.angleDeg = 180;
        this.rayEngine = new RayCaster(this);
        this.visionT = new ArrayList<>();

        spawnPosition = new int[] {position[0], position[1]};
        savedPath = new ArrayList<>();
        savedPath.add(position);
        previousState = new int[2];

        // initializing Trace
        trace = new ArrayList<>();
        Trace traceStep = new Trace(this);
        trace.add(traceStep);
    }

    public void AgentStep(){ //create a brand new trace for the current step
        Trace traceStep = new Trace(this);
        trace.add(traceStep);
        Tile tile = GameController.map.getTile(position[0], position[1]);
        tile.placeTrace(traceStep);
    }


    public void updateTrace() {
        for (int i = 0; i < trace.size(); i++) {
            if (trace.get(i).getLifeTime() > 0) {
                trace.get(i).decreaseLifeTime();
            } else if (trace.get(i).getLifeTime() == 0) { //update empty the corresponding Tile instances
                GameController.map.getTile(trace.get(i).getX_co(), trace.get(i).getY_co()).resetBoolTrace();
                GameController.map.getTile(trace.get(i).getX_co(), trace.get(i).getY_co()).resetTrace();
            }
        }
    }

    public void putBackOnSpawn(){
        setPosition(spawnPosition[0], spawnPosition[1]);
        previousState = new int[]{,};
    }

    public void resetTrace(Trace trace){
        //trace
    }

    public int[] getPosition () {
        return position;
    }

    public void setPosition ( int x, int y){
        position[0] = x;
        position[1] = y;
    }

    public int getAngleDeg () {
        return this.angleDeg;
    }

    public int getX () {
        return position[0];
    }

    public int getY () {
        return position[1];
    }


    public void setAngleDeg ( int a){
        this.angleDeg = a;
    }

    public void setVisionArea (ArrayList < int[]>visionArea){
        this.visionArea = visionArea;
    }
    ;

    public int getID () {
        return agentId;
    }
    ;

    public ArrayList<Trace> getTrace () {
        return trace;
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
}


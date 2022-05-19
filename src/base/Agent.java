package base;

import java.util.ArrayList;

import Controller.Map;
import Controller.Tile;
import rayTracer.RayCaster;

public class Agent {

//    public Variables v=new Variables();
    public Tile t;
    public Map map;
    private final int[] position;
    private float angle;
    private int angleDeg;
    private int DEGREESOFANGLE = 360;
    private Trace trace;

    RayCaster rayEngine;
    public ArrayList<int[]> visionT; //make a getter


    private ArrayList<int[]> visionArea = new ArrayList<>();
    private int agentId;
    private final int yell = 10;
    private final int footsteps=5;
    private boolean[][] yellArray=new boolean[GameController.variables.getHeight()][GameController.variables.getWidth()];


    public Agent(int[] position) {
        this.position = position;
        this.angle = (float) Math.toRadians(DEGREESOFANGLE);
        this.angleDeg = DEGREESOFANGLE;
        this.rayEngine = new RayCaster(this);
        this.visionT = new ArrayList<>();
        trace = new Trace(this);
    }

    public void moveRight(){
        int newX = position[0] + 1;
        if(Map.inMap(new int[]{newX, position[1]}))
            setPosition(newX, position[1]);
        Trace.UpdateTrace();
    }

    /**
     * get agent position from parser
     * how to set points in between
     */

    public void yellPosition() {
        int yell = 10;
        //STEP 1: identifying the array limits: check if in Map !
        int yellRadiusUpY =  position[1] + yell;
        int yellRadiusDownY =  position[1] - yell;
        int yellRadiusUpX =  position[0]+ yell;
        int yellRadiusDownX =  position[0]- yell;

        for (int i = yellRadiusDownX; i < yellRadiusUpX; i++) {
            for (int j = yellRadiusDownX; j < yellRadiusDownY; j++) {
                map.getTile(i, j).setYell();
            }
        }
    }
//
//    public void updateFootstepsPosition() {
//        int footstepsRadiusUpY = position[1] + yell;
//        int footstepsRadiusDownY = position[1] - yell;
//        int footstepsRadiusUpX = position[0] + yell;
//        int footstepsRadiusDownX = position[0] - yell;
//        for (int i = 0; i < map.getMap().length; i++) {
//            if(Map.inMap(position[0],footstepsRadiusUpY) && Map.inMap(position[0],footstepsRadiusDownY) && Map.inMap(position[0],footstepsRadiusUpX) && Map.inMap(position[0],footstepsRadiusDownX)){
//                double x = (yell - position[0]) ^ 2;
//                double y = (yell - position[1]) ^ 2;
//                if ((x + y) < (yell ^ 2)) {
//                    map.getTile(position[1], footstepsRadiusUpY).setFootsteps();
//                    map.getTile(position[1], footstepsRadiusDownY).setFootsteps();
//                    map.getTile(position[0], footstepsRadiusUpX).setFootsteps();
//                    map.getTile(position[0], footstepsRadiusDownX).setFootsteps();
//                }
//            }
//        }
//    }


    public void moveUp(){
        int newY = position[1] + 1;
        if(Map.inMap(position[0], newY))
            setPosition(position[0], newY);
        Trace.UpdateTrace();
    }

    public void moveLeft(){
        int newX = position[0] - 1;
        if(Map.inMap(newX, position[1]))
            setPosition(newX, position[1]);
        Trace.UpdateTrace();
    }

    public void moveDown(){
        int newY = position[1] - 1;
        if(Map.inMap(position[0], newY))
            setPosition(position[0], newY);
        Trace.UpdateTrace();
    }

    /**
     * turn left and right by 90 degree, just angle, not square
     */
    public void turnRight(){
        if(this.angle>Math.toRadians(90)) {
            this.angle = (float) Math.toRadians((Math.toDegrees(this.angle) - 90) % 360);
        }
        else{
            this.angle = (float) Math.toRadians(360-(Math.toDegrees(this.angle)-90));
        }
    }



    public int[] getPosition(){
        return position;
    }

    public void setPosition(int x, int y){
        position[0] = x;
        position[1] = y;
    }

    public int getAngleDeg() {
        return angleDeg;
    }

    public int getX(){
        return position[0];
    }

    public int getY(){
        return position[1];
    }

    public float getAngle(){
        return this.angle;
    }

    public void setAngle(float a){
        this.angle = a;
    }

    public void setVisionArea(ArrayList<int[]> visionArea){this.visionArea = visionArea;};

    public int getID(){return agentId;};

    public Trace getAgentTrace(){return trace;}

    public ArrayList<int[]> getVisionT(){return visionT;}

}

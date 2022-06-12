package base;

import Controller.Map;
import QLearning.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Guard extends ExplorerAgent {

    private boolean isFollowingAgent = false;
    private Intruder intruderToCatch = null;
    private Yell yell = null;

    private int[] targetLocation;

    public Guard(int[] position) {
        super(position);
        isFollowingAgent = false;
        this.yell = new Yell(this);
    }

    public void makeMove(){
        //remove guard's yell
        //place yell if needed
        //yell.remove();
        checkVision();
        if(isFollowingAgent) {
            followIntruder();
        }else if (this.yell != null){
            followYell();
        }
        else {
            makeRandomMove();
        }
    }

    private void checkVision(){
        this.visionT = this.getRayEngine().getVisibleTiles(this);
        for(int[] tilePos : visionT){
            for(Agent a : GameController.agents ){
                if(a.getClass() == Intruder.class && !isFollowingAgent){
                    int ax = a.getX(), ay = a.getY();
                    if(ax == tilePos[0] && ay == tilePos[1]) {
                        isFollowingAgent = true;
                        intruderToCatch = (Intruder) a;
                        this.doYell();
                        /*
                        yell.propagateYell();
                        yelling = true;
                        */
                    }
                }
            }
        }
    }

    private boolean hasCaught(Intruder intruder){
        // We have to make sure to remove the intruder from the map after it is caught.
        return Arrays.equals(this.getPosition(), intruder.getPosition());
    }


    private void followIntruder(){
        byte action = getActionThatMinimizesDistance();
        tryPerformingAction(action);
        if (this.hasCaught(this.intruderToCatch)){
            // remove the agent from the map as well.
            this.intruderToCatch = null;
            this.isFollowingAgent = false;
        }
    }


    private byte getActionThatMinimizesDistance(){
        int[] position = new int[]{intruderToCatch.getX(), intruderToCatch.getY()};
        return getActionThatMinimizesDistance(position);
    }


    private byte getActionThatMinimizesDistance(int[] position){
        byte action = 0;
        double smallest_distance = Double.POSITIVE_INFINITY;

        for (int act = 0; act < QLearning.NUMBER_OF_POSSIBLE_ACTIONS; act++) {
            int[] newPosition = tryToGetValidPosition(action);
            if(Map.inMap(newPosition[0],newPosition[1]) && !GameController.map.getTile(newPosition[0], newPosition[1]).hasWall()){
                double distance =
                        RewardTable.distanceBetweenPoints(
                                position[0], position[1],
                                newPosition[0], newPosition[1] );

                if(distance < smallest_distance){
                    smallest_distance = distance;
                    action = (byte) act;
                }
            }
        }
        return action;
    }

    private int[] tryToGetValidPosition(byte action){
        int[] vp = {};
        try {
            vp = getValidPositionFromAction(action);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return vp;
    }

    public boolean isFollowingAgent() {
        return isFollowingAgent;
    }

    public Intruder getIntruderToCatch() {
        return intruderToCatch;
    }

    /*
    public void startYelling(){
        yell = new Yell(this);
        yelling = true;
        GameController.yells.add(yell);
    }
     */

    public void endYelling(){

    }
    public void setYell(Yell yell){
        this.yell = yell;
    }

    public void doYell(){
        Yell yellObject = new Yell(this);
        yellObject.doYell();
        this.yell = yellObject;
        GameController.yells.add(yellObject);
    }



    public void hearingYell(){
        if(!isFollowingAgent){

        }
    }

    public void followYell()
    {
        int[] yellPosition = this.yell.getYellPosition();
        byte action = getActionThatMinimizesDistance(yellPosition);
        tryPerformingAction(action);
    }
}

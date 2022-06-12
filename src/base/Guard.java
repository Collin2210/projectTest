package base;

import QLearning.*;

import java.util.Arrays;

import static base.GameController.agents;

public class Guard extends ExplorerAgent {

    private boolean isFollowingAgent;
    private Intruder intruderToCatch;
    private Yell yell;
    private boolean yelling;
    private boolean isScatterMode;
    private byte timer; // time guard spent chasing without seeing the intruder
    private static final byte TIME_LIMIT = 2; // maximum time guard will spend chasing without seeing the intruder before scattering

    private double[] explorationArea; // {ogX, ogY, height, width}

    public Guard(int[] position) {
        super(position);
        isFollowingAgent = false;
        yelling = false;
        this.yell = new Yell(this);
        isScatterMode = true;
        timer = 0;
    }

    public void makeMove(){

        yell.remove();

        checkVision();

        if(timer == TIME_LIMIT){
            isScatterMode = true;
            timer = 0;
        }

        if(isScatterMode()) {
            makeExplorationMove();
        }
        else {
            followIntruder();
        }
    }

    private void checkVision(){
        boolean intruderIsSeen = false;
        this.visionT = this.getRayEngine().getVisibleTiles(this);
        for(int[] tilePos : visionT){
            for(Agent a : GameController.agents){
                if(a.getClass() == Intruder.class){
                    int ax = a.getX(), ay = a.getY();
                    if(ax == tilePos[0] && ay == tilePos[1]) {
                        isFollowingAgent = true;
                        intruderToCatch = (Intruder) a;
                        yell.propagateYell();
                        yelling = true;
                        isScatterMode = false;
                        intruderIsSeen = true;
                    }
                }
            }
        }
        if(!intruderIsSeen)
            timer++;
    }

    private void followIntruder(){
        byte action = getActionThatMinimizesDistance();
        tryPerformingAction(action);
    }

    private byte getActionThatMinimizesDistance(){
        byte action = 0;
        double smallest_distance = Double.POSITIVE_INFINITY;

        for (int act = 0; act < QLearning.NUMBER_OF_POSSIBLE_ACTIONS; act++) {
            int[] newPosition = tryToGetValidPosition(action);
            double distance = RewardTable.distanceBetweenPoints(
                    intruderToCatch.getX(), intruderToCatch.getY(),
                    newPosition[0], newPosition[1]
            );

            if(distance < smallest_distance){
                smallest_distance = distance;
                action = (byte) act;
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

    public boolean isScatterMode(){
        return isScatterMode;
    }

}

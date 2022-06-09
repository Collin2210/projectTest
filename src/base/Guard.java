package base;

import QLearning.*;

public class Guard extends ExplorerAgent {

    private boolean isFollowingAgent;
    private Intruder intruderToCatch;
    private Yell yell;
    private boolean yelling;
    private boolean isScatterMode;
    private boolean isOnTower;

    public Guard(int[] position) {
        super(position);
        isFollowingAgent = false;
        yelling = false;
        this.yell = new Yell(this);
        isScatterMode = true;
        isOnTower = false;
    }

    public void makeMove(){
        //remove guard's yell
        //place yell if needed
        yell.remove();
        while(isScatterMode()) {
            //if on a tower, checkVision should change

            //not on a tower
            checkVision();
            if (isFollowingAgent || yelling) {
                followIntruder();
            } else {
                isScatterMode = true;
            }
        }
    }

    private void checkVision(){
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
                        }
                    }
                }
            }
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

    public boolean isOnTower(){return isOnTower;}
}

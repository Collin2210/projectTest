package base;

import QLearning.*;

public class Guard extends ExplorerAgent {

    private boolean isFollowingAgent;
    private Intruder intruderToCatch;
    private Yell yell;
    private boolean yelling;
    private boolean isScatterMode;

    private double[] explorationArea; // {ogX, ogY, height, width}

    public Guard(int[] position) {
        super(position);
        isFollowingAgent = false;
        yelling = false;
        this.yell = new Yell(this);
        isScatterMode = true;
    }

    public void makeMove(){
        yell.remove();

        checkVision();

        if(isScatterMode())
            makeExplorationMove();
        else {
            followIntruder();
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

    public void setExplorationArea(double[] area){
        this.explorationArea = area;
    }

    public double[] getExplorationArea() {
        return explorationArea;
    }
}

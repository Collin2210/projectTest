package base;

import Controller.Map;
import QLearning.QLearning;
import QLearning.RewardTable;

import static QLearning.QLearning.*;
import static base.GameController.agents;

public class Intruder extends LearnerAgent{

    public Intruder(int[] position) {
        super(position);
    }

    private byte getActionThatMaximizesDistance(int[] position){
        byte action = 0;
        double biggest_distance = Double.NEGATIVE_INFINITY;

        for (int act = 0; act < QLearning.NUMBER_OF_POSSIBLE_ACTIONS; act++) {
            int[] newPosition = tryToGetValidPosition(action);
            if(Map.inMap(newPosition[0],newPosition[1]) && !GameController.map.getTile(newPosition[0], newPosition[1]).hasWall()){
                double distance =
                        RewardTable.distanceBetweenPoints(
                                position[0], position[1],
                                newPosition[0], newPosition[1] );

                if(distance > biggest_distance){
                    biggest_distance = distance;
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

    private boolean newPositionIsValid(int newX, int newY) {
        return Map.inMap(newX, newY);
    }


    int[] getValidPositionFromAction(byte action) throws Exception {
        int[] currentState = this.getPosition();
        int[] newPositionData = getNewPositionFromAction(action, currentState);

        if(!newPositionIsValid(newPositionData[0], newPositionData[1])){
            action = getRandomAction();
            setActionPerformed(action);
            return getValidPositionFromAction(action);
        }
        // set angle
        this.setAngleDeg(newPositionData[2]);
        return new int[]{newPositionData[0], newPositionData[1], (int) this.getAngleDeg()};
    }

    public static int[] getNewPositionFromAction(byte action, int[] currentState) throws Exception {
        int angle;
        int newX = currentState[0], newY = currentState[1];
        if(action == MOVE_UP) {
            newX -= 1;
            angle = 180;
        }
        else if (action == MOVE_RIGHT){
            newY += 1;
            angle = 90;
        }
        else if (action == MOVE_DOWN){
            newX += 1;
            angle = 0;
        }
        else if (action == MOVE_LEFT) {
            newY -= 1;
            angle = 270;
        }
        else {
            throw new Exception("action number not recognized");
        }
        return new int[]{newX, newY, angle};
    }

    public void getsCaught(){
        GameController.intrudersCaught.add(this);
        agents.remove(this);

        // reset all guards that were chasing him
        for(Agent a : agents){
            if (a instanceof Guard){
                // if guard is chasing this intruder
                if(((Guard) a).getIntruderToCatch() == this){
                    // reset to scatter
                    ((Guard) a).resetToScatterMode();
                }
            }
        }

        // reset trace
        getTrace().resetTrace();
    }

    public boolean isCaught(){
        return (GameController.intrudersCaught.contains(this));
    }
}

package base;

import QLearning.QLearning;

import static QLearning.QLearning.*;
import static base.GameController.teleporters;

public class ExplorerAgent extends Agent{
    public ExplorerAgent(int[] position) {
        super(position);
    }

    public void makeRandomMove(){
        byte action = QLearning.getRandomAction();
        tryPerformingAction(action);
    }

    public void tryPerformingAction(byte action){
        try {
            performAction(action);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void performAction(byte action) throws Exception {
        int[] newPosition = getValidPositionFromAction(action);

        // check if action takes you to a teleporter
        for(Teleporter t : teleporters){
            if(t.position[0] == newPosition[0] && t.position[1] == newPosition[1])
                newPosition = t.destination;
        }

        int[] newState = new int[]{newPosition[0], newPosition[1]};
        this.setPosition(newState[0], newState[1]);
        this.visionT.clear();
        this.getRayEngine().calculate(this);
        this.visionT = this.getRayEngine().getVisibleTiles(this);
    }

    int[] getValidPositionFromAction(byte action) throws Exception {
        int[] newPositionData = getNewPositionFromAction(action, getPosition());

        if(!newPositionIsValid(newPositionData[0], newPositionData[1])){
            if(action == NUMBER_OF_POSSIBLE_ACTIONS-1){
                return getValidPositionFromAction((byte) 0);
            }
            else {
                action = getRandomAction();
                return getValidPositionFromAction(action);
            }
        }
        // set angle
        this.setAngleDeg(newPositionData[2]);
        return new int[]{newPositionData[0], newPositionData[1]};
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

    private boolean newPositionIsValid(int newX, int newY) {
        return map.inMap(new int[]{newX, newY});
    }

}

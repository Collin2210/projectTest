package base;

import QLearning.QLearning;

import static QLearning.QLearning.*;
import static base.GameController.teleporters;

public class ExplorerAgent extends Agent{
    public ExplorerAgent(int[] position) {
        super(position);
    }

    public void move(){
        byte action = QLearning.getRandomAction();
        tryPerformingAction(action);

    }
    private void tryPerformingAction(byte action){
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
    }

    private int[] getValidPositionFromAction(byte action) throws Exception {
        int[] newPosition = getNewPositionFromAction(action, this.getPosition());

        if(!newPositionIsValid(newPosition[0],newPosition[1])){
            if(action == QLearning.NUMBER_OF_POSSIBLE_ACTIONS-1){
                return getValidPositionFromAction((byte) 0);
            }
            else {
                return getValidPositionFromAction(action += 1);
            }
        }
        return newPosition;
    }

    public static int[] getNewPositionFromAction(byte action, int[] currentState) throws Exception {
        int newX = currentState[0], newY = currentState[1];
        if(action == MOVE_UP)
            newX -= 1;
        else if (action == MOVE_RIGHT)
            newY += 1;
        else if (action == MOVE_DOWN)
            newX += 1;
        else if (action == MOVE_LEFT)
            newY -= 1;
        else {
            throw new Exception("action number not recognized");
        }
        return new int[]{newX, newY};
    }

    private boolean newPositionIsValid(int newX, int newY) {
        return Map.inMap(newX, newY);
    }
}

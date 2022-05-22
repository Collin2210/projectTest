package base;

import Controller.Map;
import Controller.Teleport;
import QLearning.QLearning;

import static QLearning.QLearning.*;
import static base.GameController.*;

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
        int[] currentState = this.getPosition();
        this.setActionPerformed(action);

        int[] newPosition = getValidPositionFromAction(action);

        // check if action takes you to a teleport
        for(Teleport t : portals){
            for(int[] in : t.getPointsIn()){
                if(newPosition[0] == in[0] && newPosition[1] == in[1]){
                    newPosition = t.getPointOut();
                    setAngleDeg(t.getDegreeOut());
                }
            }
        }

        int[] newState = new int[]{newPosition[0], newPosition[1]};
        setPreviousState(new int[]{currentState[0],currentState[1]});
        setPosition(newState[0], newState[1]);
        getSavedPath().add(new int[]{newPosition[0], newPosition[1]});
        visionT.clear();
        getRayEngine().calculate(this);
        this.visionT = this.getRayEngine().getVisibleTiles(this);
        this.updateTrace();
        this.AgentStep();
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
}

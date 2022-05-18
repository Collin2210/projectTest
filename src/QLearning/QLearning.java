package QLearning;

import base.*;

import java.util.Arrays;

import static base.GameController.isNotInTerminalState;
import static base.GameController.teleporters;

public class QLearning {

    public static final double
            LEARNING_RATE = 0.5,
            DISCOUNT_FACTOR = 0.9,
            RANDOMNESS_LEVEL = 0.5;
    public static final int
            LEARNING_CYCLES = 1000;
    public static final byte
            NUMBER_OF_POSSIBLE_ACTIONS = 4;
    public static final byte
            MOVE_UP = 0,
            MOVE_RIGHT = 1,
            MOVE_DOWN = 2,
            MOVE_LEFT = 3;

    final Map map;
    final Agent agent;
    QTable qTable;
    RewardTable rewardTable;

    byte actionPerformed;

    int[]
            currentState,
            previousState,
            spawnPosition;

    public QLearning(Agent agent) {
        this.map = GameController.map;
        this.agent = agent;
        currentState = agent.getPosition(); previousState = new int[]{-1, -1};
        qTable = new QTable(map);
        rewardTable = new RewardTable(agent);
        spawnPosition = new int[]{agent.getX(), agent.getY()};
    }

    public void learn(){
        qTable.initialize();

        for (int cycleCount = 0; cycleCount < LEARNING_CYCLES; cycleCount++) {
            while(isNotInTerminalState()){
                byte action = getNextAction();
                tryPerformingAction(action);
                updateQValue();
            }
            putAgentBackOnSpawn();
        }
    }

    public void moveSmartly(){
        byte action = getActionWithHighestQ();
        tryPerformingAction(action);

    }

    /** performs random action x% of the time, and performs action with highest Q all other times
     * */
    private byte getNextAction(){
        double rand = Math.random();
        if(rand <= RANDOMNESS_LEVEL) {
            return getRandomAction();
        }
        else{
            return getActionWithHighestQ();
        }
    }

    private byte getActionWithHighestQ(){
        return qTable.getActionWithHighestQAtState(currentState);
    }

    public static byte getRandomAction(){
        byte min = 0, max = NUMBER_OF_POSSIBLE_ACTIONS-1;
        return (byte) (Math.random() * (max - min) + min);
    }

    /** updates q-value at previous state after action is performed
     * */
    private void updateQValue(){
        double oldQ = qTable.getQ(previousState,actionPerformed);
        double TD = temporalDifference();
        double newQ = oldQ + LEARNING_RATE * TD; // as per the Bellman Equation
        qTable.setQ(previousState, actionPerformed, newQ);
    }

    /** returns how much the q-value changes at previous state after action is performed
     */
    private double temporalDifference(){
        double reward = rewardTable.getReward(currentState[0],currentState[1]);
        double maxQ = qTable.getHighestQAvailableAtPosition(currentState[0], currentState[1]);
        double oldQ = qTable.getQ(previousState,actionPerformed);
        return (reward + DISCOUNT_FACTOR * maxQ - oldQ);
    }

    private void tryPerformingAction(byte action){
        try {
            performAction(action);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void performAction(byte action) throws Exception {
        actionPerformed = action;
        int[] newPosition = getValidPositionFromAction(action);

        // check if action takes you to a teleporter
        for(Teleporter t : teleporters){
            if(t.position[0] == newPosition[0] && t.position[1] == newPosition[1])
                newPosition = t.destination;
        }

        int[] newState = new int[]{newPosition[0], newPosition[1]};
        previousState = new int[]{currentState[0],currentState[1]};
        agent.setPosition(newState[0], newState[1]);
        currentState = newState;
    }


    private void putAgentBackOnSpawn(){
        agent.setPosition(spawnPosition[0], spawnPosition[1]);
        previousState = new int[]{,};
        currentState = spawnPosition;
    }

    private boolean newPositionIsValid(int newX, int newY) {
        return Map.inMap(newX, newY);
    }

    private int[] getValidPositionFromAction(byte action) throws Exception {
        int[] newPosition = getNewPositionFromAction(action, currentState);

        if(!newPositionIsValid(newPosition[0],newPosition[1])){
            if(action == NUMBER_OF_POSSIBLE_ACTIONS-1){
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
}

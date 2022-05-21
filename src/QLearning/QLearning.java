package QLearning;

import Controller.Map;
import Controller.Teleport;
import base.*;

import static base.GameController.*;

public class QLearning {

    public static final double
            LEARNING_RATE = 0.5,
            DISCOUNT_FACTOR = 0.1,
            RANDOMNESS_LEVEL = 0.1;
    public static final int
            LEARNING_CYCLES = 300;
    public static final byte
            NUMBER_OF_POSSIBLE_ACTIONS = 4;
    public static final byte
            MOVE_UP = 0,
            MOVE_RIGHT = 1,
            MOVE_DOWN = 2,
            MOVE_LEFT = 3;

    public final Map map;
    public LearnerAgent agent;
    public QTable qTable;
    public RewardTable rewardTable;

    /**
     *
     * TODO: make rewardtable static, rn it's dynamically changed.
     * check the static reward table and sum it with occurances and
     * thus reward values from it's sensors.
     *
     * Test: raycasting singleAgentMethod
     *
     */

    public QLearning(LearnerAgent agent) {
        this.map = GameController.map;
        this.agent = agent;
        this.qTable = new QTable(map);
        rewardTable = new RewardTable(agent);
    }

    public void learn(){
        for(Agent a : agents){
            if(a.getClass().getSuperclass() == LearnerAgent.class){
                ((LearnerAgent)a).getBrain().getqTable().initialize();
            }
        }
        for (int cycleCount = 0; cycleCount < LEARNING_CYCLES; cycleCount++) {
            int moveCount = 0;
            while(!isInTerminalState()){
                for(Agent a : agents) {
                    if (a.getClass().getSuperclass() == LearnerAgent.class) {
                        this.agent = (LearnerAgent) a;
                        byte action = getNextAction();
                        tryPerformingAction(action);
                        updateQValue();
                    }
                }
                moveCount++;
            }
            System.out.print("Game Number: " +cycleCount);
            System.out.println(" Move count: " + moveCount);
            putAgentsBackOnSpawn();
        }
    }

    public void moveSmartly(){
        int moveCount = 0;
        while(!isInTerminalState()){
            for(Agent a : agents) {
                if(a.getClass().getSuperclass() == LearnerAgent.class) {
                    this.agent = (LearnerAgent) a;
                    byte action = getActionWithHighestQ();
                    tryPerformingAction(action);
                    updateQValue();
                }
                else if(a.getClass() == Guard.class)
                    ((Guard) a).makeMove();
            }
            moveCount++;
            GameController.print();
        }
        System.out.println("Move count: " + moveCount);
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
        int[] currentState = this.agent.getPosition();
        return this.agent.getBrain().getqTable().getActionWithHighestQAtState(currentState);
    }

    public static byte getRandomAction(){
        byte min = 0, max = NUMBER_OF_POSSIBLE_ACTIONS-1;
        return (byte) (Math.random() * (max - min) + min);
    }

    /** updates q-value at previous state after action is performed
     * */
    private void updateQValue(){
        int[] previousState = this.agent.getPreviousState();
        byte actionPerformed = this.agent.getActionPerformed();
        double oldQ = this.agent.getBrain().getqTable().getQ(previousState,actionPerformed);
        double TD = temporalDifference();
        double newQ = oldQ + LEARNING_RATE * TD; // as per the Bellman Equation
        this.agent.getBrain().getqTable().setQ(previousState, actionPerformed, newQ);
    }

    /** returns how much the q-value changes at previous state after action is performed
     */
    private double temporalDifference(){
        int[] currentState = this.agent.getPosition();
        int[] previousState = this.agent.getPreviousState();
        byte actionPerformed = this.agent.getActionPerformed();

        double reward = rewardTable.getReward(currentState[0],currentState[1]);
        double maxQ = this.agent.getBrain().getqTable().getHighestQAvailableAtPosition(currentState[0], currentState[1]);
        double oldQ = this.agent.getBrain().getqTable().getQ(previousState,actionPerformed);
        return (reward + DISCOUNT_FACTOR * maxQ - oldQ);
    }

    private void tryPerformingAction(byte action){
        try {
            performAction(action);
            //runRayCastSingleAgent();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void runRayCastSingleAgent(){
        agent.getRayEngine().calculate(agent);
        agent.visionT = agent.getRayEngine().getVisibleTiles(agent);
    }


    private void performAction(byte action) throws Exception {
        int[] currentState = this.agent.getPosition();
        agent.setActionPerformed(action);

        int[] newPosition = getValidPositionFromAction(action);

        // check if action takes you to a teleporter
//        for(Teleport t : teleporters){
//            if(t.position[0] == newPosition[0] && t.position[1] == newPosition[1])
//                newPosition = t.destination;
//        }

        int[] newState = new int[]{newPosition[0], newPosition[1]};
        agent.setPreviousState(new int[]{currentState[0],currentState[1]});
        agent.setPosition(newState[0], newState[1]);
        agent.getSavedPath().add(new int[]{newPosition[0], newPosition[1]});
        agent.visionT.clear();
        agent.getRayEngine().calculate(agent);
        agent.visionT = agent.getRayEngine().getVisibleTiles(agent);

        agent.updateTrace(); //decrease life time of every created trace
        agent.AgentStep(); //create a new trace for the current time step

    }


    private void putAgentsBackOnSpawn(){
        for(Agent a : agents) {
            this.agent = (LearnerAgent)a;
            agent.setPosition(agent.getSpawnPosition()[0], agent.getSpawnPosition()[1]);
            agent.setPreviousState(new int[]{,});
        }
    }

    private boolean newPositionIsValid(int newX, int newY) {
        return Map.inMap(newX, newY);
    }

    private int[] getValidPositionFromAction(byte action) throws Exception {
        int[] currentState = this.agent.getPosition();
        int[] newPositionData = getNewPositionFromAction(action, currentState);

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
        agent.setAngleDeg(newPositionData[2]);
        return new int[]{newPositionData[0], newPositionData[1], agent.getAngleDeg()};
    }

    public QTable getqTable() {
        return qTable;
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

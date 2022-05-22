package QLearning;

import Controller.Map;
import Controller.Tile;
import base.*;

import java.util.Arrays;

import static base.GameController.*;

public class QLearning {

    public static final double
            LEARNING_RATE = 0.5,
            DISCOUNT_FACTOR = 0.1,
            RANDOMNESS_LEVEL = 0.1;
    public static final int
            LEARNING_CYCLES = 300,
            MOVE_LIMIT = 100;
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
    public EMTable emTable;

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
        emTable = new EMTable(agent.getPosition());
    }

    public void learn(){
        for(Agent a : agents){
            if(a.getClass().getSuperclass() == LearnerAgent.class){
                ((LearnerAgent)a).getBrain().getQTable().initialize();
            }
        }
        for (int cycleCount = 0; cycleCount < LEARNING_CYCLES; cycleCount++) {
            int moveCount = 0;
            while(!GameEndChecker.isInTerminalState()){
                for(Agent a : agents) {
                    if (a.getClass().getSuperclass() == LearnerAgent.class) {
                        this.agent = (LearnerAgent) a;
                        this.agent.getBrain().getEmTable().updateEMtable(moveCount,this.agent.getPosition());
                        byte action = getNextAction();
                        tryPerformingAction(action);
                        updateQValue();
                    }
                    else if(a.getClass() == Guard.class){
                        ((Guard) a).makeMove();
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
        while(!GameEndChecker.isInTerminalState()){
            for(Agent a : agents) {
                if(a.getClass().getSuperclass() == LearnerAgent.class) {
                    this.agent = (LearnerAgent) a;
                    this.agent.getBrain().getEmTable().updateEMtable(moveCount,this.agent.getPosition());
                    byte action = getActionWithHighestQ();
                    tryPerformingAction(action);
                    updateQValue();
                    int newX = this.agent.getX(), newY = this.agent.getY(), index = agents.indexOf(a);
                    pathOfAllAgents.get(index).add(new int[]{newX,newY});
                }
                else if(a.getClass() == Guard.class){
                    ((Guard) a).makeMove();
                    int newX = this.agent.getX(), newY = this.agent.getY(), index = agents.indexOf(a);
                    pathOfAllAgents.get(index).add(new int[]{newX,newY});
                }
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
        return this.agent.getBrain().getQTable().getActionWithHighestQAtState(currentState);
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
        double oldQ = this.agent.getBrain().getQTable().getQ(previousState,actionPerformed);
        double TD = temporalDifference();
        double newQ = oldQ + LEARNING_RATE * TD; // as per the Bellman Equation
        this.agent.getBrain().getQTable().setQ(previousState, actionPerformed, newQ);
    }

    /** returns how much the q-value changes at previous state after action is performed
     */
    private double temporalDifference(){
        int[] currentState = this.agent.getPosition();
        int[] previousState = this.agent.getPreviousState();
        byte actionPerformed = this.agent.getActionPerformed();

        double reward = rewardTable.getReward(currentState[0],currentState[1]);
        double maxQ = this.agent.getBrain().getQTable().getHighestQAvailableAtPosition(currentState[0], currentState[1]);
        double oldQ = this.agent.getBrain().getQTable().getQ(previousState,actionPerformed);
        return (reward + DISCOUNT_FACTOR * maxQ - oldQ);
    }

    private void tryPerformingAction(byte action){
        try {
            performAction(action);
            runRayCastSingleAgent();

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
        for(int[] portalIn : portalEntrances){
            if(portalIn[0] == newPosition[0] && portalIn[1] == newPosition[1]){
                int index = portalEntrances.indexOf(portalIn);
                newPosition = portalDestinations.get(index);
                agent.setAngleDeg(portalDegrees.get(index));
            }
        }
        int[] newState = new int[]{newPosition[0], newPosition[1]};
        agent.setPreviousState(new int[]{currentState[0],currentState[1]});
        agent.setPosition(newState[0], newState[1]);
        agent.getSavedPath().add(new int[]{newPosition[0], newPosition[1]});
        agent.visionT.clear();
        agent.getRayEngine().calculate(agent);
        agent.visionT = agent.getRayEngine().getVisibleTiles(agent);
        agent.updateTrace();
        agent.AgentStep();
    }


    private void putAgentsBackOnSpawn(){
        for(Agent a : agents) {
            a.putBackOnSpawn();
            a.getTrace().clear(); // reset the value of trace stored in the Agent Object
        }
        for(Tile[] row : map.getTiles()){// reset the value of trace stored in the Tile Object
            for(Tile t : row)
                if(t.hasTrace())
                    t.resetTrace();
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
        return new int[]{newPositionData[0], newPositionData[1], (int) agent.getAngleDeg()};
    }

    public QTable getQTable() {
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

    public LearnerAgent getAgent() {
        return agent;
    }

    public void setAgent(LearnerAgent agent) {
        this.agent = agent;
    }

    public QTable getqTable() {
        return qTable;
    }

    public void setqTable(QTable qTable) {
        this.qTable = qTable;
    }

    public RewardTable getRewardTable() {
        return rewardTable;
    }

    public void setRewardTable(RewardTable rewardTable) {
        this.rewardTable = rewardTable;
    }

    public EMTable getEmTable() {
        return emTable;
    }

    public void setEmTable(EMTable emTable) {
        this.emTable = emTable;
    }
}

package QLearning;

import Controller.Map;
import Controller.Tile;
import base.*;

import java.util.ArrayList;

public class RewardTable {

    private final double[][] table;
    private final Map map;
    private final Agent agent;
    private static final int
            WALL_REWARD = -1000,
            GOAL_REWARD = 1000,
            STEP_COST = -1,
            YELL_REWARD = 10;

    private int[] ValueVector = new int[12];

    private int rewardOfOccurrences;

    public RewardTable(Agent agent) {
        this.map = GameController.map;
        this.agent = agent;
        int height = GameController.variables.getHeight(),
                width = GameController.variables.getWidth();
        this.table = new double[height][width];

        initialize(); // basically sets rewards for walls and goal

        initialValueVector();

        if(agent.getClass() == Intruder.class) {
            setDistanceToGoalReward();
        }
    }

    private void initialize(){
        for (int x = 0; x < table.length; x++) {
            for (int y = 0; y < table[x].length; y++) {
                if(map.getTile(x,y).isGoal()) {
                    if(agent.getClass() == Intruder.class){
                        table[x][y] = GOAL_REWARD;
                    }
                }
                else if (map.getTile(x,y).hasWall())
                    table[x][y] = WALL_REWARD;
                else if (!map.getTile(x,y).hasWall() && !map.getTile(x,y).isGoal()){
                    table[x][y] = STEP_COST;
                }
            }
        }
    }

    public void initialValueVector(){
        ValueVector[0] = 10; //#CASE 1 : (STRESS = GREEN) ; (TRACE = INTRUDER) ; (AGENT = INTRUDER)
        ValueVector[1] = -5; //#CASE 2 : (STRESS = GREEN) ; (TRACE = GUARD) ; (AGENT = GUARD)

        ValueVector[2] = -10;//#CASE 3 : (STRESS = GREEN) ; (TRACE = GUARD) ; (AGENT = INTRUDER)
        ValueVector[3] = +10;//#CASE 4 : (STRESS = GREEN) ; (TRACE = INTRUDER) ; (AGENT = GUARD)

        //#LEVEL 1 STRESS: One opponent detected
        ValueVector[4] = -20; //#CASE 5 : (STRESS = ORANGE) ; (TRACE = INTRUDER) ; (AGENT = INTRUDER)
        ValueVector[5] = +20; //#CASE 6 : (STRESS = ORANGE) ; (TRACE = GUARD) ; (AGENT = GUARD)

        ValueVector[6] = -20;//#CASE 7 : (STRESS = ORANGE) ; (TRACE = GUARD) ; (AGENT = INTRUDER)
        ValueVector[7] = +5;//#CASE 8 : (STRESS = ORANGE) ; (TRACE = INTRUDER) ; (AGENT = GUARD)

        //#LEVEL 2 STRESS : more than 1 opponents detected
        ValueVector[8] = -30; //#CASE 9 : (STRESS = RED) ; (TRACE = INTRUDER) ; (AGENT = INTRUDER)
        ValueVector[9] = +15; //#CASE 10 : (STRESS = RED) ; (TRACE = GUARD) ; (AGENT = GUARD)

        ValueVector[10] = -10;//#CASE 11 : (STRESS = RED) ; (TRACE = GUARD) ; (AGENT = INTRUDER)
        ValueVector[11] = +10;//#CASE 12 : (STRESS = RED) ; (TRACE = INTRUDER) ; (AGENT = GUARD)
    }

    public double getOccurenceReward(int x, int y){
        int totalReward = 0;
        if(map.getTile(x,y).hasYell()){
            totalReward += YELL_REWARD;
        }
        if(map.getTile(x,y).hasTrace()){
            Trace trace = map.getTile(x,y).getTrace();
            //System.out.println("IT GETS HERE, YOUR SHIT WORKS");
            if(!trace.getOwner().equals(this.agent)){
                if(agent.getClass() == Guard.class){
                    if(trace.getOwner().getClass() == Guard.class){
                        if(trace.getStress() == 0)
                            totalReward+=ValueVector[1];
                        else if(trace.getStress() == 1)
                            totalReward += ValueVector[5];
                        else if(trace.getStress() == 2)
                            totalReward += ValueVector[9];
                    }
                    else if(trace.getOwner().getClass() == Intruder.class){
                        if(trace.getStress() == 0)
                            totalReward+=ValueVector[3];
                        else if(trace.getStress() == 1)
                            totalReward+=ValueVector[7];
                        else if (trace.getStress() == 2)
                            totalReward+=ValueVector[11];
                    }
                }
                else if(agent.getClass() == Intruder.class){
                    if(trace.getOwner().getClass() == Guard.class){
                        if(trace.getStress() == 0)
                            totalReward+=ValueVector[2];
                        else if(trace.getStress() == 1)
                            totalReward += ValueVector[6];
                        else if(trace.getStress() == 2)
                            totalReward += ValueVector[10];
                    }
                    else if(trace.getOwner().getClass() == Intruder.class){
                        if(trace.getStress() == 0)
                            totalReward+=ValueVector[0];
                        else if(trace.getStress() == 1)
                            totalReward+=ValueVector[4];
                        else if (trace.getStress() == 2)
                            totalReward+=ValueVector[8];
                    }
                }
            }
        }
        table[x][y] += totalReward;
        return totalReward;
    }


    /**
     * Convert into EM table
     */
    private void setDistanceToGoalReward(){

        for (int x = 0; x < table.length; x++) {
            for (int y = 0; y < table[x].length; y++) {
                if(!map.getTile(x,y).hasWall() && !map.getTile(x,y).isGoal()) {

                    table[x][y] -= getDistanceFromGoal(x,y);
                }
            }
        }
    }

    private double getDistanceFromGoal(int x, int y){
        double smallestDistance = Double.MAX_VALUE;
        for(Tile t : GameController.goalTiles){
            double distance = distanceBetweenPoints(x,y,t.getPosition()[0],t.getPosition()[1]);
            if(distance < smallestDistance)
                smallestDistance = distance;
        }
        return smallestDistance;
    }

    public static double distanceBetweenPoints(int xA,int yA, int xB, int yB){
        return Math.sqrt( Math.pow(xB - xA,2) + Math.pow(yB - yA,2) );
    }


    public double getReward(int stateX, int stateY){
        return table[stateX][stateY];
    }

    public void printTable(){
        for(double[] row : table){
            for(double b : row) {
                System.out.print(" " + b + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

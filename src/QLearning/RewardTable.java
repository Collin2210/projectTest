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

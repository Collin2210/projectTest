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
            YELL_REWARD = 10,
            GUARD_TILE_DISCOVERY_REWARD = 1;


    public void initialValueVector(){
        ValueVector[0] = 100; //#CASE 1 : (STRESS = GREEN) ; (TRACE = INTRUDER) ; (AGENT = INTRUDER)
        ValueVector[1] = -100; //#CASE 2 : (STRESS = GREEN) ; (TRACE = GUARD) ; (AGENT = GUARD)

        ValueVector[2] = -100;//#CASE 3 : (STRESS = GREEN) ; (TRACE = GUARD) ; (AGENT = INTRUDER)
        ValueVector[3] = +500;//#CASE 4 : (STRESS = GREEN) ; (TRACE = INTRUDER) ; (AGENT = GUARD)

        //#LEVEL 1 STRESS: One opponent detected
        ValueVector[4] = -200; //#CASE 5 : (STRESS = ORANGE) ; (TRACE = INTRUDER) ; (AGENT = INTRUDER)
        ValueVector[5] = +200; //#CASE 6 : (STRESS = ORANGE) ; (TRACE = GUARD) ; (AGENT = GUARD)

        ValueVector[6] = -2000;//#CASE 7 : (STRESS = ORANGE) ; (TRACE = GUARD) ; (AGENT = INTRUDER)
        ValueVector[7] = +500;//#CASE 8 : (STRESS = ORANGE) ; (TRACE = INTRUDER) ; (AGENT = GUARD)

        //#LEVEL 2 STRESS : more than 1 opponents detected
        ValueVector[8] = -3000; //#CASE 9 : (STRESS = RED) ; (TRACE = INTRUDER) ; (AGENT = INTRUDER)
        ValueVector[9] = +1500; //#CASE 10 : (STRESS = RED) ; (TRACE = GUARD) ; (AGENT = GUARD)

        ValueVector[10] = -1000;//#CASE 11 : (STRESS = RED) ; (TRACE = GUARD) ; (AGENT = INTRUDER)
        ValueVector[11] = +1000;//#CASE 12 : (STRESS = RED) ; (TRACE = INTRUDER) ; (AGENT = GUARD)
    }

    private int[] ValueVector = new int[12];

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
        checkTrace(this.agent);
    }

    private void initialize(){
        for (int x = 0; x < table.length; x++) {
            for (int y = 0; y < table[x].length; y++) {
                if(map.getTile(x,y).isGoal()) {
                    System.out.println("gets here 1");
                    /*if(agent.getClass() != Guard.class) {
                        System.out.println("gets here 2");
                        table[x][y] = GOAL_REWARD;
                        System.out.println("goal: x and y:"+ x +", "+ y);
                    }

                     */
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

    public double sumOfRewards(){
        int reward = 0;
        ArrayList<int[]> visionTable = agent.visionT;

        for (int[] pos:visionTable) {
//            if(map.getMap()[pos[0]][pos[1]].has
//            );

        }
        for (int x = 0; x < table.length; x++) {
            for (int y = 0; y < table[x].length; y++) {
                if(!map.getTile(x,y).hasWall() && !map.getTile(x,y).isGoal()) {

                }
            }
        }
        return 0;

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

    public void setGuardReward(){
        for (int x = 0; x < table.length; x++) {
            for (int y = 0; y < table[x].length; y++) {
                if(!map.getTile(x,y).hasWall() && !map.getTile(x,y).isGoal()) {
                    table[x][y] = GUARD_TILE_DISCOVERY_REWARD;
                }
                if(agent.getX() == x && agent.getY() == y)
                    table[x][y] = 0;
            }
        }
    }

    public void updateGuardReward(int[] seenTilePosition){
        // no reward if the agent goes back there
        int x = seenTilePosition[0], y = seenTilePosition[1];
        table[x][y] = 0;
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

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

    private int[] ValueVector = new int[12];

    public RewardTable(Agent agent) {
        this.map = GameController.map;
        this.agent = agent;
        int height = GameController.variables.getHeight(),
                width = GameController.variables.getWidth();
        this.table = new double[height][width];

        initialize(); // basically sets rewards for walls and goal

        if(agent.getClass() == Intruder.class)
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

    private void checkTrace(Agent a /*current exploring Agent*/) { //within vision range
        //#ZERO STRESS: zero opponent detected
        for( int i = 0; i < a.getVisionT()/* acquisition of vision range*/.size() ; i++){
            int x = a.getVisionT().get(i)[0];
            int y = a.getVisionT().get(i)[1];

            Trace trace = map.getTile(x, y).getTrace();

            int stressLevel = map.getTile(x, y).getTrace().getStress();
            if (stressLevel == 0) {// no opponent detected
                if (trace.isTeamTrace(a) == true) {
                    //#CASE 1
                    if (a.getClass() == Intruder.class) {
                        table[x][y] = ValueVector[0];
                    } else //#CASE 2
                        table[x][y] = ValueVector[1];
                } else {//#CASE 3
                    if (a.getClass() == Intruder.class) {
                        table[x][y] = ValueVector[2];
                    } else //#CASE 4
                        table[x][y] = ValueVector[3];
                }
            }
            if (stressLevel == 1) {//medium stress: 1 opponent detected
                if (trace.isTeamTrace(a) == true) {
                    //#CASE 5
                    if (a.getClass() == Intruder.class) {
                        table[x][y] = ValueVector[4];
                    } else //#CASE 6
                        table[x][y] = ValueVector[5];
                } else {//#CASE 7
                    if (a.getClass() == Intruder.class) {
                        table[x][y] = ValueVector[6];
                    } else //#CASE 8
                        table[x][y] = ValueVector[7];
                }
            }
            if (stressLevel > 1) {//high stress: more than 1 opponent detected
                if (trace.isTeamTrace(a) == true) {
                    //#CASE 9
                    if (a.getClass() == Intruder.class) {
                        table[x][y] = ValueVector[8];
                    } else //#CASE 10
                        table[x][y] = ValueVector[9];
                } else {//#CASE 11
                    if (a.getClass() == Intruder.class) {
                        table[x][y] = ValueVector[10];
                    } else //#CASE 12
                        table[x][y] = ValueVector[11];
                }

            }
        }
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

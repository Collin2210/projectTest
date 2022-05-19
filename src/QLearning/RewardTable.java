package QLearning;

import Controller.Map;
import Controller.Tile;
import base.*;

public class RewardTable {

    private final double[][] table;
    private final Map map;
    private final Agent agent;
    private static final int
            WALL_REWARD = -1000,
            GOAL_REWARD = 1000,
            STEP_COST = -1,
            GUARD_TILE_DISCOVERY_REWARD = 1;

    private int[] ValueVector = new int[12];

    public RewardTable(Agent agent) {
        this.map = GameController.map;
        this.agent = agent;
        this.table = new double[GameController.variables.getHeight()][GameController.variables.getWidth()];

        initialize(); // basically sets rewards for walls and goal

        if(agent.getClass() == Intruder.class)
            setDistanceToGoalReward();
        else if(agent.getClass() == Guard.class)
            setGuardReward();
        checkTrace(this.agent);
    }

    private void initialize(){
        for (int x = 0; x < table.length; x++) {
            for (int y = 0; y < table[x].length; y++) {
                if(map.getTile(x,y).isGoal()) {
                    if(agent.getClass() != Guard.class)
                        table[x][y] = GOAL_REWARD;
                }
                else if (map.getTile(x,y).hasWall())
                    table[x][y] = WALL_REWARD;
            }
        }
    }

    private void setDistanceToGoalReward(){
        for (int x = 0; x < table.length; x++) {
            for (int y = 0; y < table[x].length; y++) {
                if(!map.getTiles()[x][y].hasWall() && !map.getTiles()[x][y].isGoal()) {
                    table[x][y] = STEP_COST;
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
        for(Tile t : GameController.GOAL_TILE){
            double distance = distanceBetweenPoints(x,y,t.getXCoord(),t.getYCoord());
            if(distance < smallestDistance)
                smallestDistance = distance;
        }
        return smallestDistance;
    }

    public static double distanceBetweenPoints(int xA,int yA, int xB, int yB){
        return Math.sqrt( Math.pow(xB - xA,2) + Math.pow(yB - yA,2) );
    }

    /* METHOD: CHECK_TRACE
     * What it does ? Analyses all possible case of Agent interaction
     * to define the multi-agent component of the individual decision
     * */
    private void checkTrace( ) { //within vision range
        //#ZERO STRESS: zero opponent detected
        for( int m = 0; m < agent.getVisionT().size() ; m++){
            int x = agent.getVisionT()[m][0];
            int y = agent.getVisionT()()[m][1];

            int stressLevel = Map.getTile(x, y).getTrace().getStress();
            if (stressLevel == 0) {// no opponent detected
                if (Trace.isTeamTrace(agent) == true) {
                    //#CASE 1
                    if (agent.getClass() == Intruder.class) {
                        table[x][y] = ValueVector[0];
                    } else //#CASE 2
                        table[x][y] = ValueVector[1];
                } else {//#CASE 3
                    if (agent.getClass() == Intruder.class) {
                        table[x][y] = ValueVector[2];
                    } else //#CASE 4
                        table[x][y] = ValueVector[3];
                }
            }
            if (stressLevel == 1) {//medium stress: 1 opponent detected
                if (Trace.isTeamTrace(agent) == true) {
                    //#CASE 5
                    if (agent.getClass() == Intruder.class) {
                        table[x][y] = ValueVector[4];
                    } else //#CASE 6
                        table[x][y] = ValueVector[5];
                } else {//#CASE 7
                    if (agent.getClass() == Intruder.class) {
                        table[x][y] = ValueVector[6];
                    } else //#CASE 8
                        table[x][y] = ValueVector[7];
                }
            }
            if (stressLevel > 1) {//high stress: more than 1 opponent detected
                if (Trace.isTeamTrace(agent) == true) {
                    //#CASE 9
                    if (agent.getClass() == Intruder.class) {
                        table[x][y] = ValueVector[8];
                    } else //#CASE 10
                        table[x][y] = ValueVector[9];
                } else {//#CASE 11
                    if (agent.getClass() == Intruder.class) {
                        table[x][y] = ValueVector[10];
                    } else //#CASE 12
                        table[x][y] = ValueVector[11];
                }

            }
        }
    }

    public void yellReward() {
        int yell = 10;
        //STEP 1: identifying the array limits: check if in Map !
        int yellRadiusUpY = agent.getY() + yell;
        int yellRadiusDownY = agent.getY() - yell;
        int yellRadiusUpX = agent.getX() + yell;
        int yellRadiusDownX = agent.getX() - yell;

        for (int i = yellRadiusDownX; i < yellRadiusUpX; i++) {
            for (int j = yellRadiusDownX; j < yellRadiusDownY; j++) {
                table[i][j] = 600;
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

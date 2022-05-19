package QLearning;

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

    public RewardTable(Agent agent) {
        this.map = GameController.map;
        this.agent = agent;
        this.table = new double[Variables.MAP_HEIGHT][Variables.MAP_WIDTH];

        initialize(); // basically sets rewards for walls and goal

        if(agent.getClass() == Intruder.class)
            setDistanceToGoalReward();
        else if(agent.getClass() == Guard.class)
            setGuardReward();
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
                if(!map.getMap()[x][y].isWall() && !map.getMap()[x][y].isGoal()) {
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

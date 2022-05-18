package QLearning;

import base.*;

public class RewardTable {

    private final double[][] table;
    private final Map map;
    private static final int
            WALL_REWARD = -1000,
            GOAL_REWARD = 1000,
            STEP_COST = -1;

    public RewardTable() {
        this.map = GameController.map;
        this.table = new double[Variables.MAP_HEIGHT][Variables.MAP_WIDTH];

        initialize(); // basically sets rewards for walls and goal

        setDistanceToGoalReward();

    }

    private void initialize(){
        for (int x = 0; x < table.length; x++) {
            for (int y = 0; y < table[x].length; y++) {
                if(map.getTile(x,y).isGoal())
                    table[x][y] = GOAL_REWARD;
                else if (map.getTile(x,y).isWall())
                    table[x][y] = WALL_REWARD;
                else table[x][y] = STEP_COST;
            }
        }
    }

    private void setDistanceToGoalReward(){
        for (int x = 0; x < table.length; x++) {
            for (int y = 0; y < table[x].length; y++) {
                if(!map.getMap()[x][y].isWall() && !map.getMap()[x][y].isGoal()) {
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

    /** returns distance between guard and closest intruder
     * */
    private double getDistanceFromIntruder(int x, int y){
        double smallestDistance = Double.MAX_VALUE;
        for(Agent a : GameController.agents){
            double distance = distanceBetweenPoints(x,y,a.getX(),a.getY());
            if(distance < smallestDistance)
                smallestDistance = distance;
        }
        return smallestDistance;
    }


    private double distanceBetweenPoints(int xA,int yA, int xB, int yB){
        return Math.sqrt( Math.pow(xB - xA,2) + Math.pow(yB - yA,2) );
    }


    public double getReward(int stateX, int stateY){
        return table[stateX][stateY];
    }
}

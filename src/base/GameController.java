package base;

// TODO: 5/16/2022
//  create map
//  create agent
//  move agents
//  agent w/ raycasting
//  qlearning

import RayCasting.RayCasting;

import java.util.ArrayList;

public class GameController {

    public static final Map map = new Map();
    public static final ArrayList<Agent> agents = new ArrayList<>();
    public static final ArrayList<Tile> goalTiles = new ArrayList<>();

    public static int clock = 0;

    public GameController(){
    }

    public void makeAgentsLearn(){
        for(Agent a : agents) {
            if(a.getClass().getSuperclass() == LearnerAgent.class)
                ((LearnerAgent) a).learn();
        }
    }

    public void makeAgentsMoveSmartly(){
        for(Agent a : agents) {
            if(a.getClass().getSuperclass()==LearnerAgent.class) {
                ((LearnerAgent) a).moveSmartly();
            }
        }
    }

    public void addGoalTiles(ArrayList<int[]> goalPosition){
        for(Tile[] row : map.getMap()){
            for(Tile tile : row){
                for(int[] goal : goalPosition){
                    if(tile.isAtPosition(goal)) {
                        tile.setGoal();
                        goalTiles.add(tile);
                    }
                }
            }
        }
    }

    public void addGoalTiles(int[][] goalPosition){
        for(int[] goal : goalPosition){
            Tile goalTile = map.getTile(goal[0], goal[1]);
            goalTile.setGoal();
            goalTiles.add(goalTile);
        }
    }

    public void addWalls(int[][] wallPos){
        for(int[] wall : wallPos){
            Tile wallTile = map.getTile(wall[0], wall[1]);
            wallTile.setWall();
        }
    }

    public void addAgents(ArrayList<int[]> agentPositions){
        for(int[] position : agentPositions){
            agents.add(new Agent(position));
        }
    }

    public void addAgents(int[][] agentPositions){
        for(int[] position : agentPositions){
            agents.add(new Agent(position));
        }
    }

    public void addIntruder(int[][] intruderPositions){
        for(int[] position : intruderPositions){
            agents.add(new Intruder(position));
        }
    }

    public void addGuards(int[][] guardPositions){
        for(int[] position : guardPositions){
            agents.add(new Guard(position));
        }
    }

    public void printVisionT(ArrayList<int[]> viT){
        StringBuilder s = new StringBuilder();
        for(int[] t : viT) {
            int x = t[0], y = t[1];
            s.append("(").append(x+0.5).append(",").append(y+0.5).append("),");
        }
        System.out.println(s);
    }

    public static void print(){

        String
                ANSI_RESET = "\u001B[0m",
                ANSI_YELLOW = "\u001B[33m",
                ANSI_RED = "\u001B[31m",
                ANSI_PURPLE = "\u001B[35m",
                ANSI_BLUE = "\u001B[34m";

        for(Tile[] row : map.getMap()){
            for(Tile tile : row){
                boolean hasIntruder = false, hasGuard = false;
                for(Agent agent  : agents){
                    if(agent.getX() == tile.getPosition()[0]
                            && agent.getY() == tile.getPosition()[1]) {
                        if(agent.getClass() == Guard.class)
                            hasGuard = true;
                        else if (agent.getClass() == Intruder.class)
                            hasIntruder = true;
                    }
                }
                if((hasGuard || hasIntruder) && tile.isGoal()) {
                    if(hasGuard)
                        System.out.print(ANSI_BLUE + " Y " + ANSI_RESET);
                    else System.out.print(ANSI_PURPLE + " Y " + ANSI_RESET);
                }
                else if(hasGuard || hasIntruder) {
                    if(hasGuard)
                        System.out.print(ANSI_BLUE + " A " + ANSI_RESET);
                    else System.out.print(ANSI_RED + " A " + ANSI_RESET);
                }
                else if (tile.isGoal())
                    System.out.print(ANSI_YELLOW + " G " + ANSI_RESET);
                else if(tile.isWall())
                    System.out.print(" L ");
                else
                    System.out.print(" O ");
            }
            System.out.println();
        }
        System.out.println();
    }

}

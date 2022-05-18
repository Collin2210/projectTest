package base;

// TODO: 5/16/2022
//  create map
//  create agent
//  move agents
//  agent w/ raycasting
//  qlearning

import RayCasting.RayCasting;
import rayTracer.RayCaster;

import java.util.ArrayList;

public class GameController {

    public static final Map map = new Map();
    private final ArrayList<Agent> agents;
    private final ArrayList<Tile> goalTiles;

    private ArrayList<ArrayList<int[]>> visionOfAgents;
   // private ArrayList<RayCaster> listOfRayCasters;
    RayCaster rayEngine;

    public GameController(){
        agents = new ArrayList<>();
        goalTiles = new ArrayList<>();
        visionOfAgents = new ArrayList<>();
        //listOfRayCasters = new ArrayList<>();
    }

    public void runRaycast(){
        for (Agent a:agents
        ) {
            rayEngine = new RayCaster(a);
            //listOfRayCasters.add(rayEngine);
            rayEngine.calculate(a);
            ArrayList<int[]> visionT = rayEngine.getVisibleTiles(a);
            visionOfAgents.add(rayEngine.getVisibleTiles(a));
            printVisionT(visionT);
        }

    }


    public void addGoalTiles(ArrayList<int[]> goalPosition){
        for(Tile[] row : map.getMap()){
            for(Tile tile : row){
                for(int[] goal : goalPosition){
                    if(tile.isAtPosition(goal)) {
                        tile.setGoal();
                        goalPosition.remove(goal);
                    }
                }
            }
        }
    }

    public void addGoalTiles(int[][] goalPosition){
        for(Tile[] row : map.getMap()){
            for(Tile tile : row){
                for(int[] goal : goalPosition){
                    if(tile.isAtPosition(goal)) {
                        tile.setGoal();
                    }
                }
            }
        }
    }


    public void addAgents(int[][] agentPositions){
        for(int[] position : agentPositions){
            agents.add(new Agent(position));
        }
    }

    int counter = 0;


    public void printVisionT(ArrayList<int[]> viT){
        StringBuilder s = new StringBuilder();
        for(int[] t : viT) {
            int x = t[0], y = t[1];
            s.append("(").append(x+0.5).append(",").append(y+0.5).append("),");
        }
        System.out.println(s);
    }

    public void print(){

        System.out.println("num of seen tiles: " + visionOfAgents.get(0).size());
        int counter = 1;

        String
                ANSI_RESET = "\u001B[0m",
                ANSI_YELLOW = "\u001B[33m",
                ANSI_RED = "\u001B[31m",
                ANSI_PURPLE = "\u001B[35m";

        for(Tile[] row : map.getMap()){
            for(Tile tile : row){
                boolean hasAgent = false;
                for(Agent agent  : agents){
                    if(agent.getX() == tile.getPosition()[0]
                            && agent.getY() == tile.getPosition()[1])
                        hasAgent = true;
                }
                if(hasAgent && tile.isGoal())
                    System.out.print(ANSI_PURPLE + " Y " + ANSI_RESET);
                else if(hasAgent)
                    System.out.print(ANSI_RED + " A " + ANSI_RESET);
                else if (tile.isGoal())
                    System.out.print(ANSI_YELLOW + " G " + ANSI_RESET);
                else if(tile.isWall())
                    System.out.print(" L ");
                else {
                    boolean isSeen = false;
                    for(ArrayList<int[]> agentVis : visionOfAgents){
                        for(int[] tilePos : agentVis){
                            if(tilePos[0] == tile.getPosition()[0]
                            && tilePos[1] == tile.getPosition()[1])
                                isSeen = true;
                        }
                    }
                    if(isSeen) {
                        System.out.print(ANSI_PURPLE + " O " + ANSI_RESET);
                        counter++;
                    }
                    else System.out.print(" O ");
                }
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("counter = " + counter);
    }

}

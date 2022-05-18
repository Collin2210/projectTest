package base;

import java.util.ArrayList;
import java.util.Arrays;

public class GameController {

    public static final Map map = new Map();
    public static final ArrayList<Agent> agents = new ArrayList<>();
    public static final ArrayList<Tile> goalTiles = new ArrayList<>();
    public static final ArrayList<Teleporter> teleporters = new ArrayList<>();

    public static int clock = 0;

    public GameController(){
    }

    public static boolean isNotInTerminalState(){
        for(Agent a : GameController.agents){
            if(a.getClass() == Intruder.class){
                if(GameController.goalTiles.contains(map.getTile(a.getX(),a.getY())))
                    return false;
            }
        }
        return true;
    }

    public void makeAgentsLearn(){
        for(Agent a : agents) {
            if(a.getClass().getSuperclass() == LearnerAgent.class)
                ((LearnerAgent) a).learn();
        }
    }

    public void makeAgentsMoveSmartly(){
        while(isNotInTerminalState()){
            for(Agent a : agents){
                if(a.getClass().getSuperclass()==LearnerAgent.class) {
                    ((LearnerAgent) a).moveSmartly();
                }
            }
            GameController.print();
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

    public void addTeleporters(Teleporter[] teles){
        for(Teleporter t : teles){
            teleporters.add(t);
            map.getTile(t.position[0], t.position[1]).setTeleport();
            map.getTile(t.destination[0], t.destination[1]).setAsTeleportDestination();
        }
    }

    public static void print(){

        String
                ANSI_RESET = "\u001B[0m",
                ANSI_YELLOW = "\u001B[33m",
                ANSI_RED = "\u001B[31m",
                ANSI_PURPLE = "\u001B[35m",
                ANSI_BLUE = "\u001B[34m",
                ANSI_CYAN = "\u001B[36m",
                ANSI_GREEN = "\u001B[32m";


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
                    System.out.print(ANSI_GREEN + " L " + ANSI_RESET);
                else if(tile.hasTeleport())
                    System.out.print(ANSI_CYAN + " T " + ANSI_RESET);
                else if(tile.isATeleportDestination())
                    System.out.print(ANSI_CYAN + " Z " + ANSI_RESET);
                else
                    System.out.print(" O ");
            }
            System.out.println();
        }
        System.out.println();
    }

}

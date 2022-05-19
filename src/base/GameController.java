package base;

import java.util.ArrayList;

import Controller.Map;
import Controller.FileParser;
import Controller.Teleport;
import Controller.Tile;
import Controller.Variables;

public class GameController {

    public static Variables variables;
    public static Map map;
    public static final ArrayList<Agent> agents = new ArrayList<>();
    public static final ArrayList<Tile> GOAL_TILE = new ArrayList<>();
    public static final ArrayList<Teleporter> teleporters = new ArrayList<>();

    private static final ArrayList<ArrayList<int[]>> visionOfAgents = new ArrayList<>();

    public GameController(){
    }

    public void startGame(){
        String p = "recources/testmap2.txt";
        variables = FileParser.parser(p);
        map = new Map();
        addGuards();
        addIntruder();
        addGoal();
        addWalls();
    }

    public static boolean isNotInTerminalState(){
        for(Agent a : GameController.agents){
            if(a.getClass() == Intruder.class){
                if(GameController.GOAL_TILE.contains(map.getTile(a.getX(),a.getY())))
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
                else if(a.getClass() == Guard.class){
                    ((Guard) a).makeMove();
                }
            }
            GameController.print();
        }
    }

    public void runRaycast(){
        for (Agent a:agents) {
            a.rayEngine.calculate(a);
            ArrayList<int[]> visionT = a.rayEngine.getVisibleTiles(a);
            visionOfAgents.add(visionT);
        }

    }

    public void addTeleport(){
        ArrayList<Teleport> portals = variables.getPortals();
        ArrayList<int[]> portalPos = new ArrayList<>();
        for(Teleport t : portals){
            portalPos.addAll(t.getPointsIn());
        }
    }

    public void addGoal(){
        ArrayList<int[]> goalTiles = variables.getGoalPoints();

        for(int[] c: goalTiles){
            Tile goalTile = map.getTile(c[0], c[1]);
            goalTile.setGoal();
            GOAL_TILE.add(goalTile);
        }
    }

    public void addWalls(int[][] wallPos){
        for(int[] wall : wallPos){
            Tile wallTile = map.getTile(wall[0], wall[1]);
            wallTile.placeWall();
        }
    }

    public void addIntruder(){
        ArrayList<int[]> spawn = variables.getIntruderSpawnPoints();
        int nrOfIntruders = variables.getNumberOfIntruders();
        for(int i = 0;i<nrOfIntruders;i++){
            agents.add(new Intruder(spawn.get(i)));
        }
    }

    public void addGuards(){
        ArrayList<int[]> spawn = variables.getGuardSpawnPoints();
        System.out.println("guard size= "+variables.getGuardSpawnPoints().size());
        int nrOfIntruders = variables.getNumberOfGuards();
        for(int i = 0;i<nrOfIntruders;i++){
            agents.add(new Guard(spawn.get(i)));
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


        for(Tile[] row : map.getTiles()){
            for(Tile tile2 : row){
                boolean hasIntruder = false, hasGuard = false;
                for(Agent agent  : agents){
                    if(agent.getX() == tile2.getPosition()[0]
                            && agent.getY() == tile2.getPosition()[1]) {
                        if(agent.getClass() == Guard.class)
                            hasGuard = true;
                        else if (agent.getClass() == Intruder.class)
                            hasIntruder = true;
                    }
                }
                if((hasGuard || hasIntruder) && tile2.isGoal()) {
                    if(hasGuard)
                        System.out.print(ANSI_BLUE + " Y " + ANSI_RESET);
                    else System.out.print(ANSI_PURPLE + " Y " + ANSI_RESET);
                }
                else if(hasGuard || hasIntruder) {
                    if(hasGuard)
                        System.out.print(ANSI_BLUE + " A " + ANSI_RESET);
                    else System.out.print(ANSI_RED + " A " + ANSI_RESET);
                }
                else if (tile2.isGoal())
                    System.out.print(ANSI_YELLOW + " G " + ANSI_RESET);
                else if(tile2.hasWall())
                    System.out.print(ANSI_GREEN + " L " + ANSI_RESET);
                else if(tile2.hasTeleportIn())
                    System.out.print(ANSI_CYAN + " T " + ANSI_RESET);
                else if(tile2.hasTeleportOut())
                    System.out.print(ANSI_CYAN + " Z " + ANSI_RESET);
                else {
                    boolean isSeen = false;
                    for(Agent a : agents){
                        for(int[] tilePos : a.visionT){
                            if(tilePos[0] == tile2.getPosition()[0]
                                    && tilePos[1] == tile2.getPosition()[1])
                                isSeen = true;
                        }
                    }
                    if(isSeen) {
                        System.out.print(ANSI_PURPLE + " O " + ANSI_RESET);
                    }
                    else System.out.print(" O ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}

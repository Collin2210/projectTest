package base;

import Controller.FileParser;
import Controller.Map;
import Controller.Tile;
import Controller.Variables;

import java.util.ArrayList;
import java.util.Arrays;

public class GameController {

    public static Map map;
    public static Variables variables;
    public static final ArrayList<Agent> agents = new ArrayList<>();
    public static final ArrayList<Tile> goalTiles = new ArrayList<>();
    public static final ArrayList<Teleport> teleporters = new ArrayList<>();
    public static final ArrayList<int[]> agentPosition = new ArrayList<>();
    private static final ArrayList<ArrayList<int[]>> visionOfAgents = new ArrayList<>();

    public GameController(){
    }

    public void startGame(){
        String p = "recources/testmap2.txt";
        variables = FileParser.parser(p);
        map = new Map();
        addGoal();
        addWalls();
        addIntruder();
        addGuards();
        runRaycast();
    }

    public static boolean isInTerminalState(){
        for(Agent a : agents) {
            if (a.getClass() == Intruder.class) {
                Tile t = map.getTile(a.getX(), a.getY());
                if(GameController.goalTiles.contains(t))
                    return true;
            }
        }
        return false;
    }

    public void makeAgentsLearn(){
        LearnerAgent a = (LearnerAgent) agents.get(0);
        a.learn();
        visionOfAgents.clear();

    }

    public void makeAgentsMoveSmartly(){
        LearnerAgent a = (LearnerAgent) agents.get(0);
        a.moveSmartly();
    }


    public void runRaycast() {
        for (Agent a : agents) {
            a.rayEngine.calculate(a);
            a.visionT = a.rayEngine.getVisibleTiles(a);
        }
    }


    public void addTeleport(){
        ArrayList<Controller.Teleport> portals = variables.getPortals();
        ArrayList<int[]> portalPos = new ArrayList<>();
        for(Controller.Teleport t : portals){
            portalPos.addAll(t.getPointsIn());
        }
    }

    public void addGoal(){
        ArrayList<int[]> goalTilesFromVariables = variables.getGoalPoints();

        for(int[] c: goalTilesFromVariables){
            Tile goalTile = map.getTile(c[0], c[1]);
            goalTile.setGoal();
            goalTiles.add(goalTile);
        }
    }

    public void addWalls(){
        ArrayList<int[]> walls = map.getWallpoints();
        for(int[] wall : walls){
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

//    public void addTeleporters(Teleport[] teles){
//        for(Teleport t : teles){
//            teleporters.add(t);
//            map.getTile(t.position[0], t.position[1]).setTeleport();
//            map.getTile(t.destination[0], t.destination[1]).setAsTeleportDestination();
//        }
//    }

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
                else if(tile.hasWall())
                    System.out.print(ANSI_GREEN + " L " + ANSI_RESET);
                else if(tile.hasTeleportIn())
                    System.out.print(ANSI_CYAN + " T " + ANSI_RESET);
                else if(tile.hasWall())
                    System.out.print(ANSI_CYAN + " Z " + ANSI_RESET);

                if(tile.hasTrace()) {
                    if (tile.getTrace().getStress() == 0) {
                        System.out.print(ANSI_GREEN + " . " + ANSI_RESET);
                    }
                    if (tile.getTrace().getStress() == 1) {
                        System.out.print(ANSI_YELLOW + " . " + ANSI_RESET);
                    }
                    if (tile.getTrace().getStress() > 1) {
                        System.out.print(ANSI_RED + " . " + ANSI_RESET);
                    }
                }

                else {
                    boolean isSeen = false;
                    for(Agent a : agents){
                        for(int[] tilePos : a.visionT){
                            if(tilePos[0] == tile.getPosition()[0]
                                    && tilePos[1] == tile.getPosition()[1])
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

package base;

import Controller.*;

import java.util.ArrayList;
import java.util.Collections;

public class GameController {

    public static Map map;
    public static Variables variables;
    public static final ArrayList<Agent> agents = new ArrayList<>();
    public static final ArrayList<Tile> goalTiles = new ArrayList<>();

    public static final ArrayList<Teleport> portals = new ArrayList<>();

    public static final ArrayList<ArrayList<int[]>> pathOfAllAgents = new ArrayList<>();

    public static int numOfGuardWins = 0;
    public static int numOfIntruderWins = 0;

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
        addTeleports();
        runRaycast();

    }

    public void makeAgentsLearn(){
        LearnerAgent a = (LearnerAgent) agents.get(0);
        a.learn();
    }

    public void makeAgentsMoveSmartly(){
        LearnerAgent a = (LearnerAgent) agents.get(0);
        a.moveSmartly();
    }

    public void moveAgentDumbly(){
        for(int i = 0; i < 15; i++){
            Agent a = agents.get(0);
            a.setPosition(a.getX()+1,a.getY());
            a.updateTrace();
            GameController.print();
        }
    }

    public void runRaycast() {
        for (Agent a : agents) {
            a.rayEngine.calculate(a);
            a.visionT = a.rayEngine.getVisibleTiles(a);
        }
    }

    public void addTeleports(){
        portals.addAll(variables.getPortals());
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
            pathOfAllAgents.add(new ArrayList<>());
        }
    }

    public void addGuards(){
        ArrayList<int[]> spawn = variables.getGuardSpawnPoints();
        int nrOfIntruders = variables.getNumberOfGuards();
        for(int i = 0;i<nrOfIntruders;i++){
            agents.add(new Guard(spawn.get(i)));
            pathOfAllAgents.add(new ArrayList<>());
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

                else if(tile.hasTrace()) {
                    System.out.print(ANSI_PURPLE + " T " + ANSI_RESET);

                    /*if (tile.getTrace().getStress() == 0) {
                        System.out.print(ANSI_GREEN + " . " + ANSI_RESET);
                    }
                    if (tile.getTrace().getStress() == 1) {
                        System.out.print(ANSI_YELLOW + " . " + ANSI_RESET);
                    }
                    if (tile.getTrace().getStress() > 1) {
                        System.out.print(ANSI_RED + " . " + ANSI_RESET);
                    }*/
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
                        System.out.print(ANSI_GREEN + " V " + ANSI_RESET);
                    }
                    else System.out.print(" O ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}

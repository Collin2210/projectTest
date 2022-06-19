package base;

import Controller.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameController {

    public static Map map;
    public static Variables variables;
    public static ArrayList<Agent> agents;
    public static ArrayList<Agent> intrudersCaught;

    public static ArrayList<Tile> goalTiles;
    public static ArrayList<Yell> yells;

    public static ArrayList<int[]> portalEntrances;
    public static ArrayList<int[]> portalDestinations;
    public static ArrayList<Double> portalDegrees;

    public static ArrayList<ArrayList<int[]>> pathOfAllGuards;
    public static ArrayList<ArrayList<int[]>> pathOfAllIntruders;

    public static ArrayList<int[]> intruderSpawnPoints;
    public static ArrayList<int[]> guardSpawnPoints;

    public static ArrayList<Guard> Guards;
    public static ArrayList<Intruder> Intruders;

    public static int numOfGuardWins;
    public static int numOfIntruderWins;

    public static final double TOWER_VISION_BONUS = 10;

    public GameController() {
        agents = new ArrayList<>();
        intrudersCaught = new ArrayList<>();

        goalTiles = new ArrayList<>();
        yells = new ArrayList<>();;

        portalEntrances = new ArrayList<>();
        portalDestinations = new ArrayList<>();
        portalDegrees = new ArrayList<>();

        pathOfAllGuards = new ArrayList<>();
        pathOfAllIntruders = new ArrayList<>();
        intruderSpawnPoints = new ArrayList<>();
        guardSpawnPoints = new ArrayList<>();

        Guards = new ArrayList<>();
        Intruders = new ArrayList<>();

        numOfGuardWins = 0;
        numOfIntruderWins = 0;
    }

    public void startGame() {
        String p = "recources/testmap.txt";
        variables = FileParser.parser(p);
        map = new Map();
        addGoal();
        addWalls();
        addIntruder();
        //addGuards();
        addTeleports();
        runRaycast();
        print();
    }

    public void makeAgentsLearn() {
        LearnerAgent a = (LearnerAgent) agents.get(0);
        a.learn();
    }

    public void makeAgentsMoveSmartly() {
        LearnerAgent a = (LearnerAgent) agents.get(0);
        a.moveSmartly();
    }

    public void moveAgentDumbly() {
        for (int i = 0; i < 15; i++) {
            Agent a = agents.get(0);
            a.setPosition(a.getX() + 1, a.getY());
            GameController.print();
        }
    }

    public void runRaycast() {
        for (Agent a : agents) {
            a.rayEngine.calculate(a);
            a.visionT = a.rayEngine.getVisibleTiles(a);
        }
    }

    public void addTeleports() {
        ArrayList<Controller.Teleport> portals = variables.getPortals();
        for (Controller.Teleport t : portals) {
            portalEntrances.addAll(t.getPointsIn());
            Collections.addAll(portalDestinations, t.getPointOut());
            portalDegrees.add(t.getDegreeOut());
        }
    }

    public void addShadedAreas() {
        ArrayList<int[]> shadedTiles = variables.getShadePoints();

        for (int[] tile : shadedTiles) {
            map.getTile(tile[0], tile[1]).setShade();
        }
    }

    public void addGoal() {
        ArrayList<int[]> goalTilesFromVariables = variables.getGoalPoints();
        for (int[] c : goalTilesFromVariables) {
            Tile goalTile = map.getTile(c[0], c[1]);
            goalTile.setGoal();
            goalTiles.add(goalTile);
        }
    }

    public void addWalls() {
        ArrayList<int[]> walls = map.getWallpoints();
        for (int[] wall : walls) {
            Tile wallTile = map.getTile(wall[0], wall[1]);
            wallTile.placeWall();
        }
    }

    public void addIntruder() {
        ArrayList<int[]> spawn = variables.getIntruderSpawnPoints();
        int nrOfIntruders = variables.getNumberOfIntruders();
        for (int i = 0; i < nrOfIntruders; i++) {
            Intruder in = new Intruder(spawn.get(i));
            intruderSpawnPoints.add(spawn.get(i));
            agents.add(in);
            Intruders.add(in);
            pathOfAllIntruders.add(new ArrayList<>());
//            pathOfAllIntruders.get(i).add(spawn.get(i));
        }
    }

    public void addGuards() {
        ArrayList<int[]> spawn = variables.getGuardSpawnPoints();
        int nrOfIntruders = variables.getNumberOfGuards();
        // guard's exploration areas
        ArrayList<double[]> areas = GuardAlgo.getAreasForGuards();

        for (int i = 0; i < nrOfIntruders; i++) {
            Guard g = new Guard(spawn.get(i));
            g.setExplorationArea(areas.get(i));
            agents.add(g);
            Guards.add(g);
            pathOfAllGuards.add(new ArrayList<>());
            guardSpawnPoints.add(spawn.get(i));
//            pathOfAllGuards.get(i).add(spawn.get(i));
        }
    }

    public ArrayList<int[]> getTraceTiles(){
        ArrayList<int[]> traceTiles = new ArrayList<>();

        for(Agent a : agents){
            traceTiles.addAll(a.getTrace().getTraceTiles());
        }

        return traceTiles;
    }

    public static void print() {

        String
                ANSI_RESET = "\u001B[0m",
                ANSI_YELLOW = "\u001B[33m",
                ANSI_RED = "\u001B[31m",
                ANSI_PURPLE = "\u001B[35m",
                ANSI_BLUE = "\u001B[34m",
                ANSI_CYAN = "\u001B[36m",
                ANSI_GREEN = "\u001B[32m";


        for (Tile[] row : map.getTiles()) {
            for (Tile tile : row) {
                boolean hasIntruder = false, hasGuard = false;
                for (Agent agent : agents) {
                    if (agent.getX() == tile.getPosition()[0]
                            && agent.getY() == tile.getPosition()[1]) {
                        if (agent.getClass() == Guard.class)
                            hasGuard = true;
                        else if (agent.getClass() == Intruder.class)
                            hasIntruder = true;
                    }
                }
                if (hasGuard || hasIntruder) {
                    if (hasGuard)
                            System.out.print(ANSI_BLUE + " A " + ANSI_RESET); // guard
                    else System.out.print(ANSI_RED + " A " + ANSI_RESET);
                } else if ((hasGuard || hasIntruder) && tile.isGoal()) {
                    if (hasGuard)
                        System.out.print(ANSI_BLUE + " Y " + ANSI_RESET);
                    else System.out.print(ANSI_PURPLE + " Y " + ANSI_RESET);
                } else if (tile.hasTrace()) {
                    System.out.print(ANSI_RED + " T " + ANSI_RESET);
                } else if (tile.isGoal())
                    System.out.print(ANSI_YELLOW + " G " + ANSI_RESET);
                else if (tile.hasWall())
                    System.out.print(ANSI_GREEN + " L " + ANSI_RESET);
                else if (tile.hasTeleportIn())
                    System.out.print(ANSI_CYAN + " T " + ANSI_RESET);
                else if (tile.hasTeleportOut())
                    System.out.print(ANSI_CYAN + " % " + ANSI_RESET);
                else if (tile.hasWall())
                    System.out.print(ANSI_CYAN + " Z " + ANSI_RESET);
                else {
                    boolean isSeen = false;
                    for (Agent a : agents) {
                        for (int[] tilePos : a.visionT) {
                            if (tilePos[0] == tile.getPosition()[0]
                                    && tilePos[1] == tile.getPosition()[1])
                                isSeen = true;
                        }
                    }
                    if (isSeen) {
                        System.out.print(ANSI_PURPLE + " O " + ANSI_RESET);
                    } else System.out.print(" O ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printRect(double[] rect) {
        double
                height = rect[2], width = rect[3];
        double[]
                og = {rect[0], rect[1]},
                point1 = {rect[0], rect[1] + height},
                point2 = {rect[0] + width, rect[1] + height},
                point3 = {rect[0] + width, rect[1]};

        double[][] points = {og, point1, point2, point3};

        StringBuilder s = new StringBuilder("polygon(");
        for (double[] p : points)
            s.append("(").append(p[0]).append(",").append(p[1]).append("),");
        s.append(")");

        System.out.println(s);
    }

}

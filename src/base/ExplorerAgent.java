package base;

import Controller.Map;
import QLearning.QLearning;

import java.util.ArrayList;
import java.util.Arrays;

import static QLearning.QLearning.*;
import static QLearning.RewardTable.distanceBetweenPoints;
import static base.GameController.*;

public class ExplorerAgent extends Agent{

    public static final byte
            HEIGHT_INDEX = 2,
            WIDTH_INDEX = 3;

    private double[] explorationArea; // {x, y, height, width}
    private final ArrayList<int[]> exploredTiles;

    public ExplorerAgent(int[] position) {
        super(position);
        exploredTiles = new ArrayList<>();
        exploredTiles.add(new int[]{position[0], position[1]});
    }

    public void makeExplorationMove(){

        int[] nextPosition = this.getPosition();

        if(!isInHisArea())
            nextPosition = getTileClosestToArea();
        else {
            boolean appropriateNeighbourIsFound = false;
            for(int[] neighbour : getAllNeighbours()){
                // get first one that is In Bounds, Not A Wall and has not been explored
                if(isInHisArea(neighbour) && !isAlreadyExplored(neighbour) && !map.hasWall(neighbour)) {
                    nextPosition = neighbour;
                    appropriateNeighbourIsFound = true;
                    break;
                }
            }
            if(!appropriateNeighbourIsFound) {
                nextPosition = exploredTiles.get(exploredTiles.size() - 2);
                exploredTiles.clear();
            }
        }

        // apply move
        this.setPosition(nextPosition[0], nextPosition[1]);
        exploredTiles.add(new int[]{nextPosition[0], nextPosition[1]});

    }

    private int[] getTileClosestToArea(){
        int[][] neighbours = getAllNeighbours();

        double smallestDistanceToRegion = Double.MAX_VALUE;
        int[] closestNeighbourToRegion = neighbours[0];

        for(int[] n : neighbours){
            int
                    middleX = (int) (explorationArea[0] + explorationArea[WIDTH_INDEX]/2),
                    middleY = (int) (explorationArea[1] + explorationArea[HEIGHT_INDEX]/2);

            double distance = distanceBetweenPoints(n[0], n[1], middleX, middleY);
            if(distance < smallestDistanceToRegion){
                smallestDistanceToRegion = distance;
                closestNeighbourToRegion = n;
            }
        }

        return closestNeighbourToRegion;
    }

    private int[][] getAllNeighbours(){
        int
                x = getX(),
                y = getY();
        int[]
                right = {x+1, y},
                left = {x-1, y},
                up = {x, y+1},
                down = {x, y-1};

        return new int[][]{right, left, up, down};
    }

    private boolean isInHisArea(){
        int
                x = getX(), y = getY(),
                startX = (int) explorationArea[0], endX = startX + (int) explorationArea[WIDTH_INDEX],
                startY = (int) explorationArea[1], endY = startY + (int) explorationArea[HEIGHT_INDEX];

        return startX <= x && x < endX
                && startY <= y && y < endY;
    }

    public void setExplorationArea(double[] explorationArea) {
        this.explorationArea = explorationArea;
    }

    public boolean isInHisArea(int[] position){
        int
                x = position[0], y = position[1],
                startX = (int) explorationArea[0], endX = startX + (int) explorationArea[WIDTH_INDEX],
                startY = (int) explorationArea[1], endY = startY + (int) explorationArea[HEIGHT_INDEX];

        return startX <= x && x < endX
                && startY <= y && y < endY;
    }

    public boolean isAlreadyExplored(int[] position){
        for(int[] t : exploredTiles){
            if(t[0] == position[0] && t[1] == position[1])
                return true;
        }
        return false;
    }


    public void tryPerformingAction(byte action){
        try {
            performAction(action);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void performAction(byte action) throws Exception {
        int[] currentState = this.getPosition();
        this.setActionPerformed(action);

        int[] newPosition = getValidPositionFromAction(action);

        // check if action takes you to a teleport
        for(int[] portalIn : portalEntrances){
            if(portalIn[0] == newPosition[0] && portalIn[1] == newPosition[1]){
                int index = portalEntrances.indexOf(portalIn);
                newPosition = portalDestinations.get(index);
                this.setAngleDeg(portalDegrees.get(index));
            }
        }
        int[] newState = new int[]{newPosition[0], newPosition[1]};
        setPreviousState(new int[]{currentState[0],currentState[1]});
        setPosition(newState[0], newState[1]);
        getSavedPath().add(new int[]{newPosition[0], newPosition[1]});
        visionT.clear();
        getRayEngine().calculate(this);
        this.visionT = this.getRayEngine().getVisibleTiles(this);
        this.updateTrace();
        this.AgentStep();
    }

    private boolean newPositionIsValid(int newX, int newY) {
        return Map.inMap(newX, newY);
    }

    int[] getValidPositionFromAction(byte action) throws Exception {
        int[] currentState = this.getPosition();
        int[] newPositionData = getNewPositionFromAction(action, currentState);

        if(!newPositionIsValid(newPositionData[0], newPositionData[1])){
            if(action == NUMBER_OF_POSSIBLE_ACTIONS-1){
                return getValidPositionFromAction((byte) 0);
            }
            else {
                action = getRandomAction();
                return getValidPositionFromAction(action);
            }
        }
        // set angle
        this.setAngleDeg(newPositionData[2]);
        return new int[]{newPositionData[0], newPositionData[1], (int) this.getAngleDeg()};
    }

    public static int[] getNewPositionFromAction(byte action, int[] currentState) throws Exception {
        int angle;
        int newX = currentState[0], newY = currentState[1];
        if(action == MOVE_UP) {
            newX -= 1;
            angle = 180;
        }
        else if (action == MOVE_RIGHT){
            newY += 1;
            angle = 90;
        }
        else if (action == MOVE_DOWN){
            newX += 1;
            angle = 0;
        }
        else if (action == MOVE_LEFT) {
            newY -= 1;
            angle = 270;
        }
        else {
            throw new Exception("action number not recognized");
        }
        return new int[]{newX, newY, angle};
    }
}

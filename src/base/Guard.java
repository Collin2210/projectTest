package base;

import Controller.Tile;
import Path.Move;
import Path.Position;
import QLearning.RewardTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static base.GameController.*;

public class Guard extends ExplorerAgent {

    private Intruder intruderToCatch;
    private Tile tileWithTraceToChase;

    public boolean
            chasingIntruder,
            seesTrace,
            yelled;

    private byte timer; // time guard spent chasing without seeing the intruder
    private static final byte TIME_LIMIT = 2; // maximum time guard will spend chasing without seeing the intruder before scattering

    public Guard(int[] position) {
        super(position);
        resetToScatterMode();
        timer = 0;
    }

    public void makeMove(){

        checkVision();

        // check if guard has been chasing intruder for long enough without seeing him to return to scatter
        if(timer == TIME_LIMIT){
            resetToScatterMode();
        }

        // check if reached yell
        if(getAudioObject() != null && getX() == getAudioObject().getPosition()[0] && getY() == getAudioObject().getPosition()[1])
            setHeardAudio(null);

        // guard gives priority to seen Intruders, then Trace and finally yells
        if(chasingIntruder)
            followIntruder();

        else if (seesTrace)
            followTrace();

        else if (hearsYell())
            followAudio();

        else
            makeExplorationMove();
    }

    /**
     * this method checks the agents' vision range and asserts if it sees an intruder or a trace
     */
    private void checkVision(){
        byte numberOfIntrudersSeen = 0;
        ArrayList<Intruder> intrudersSeen = new ArrayList<>();
        ArrayList<Tile> tilesWithTracesSeen = new ArrayList<>();

        // initialize booleans
        seesTrace = false;
        chasingIntruder = false;

        // update vision
        this.visionT = this.getRayEngine().getVisibleTiles(this);

        // for every tile in vision range
        for(int[] tilePos : visionT){

            // check for intruders
            for(Agent a : agents){
                if(a.getClass() == Intruder.class){

                    // if intruder is on a tile in vision
                    if(a.getX() == tilePos[0] && a.getY() == tilePos[1]) {
                        intrudersSeen.add((Intruder) a);
                        chasingIntruder = true;
                        numberOfIntrudersSeen++;
                        // if he hasn't yelled for this intruder yet, yell
                        if(!yelled){
                            doYell();
                            yelled = true;
                        }
                    }
                }
            }

            // check for trace: react only to intruder's trace or stressed guards
            Tile tile = map.getTile(tilePos);
            if(tile.hasTrace()){
                Trace trace = tile.getTrace();
                boolean
                        traceOwnerIsIntruder = trace.getOwner().getClass() == Intruder.class,
                        traceOwnerIsStressed = trace.getStressLevel() > 0,
                        isNotOwnTrace = trace.getOwner() != this;

                if((traceOwnerIsIntruder || traceOwnerIsStressed) && isNotOwnTrace){
                    seesTrace = true;
                    tilesWithTracesSeen.add(tile);
                }
            }
        }

        // react to intruder seen
        if(chasingIntruder){
            // if he still sees intruder
            if(intrudersSeen.size() != 0){
                // get the closest intruder
                double smallestDistance = Double.MAX_VALUE;
                Intruder closestIntruder = null;
                for(Intruder intruder : intrudersSeen){
                    double distance = RewardTable.distanceBetweenPoints(getX(), getY(), intruder.getX(), intruder.getY());
                    if(distance < smallestDistance){
                        smallestDistance = distance;
                        closestIntruder = intruder;
                    }
                }
                // chase the closest intruder
                intruderToCatch = closestIntruder;
            }
        }

        // react to trace seen
        if(seesTrace){
            // get the furthest tile with a trace
            double highestDistance = Double.MIN_VALUE;
            Tile furthestTile = null;
            for(Tile t : tilesWithTracesSeen){
                double distance = RewardTable.distanceBetweenPoints(getX(),getY(),t.getXCoord(),t.getXCoord());
                if(distance > highestDistance){
                    highestDistance = distance;
                    furthestTile = t;
                }
            }

            // follow the furthest tile
            tileWithTraceToChase = furthestTile;
        }

        // update chasing timer (the timer checks if guard has been chasing an agent it does not see anymore for long enough)
        if(!chasingIntruder && !isScatterMode())
            timer++;

        // update stress level based on the number of intruders seen
        updateStressLevel(numberOfIntrudersSeen);
    }

    private boolean hasCaught(Intruder intruder){
        return manhattanDist(getPosition(), intruder.getPosition()) <= 2;
    }

    private void followWithEucli(int[] position){

        double smallest_distance = Double.MAX_VALUE;
        int[] closest_neighbour = {,};

        // get closest neighbour
        for(int[] n : getAllNeighbours()){
            double distance = RewardTable.distanceBetweenPoints(n[0], n[1], position[0], position[1]);
            if(distance <= smallest_distance){
                smallest_distance = distance;
                closest_neighbour = n;
            }
        }

        applyNextMove(closest_neighbour);
    }

    /**
     * this method will generate path to a position using A*, then make 1 step towards said position
     * @param position : can be position of intruder, trace, or source of yell
     */
    private void followWithAStart(int[] position){
        // get path to position
        List<Position> pathToPosition = Move.getPath(getPosition(), position);

        if(pathToPosition == null){
            System.out.println("from " + Arrays.toString(getPosition()) + " to " + Arrays.toString(position));
            GameController.print();
        }

        // get next step towards position
        Position nextPos = pathToPosition.get(1);
        int[] nextPosition = {nextPos.getX(), nextPos.getY()};

        // do all the stuff when next position is found: check teleport, update angle, update exploredTiles, update vision
        applyNextMove(nextPosition);
    }

    private void followIntruder() {

        // check if guard has caught intruder
        if (this.hasCaught(this.intruderToCatch)){
            intruderToCatch.getsCaught();
            this.intruderToCatch = null;
            resetToScatterMode();
            return;
        }

        // get position of intruder and head there
        followWithEucli(intruderToCatch.getPosition());
    }

    private void followTrace(){
        // check if reached trace

        int[] tracePosition;

        try {
            tracePosition = tileWithTraceToChase.getPosition();
        } catch (Exception e) {
            GameController.print();
            throw new RuntimeException(e);
        }

        if(Arrays.equals(getPosition(), tracePosition)){
            seesTrace = false;
            chasingIntruder = false;
            intruderToCatch = null;
            tileWithTraceToChase = null;
            timer = 0;
            makeExplorationMove();
            return;
        }

        // get position of trace tile and head there
        followWithEucli(tileWithTraceToChase.getPosition());
    }

    public Intruder getIntruderToCatch() {
        return intruderToCatch;
    }

    public void doYell(){
        Yell yellObject = new Yell(this);
        yellObject.spreadAudio();
        //this.audioObject = yellObject;
        GameController.yells.add(yellObject);
    }

    public void followAudio()
    {
        int[] audioPosition = getAudioObject().getPosition();

        // check if it has reached audio
        if(Arrays.equals(getPosition(), audioPosition)){
            resetToScatterMode();
            makeExplorationMove();
            return;
        }

        // get path to intruder with a star
        List<Position> pathToIntruder = Move.getPath(getPosition(), audioPosition);

        // check if guard got to yell
        if(pathToIntruder.size() == 1){
            removeYell();
            resetToScatterMode();
        }

        // get next position towards intruder
        Position nextPos = pathToIntruder.get(1);
        int[] nextPosition = {nextPos.getX(), nextPos.getY()};

        // do all the stuff when next position is found:
        //              check teleport, update angle, update exploredTiles, update vision
        applyNextMove(nextPosition);
    }

    public void resetParam(){
        resetToScatterMode();
        clearExploredTiles();
    }

    public boolean isScatterMode(){
        return !seesTrace && !hearsYell() && !chasingIntruder;
    }

    public boolean isChasingIntruder(){
        return this.chasingIntruder;
    }

    public void resetToScatterMode(){
        seesTrace = false;
        chasingIntruder = false;
        intruderToCatch = null;
        tileWithTraceToChase = null;
        removeYell();
        yelled = false;
        timer = 0;
    }

    public static double manhattanDist(int[] a, int[] b){
        int xA = a[0], yA = a[1];
        int xB = b[0], yB = b[1];

        return Math.abs(xB - xA) + Math.abs(yB -yA);
    }
}

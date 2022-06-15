package base;

import Controller.Tile;
import Path.Move;
import Path.Position;
import QLearning.RewardTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static base.GameController.agents;
import static base.GameController.map;

public class Guard extends ExplorerAgent {

    private Intruder intruderToCatch;
    private Tile tileWithTraceToChase;

    private boolean
            chasingIntruder,
            seesTrace;

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

        // update vision
        this.visionT = this.getRayEngine().getVisibleTiles(this);

        // for every tile in vision range
        for(int[] tilePos : visionT){

            // check for intruders
            for(Agent a : agents){
                if(a.getClass() == Intruder.class){
                    if(a.getX() == tilePos[0] && a.getY() == tilePos[1]) {
                        intrudersSeen.add((Intruder) a);
                        chasingIntruder = true;
                        numberOfIntrudersSeen++;
                    }
                }
            }

            // check for trace: react only to intruder's trace or stressed guards
            Tile tile = map.getTile(tilePos);
            if(tile.hasTrace()){
                Trace trace = tile.getTrace();
                boolean
                        traceOwnerIsIntruder = trace.getOwner().getClass() == Intruder.class,
                        traceOwnerIsStressed = trace.getStressLevel() > 0;

                if(traceOwnerIsIntruder || traceOwnerIsStressed) {
                    seesTrace = true;
                    tilesWithTracesSeen.add(tile);
                }
            }
        }

        // react to intruder seen
        if(chasingIntruder){
            // get the closest intruder
            double smallestDistance = Double.MAX_VALUE;
            Intruder closestIntruder = null;
            for(Intruder intruder : intrudersSeen){
                double distance = RewardTable.distanceBetweenPoints(getX(), getY(), closestIntruder.getX(), intruder.getY());
                if(distance < smallestDistance){
                    smallestDistance = distance;
                    closestIntruder = intruder;
                }
            }

            // chase the closest intruder
            intruderToCatch = closestIntruder;
            this.doYell();
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
        // We have to make sure to remove the intruder from the map after it is caught.
        return Arrays.equals(this.getPosition(), intruder.getPosition());
    }

    /**
     * this method will generate path to a position using A*, then make 1 step towards said position
     * @param position : can be position of intruder, trace, or source of yell
     */
    private void follow(int[] position){
        // get path to position
        List<Position> pathToPosition = Move.getPath(getPosition(), position);

        // get next step towards position
        Position nextPos = pathToPosition.get(1);
        int[] nextPosition = {nextPos.getX(), nextPos.getY()};

        // do all the stuff when next position is found: check teleport, update angle, update exploredTiles, update vision
        applyNextMove(nextPosition);
    }

    private void followIntruder() {
        // get position of intruder and head there
        follow(intruderToCatch.getPosition());

        // check if guard has caught intruder
        if (this.hasCaught(this.intruderToCatch)){
            agents.remove(intruderToCatch);
            this.intruderToCatch = null;
            resetToScatterMode();
        }
    }

    private void followTrace(){
        // get position of trace tile and head there
        follow(tileWithTraceToChase.getPosition());
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
        timer = 0;
    }
}

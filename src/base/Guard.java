package base;

import Path.Move;
import Path.Position;
import QLearning.*;

import java.util.Arrays;
import java.util.List;

import static base.GameController.*;

public class Guard extends ExplorerAgent {

    private Intruder intruderToCatch;
    private Yell yell;
    private boolean yelling;
    private boolean isScatterMode;
    private byte timer; // time guard spent chasing without seeing the intruder
    private static final byte TIME_LIMIT = 2; // maximum time guard will spend chasing without seeing the intruder before scattering

    public Guard(int[] position) {
        super(position);
        yelling = false;
        this.yell = new Yell(this);
        isScatterMode = true;
        timer = 0;
    }

    public void makeMove(){

        yell.remove();

        // check if intruder is in vision range
        checkVision();

        // check if guard has been chasing intruder for long enough without seeing him to return to scatter
        if(timer == TIME_LIMIT){
            isScatterMode = true;
            intruderToCatch = null;
            yelling = false;
            timer = 0;
        }

        if(isScatterMode()) {
            makeExplorationMove();
        }
        else {
            followIntruder();
        }
    }

    private void checkVision(){
        boolean intruderIsSeen = false;
        this.visionT = this.getRayEngine().getVisibleTiles(this);
        for(int[] tilePos : visionT){
            for(Agent a : GameController.agents){
                if(a.getClass() == Intruder.class){
                    int ax = a.getX(), ay = a.getY();
                    if(ax == tilePos[0] && ay == tilePos[1]) {
                        intruderToCatch = (Intruder) a;
                        yell.propagateYell();
                        yelling = true;
                        isScatterMode = false;
                        intruderIsSeen = true;
                    }
                }
            }
        }

        if(!intruderIsSeen && !isScatterMode)
            timer++;
    }

    private void followIntruder(){
        // get path to intruder with a star
        int[] intruderPosition = intruderToCatch.getPosition();
        List<Position> pathToIntruder = Move.getPath(getPosition(), intruderPosition);

        // get next position towards intruder
        Position nextPos = pathToIntruder.get(1);
        int[] nextPosition = {nextPos.getX(), nextPos.getY()};

        // do all the stuff when next position is found: check teleport, update angle, update exploredTiles, update vision
        applyNextMove(nextPosition);

    }

    public Intruder getIntruderToCatch() {
        return intruderToCatch;
    }

    public boolean isScatterMode(){
        return isScatterMode;
    }

}

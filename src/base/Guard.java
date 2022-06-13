package base;

import Controller.Map;
import Path.Move;
import Path.Position;
import QLearning.*;

import static base.GameController.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Guard extends ExplorerAgent {

    private Intruder intruderToCatch;
    private boolean isScatterMode;
    private byte timer; // time guard spent chasing without seeing the intruder
    private static final byte TIME_LIMIT = 2; // maximum time guard will spend chasing without seeing the intruder before scattering
    private Yell yell = null;

    private int[] targetLocation;

    public Guard(int[] position) {
        super(position);
        this.yell = new Yell(this);
        isScatterMode = true;
        timer = 0;
    }

    public void makeMove(){
        //remove guard's yell
        //place yell if needed
        checkVision();

        // check if guard has been chasing intruder for long enough without seeing him to return to scatter
        if(timer == TIME_LIMIT){
            isScatterMode = true;
            intruderToCatch = null;
            timer = 0;
        }

        if(!isScatterMode) {
            followIntruder();
        }
        else if (this.yell != null){
            // followYell();
        }
        else {
            makeExplorationMove();
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
                        isScatterMode = false;
                        intruderIsSeen = true;
                    }
                }
            }
        }

        if(!intruderIsSeen && !isScatterMode)
            timer++;
    }

    private boolean hasCaught(Intruder intruder){
        // We have to make sure to remove the intruder from the map after it is caught.
        return Arrays.equals(this.getPosition(), intruder.getPosition());
    }


    private void followIntruder() {
        // get path to intruder with a star
        int[] intruderPosition = intruderToCatch.getPosition();
        List<Position> pathToIntruder = Move.getPath(getPosition(), intruderPosition);

        // get next position towards intruder
        Position nextPos = pathToIntruder.get(1);
        int[] nextPosition = {nextPos.getX(), nextPos.getY()};

        // do all the stuff when next position is found: check teleport, update angle, update exploredTiles, update vision
        applyNextMove(nextPosition);

        if (this.hasCaught(this.intruderToCatch)){
            // remove the agent from the map as well.
            this.intruderToCatch = null;
            this.isScatterMode = true;
        }
    }

    public Intruder getIntruderToCatch() {
        return intruderToCatch;
    }
}

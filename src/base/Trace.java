package base;

import java.util.ArrayList;

public class Trace {

    public static byte
            MAX_LENGTH = 3;

    public static final byte
            NO_STRESS = 0,
            MID_STRESS = 1,
            HIGH_STRESS = 2;

    private final Agent owner;

    private ArrayList<int[]> traceTiles;

    private byte stressLevel;

    private double angle;

    public Trace(Agent owner) {
        this.traceTiles = new ArrayList<>();
        this.owner = owner;
        stressLevel = NO_STRESS;
        angle = owner.getAngleDeg();
    }

    public void addToTrace(int[] tilePos){
        int[] data = {tilePos[0], tilePos[1], (int) owner.getAngleDeg()};

        // add trace to tile
        traceTiles.add(data);
        GameController.map.getTile(tilePos).addTrace(this);

        // remove last trace tile
        if(traceTiles.size() > MAX_LENGTH){
            GameController.map.getTile(traceTiles.get(0)).removeTrace();
            traceTiles.remove(0); // remove first added
        }
    }

    public void resetTrace(){
        for(int[] tile : traceTiles) {
            GameController.map.getTile(tile).removeTrace();
        }
        traceTiles.clear();
        stressLevel = NO_STRESS;
    }

    public ArrayList<int[]> getTraceTiles() {
        return traceTiles;
    }

    public void setTraceTiles(ArrayList<int[]> traceTiles) {
        this.traceTiles = traceTiles;
    }

    public byte getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(byte stressLevel) {
        this.stressLevel = stressLevel;
    }

    public Agent getOwner() {
        return owner;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}

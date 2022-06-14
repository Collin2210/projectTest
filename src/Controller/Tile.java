package Controller;


import base.Trace;

public class Tile {
    private final int[] position;

    private boolean
            hasShade,
            hasWall,
            isTeleportIn,
            isTeleportOut,
            isGoal,
            isTower;

    private int[] portalOut = new int[2];
    private double degreeOut;

    private Trace trace;

    public Tile(int x, int y) {
        this.position = new int[]{x, y};
        hasShade = false;
        hasWall = false;
        isTeleportIn = false;
        isTeleportOut = false;
        isGoal = false;
        isTower = false;
        trace = null;
    }

    public void placeWall(){
        hasWall = true;
    }

    public void placeShade(){
        hasShade = true;
    }

    public void placeTeleportIN(int[] portalOut, double degreeOut){
        isTeleportIn = true;
        this.portalOut = portalOut;
        this.degreeOut = degreeOut;
    }

    public void placeTeleportOUT(double degreeOut){
        isTeleportOut = true;
        this.degreeOut = degreeOut;
    }

    public boolean isAtPosition(int[] position){
        return ( this.position[0] == position[0]
                && this.position[1] == position[1] );
    }

    public void addTrace(Trace trace){
        this.trace = trace;
    }

    public void removeTrace(){
        trace = null;
    }

    public void setGoal(){
        this.isGoal = true;
    }

    public void placeTower(){this.isTower = true;}
    public boolean isGoal(){
        return isGoal;
    }
    public boolean hasTower(){return isTower;}

    public boolean hasWall(){
        return hasWall;
    }

    public boolean hasShade(){
        return hasShade;
    }

    public void setShade(){this.hasShade = true;}

    public  boolean hasTeleportIn(){
        return isTeleportIn;
    }

    public  boolean hasTeleportOut(){
        return isTeleportOut;
    }

    public double getDegreeOut(){return degreeOut;}

    public int[] getPortalOut(){return this.portalOut;}

    public int[] getPosition(){return position;}

    public int getXCoord() {
        return this.position[0];
    }

    public int getYCoord() {
        return this.position[1];
    }
}

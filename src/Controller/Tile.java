package Controller;

import base.Trace;

public class Tile {
    private final int[] position;

    private boolean
            hasShade,
            hasWall,
            isTeleportIn,
            isTeleportOut,
            hasTrace,
            isYell,
            isGoal,
            isTower;

    public Trace AgentTrace;

    private int[] portalOut = new int[2];
    private double degreeOut;

    public Tile(int x, int y) {
        this.position = new int[]{x, y};
        hasShade = false;
        hasWall = false;
        isTeleportIn = false;
        isTeleportOut = false;
        isGoal = false;
        hasTrace = false;
        isTower = false;
    }

    public void placeTrace(Trace trace ){
        hasTrace = true;
        AgentTrace = trace;
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

    public void resetTrace(){
        hasTrace = false;
        AgentTrace = null;
    }

    public Trace getTrace(){return AgentTrace;};

    public void setGoal(){
        this.isGoal = true;
    }

    public void setTrace() {this.hasTrace = true;}

    public void setYell(){
        this.isYell=true;
    }

    public void removeYell() { this.isYell = false;}
    public void placeTower(){this.isTower = true;}
    public boolean isGoal(){
        return isGoal;
    }
    public boolean hasTower(){return isTower;}
    public boolean hasYell(){
        return isYell;
    }

    public boolean hasTrace(){
        return hasTrace;
    }
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

    public void addTrace(Trace trace){
        this.AgentTrace = trace;
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

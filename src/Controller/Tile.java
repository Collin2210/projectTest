package Controller;

import java.util.ArrayList;

public class Tile {
    private final int[] position;

    private boolean
            hasShade,
            hasWall,
            isTeleportIn,
            isTeleportOut,
            isGoal;

    private int[] portalOut = new int[2];
    private double degreeOut;
    private final int size = 10;


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public Tile(int x, int y) {
        this.position = new int[]{x, y};
        hasShade = false;
        hasWall = false;
        isTeleportIn = false;
        isTeleportOut = false;
        isGoal = false;
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

    public void setGoal(){
        this.isGoal = true;
    }

    public boolean isGoal(){
        return isGoal;
    }

    public boolean hasWall(){
        return hasWall;
    }

    public boolean hasShade(){
        return hasShade;
    }

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

    public String toString(){
        String s = "_";

        if(this.hasTeleportIn()){
            s = "T";
        }
        if(this.hasTeleportOut()){
            s = "O";
        }
        if(this.hasWall()) {
            s = "W";
        }
        if(this.hasShade()){
            s = "H";
        }

        return s;
    }
}

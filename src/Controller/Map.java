package Controller;

import base.GameController;

import java.util.ArrayList;
public class Map {

    private final Variables variables;
    private final Tile[][] map;
    private final int xSize;
    private final int ySize;
    private ArrayList<int[]> wallpoints = new ArrayList<>();


    /**
     * Map class contains all methods that operate on map objects
     * */
    public Map() {
        this.variables = GameController.variables;
        this.map = new Tile[variables.getWidth()][variables.getHeight()];
        xSize = variables.getWidth();
        ySize = variables.getHeight();
        tileInit();
        this.wallpoints = wallPoints();
        initializeMap();
    }

    /**
     * METHODS USED TO INITIALIZE THE MAP
     * */

    private void tileInit(){
        for(int i = 0; i<xSize;i++){
            for(int j = 0; j<ySize; j++){
                map[i][j] = new Tile(i,j);
            }
        }
    }

    private void initializeMap(){
        placeWalls();
        placePortals();
        placeShade();
        placeTowers();
    }

    private void placeWalls(){
        for(int[] wallCoord : this.wallpoints){
            map[wallCoord[0]][wallCoord[1]].placeWall();
        }
    }

    private void placeShade(){
        ArrayList<int[]> shadePoints = shadesPoints();
        for(int[] c: shadePoints){
            map[c[0]][c[1]].placeShade();
        }
    }

    private void placeTowers(){
        ArrayList<int[]> towerPoints = towerPoints();
        for(int[] c: towerPoints){
            map[c[0]][c[1]].placeTower();
        }
    }

    private void placePortals(){
        ArrayList<Teleport> portals = this.variables.getPortals();
        for (Teleport p : portals){
            ArrayList<int[]> portalPoints = p.getPoints();
            double angle = p.getDegreeOut();
            int[] out = portalPoints.get(portalPoints.size()-1);
            map[out[0]][out[1]].placeTeleportOUT(angle);
            portalPoints.remove(portalPoints.size()-1);
            for(int[] c: portalPoints){
                map[c[0]][c[1]].placeTeleportIN(out,angle);
            }
        }
    }

    public boolean hasWall(int x, int y){
        return map[x][y].hasWall();
    }

    public boolean hasWall(int[] pos){
        int x = pos[0];
        int y = pos[1];
        return map[x][y].hasWall();
    }



    private ArrayList<int[]> wallPoints() {
        ArrayList<Wall> walls = this.variables.getWalls();
        ArrayList<int[]> wallPoints = new ArrayList<>();

        for (Wall w : walls) {
            ArrayList<int[]> wall = w.getPoints();
            wallPoints.addAll(wall);
        }
        return wallPoints;
    }



    private ArrayList<int[]> shadesPoints(){
        ArrayList<Shade> shades = this.variables.getShades();
        ArrayList<int[]> shadePoints = new ArrayList<>();
        for(Shade s: shades){
            shadePoints.addAll(s.getPoints());
        }
        return shadePoints;
    }

    private ArrayList<int[]> towerPoints(){
        ArrayList<Tower> towers = this.variables.getTowers();
        ArrayList<int[]> towerPoints = new ArrayList<>();
        for(Tower t: towers){
            towerPoints.addAll(t.getPoints());
        }
        return towerPoints;
    }
    private ArrayList<int[]> teleportPoints(){
        return new ArrayList<int[]>();
    }

    public static boolean inMap(int[] position){
        return position[0] < GameController.variables.getHeight()
                && position[1] < GameController.variables.getWidth() && position[0] >= 0 && position[1] >= 0;
    }

    public static boolean inMap(int x, int y){
        return x < GameController.variables.getHeight()
                && y < GameController.variables.getWidth() && x >= 0 && y >= 0;
    }

    // Getters n Setters
    public Variables getVariables() {
        return variables;
    }

    public Tile getTile(int x, int y){
        return this.map[x][y];
    }

    public Tile[][] getTiles(){
        return this.map;
    }

    public int getMapHeight(){
        return this.ySize;
    }

    public int getMapWidth(){
        return this.xSize;
    }

    public ArrayList<int[]> getWallpoints(){ return wallpoints;}

    public String toString(){
        StringBuilder s = new StringBuilder();

        for(int i = 0; i<xSize;i++){
            s.append(i+": ");
            for(int j = 0; j<ySize; j++){
                String c = map[i][j].toString();
                s.append(c);
            }
            s.append("\n");
        }
        return s.toString();
    }

}

package RayCasting;

import base.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import static java.lang.Math.*;

public class RayCasting {

    int mapHeight;
    int mapWidth;
    ArrayList<Cell> world;
    LinkedList<Edge> edges = new LinkedList<>();
    ArrayList<Ray> visionPolygonPoints = new ArrayList<>();
    Map map;

    public RayCasting(){
        this.map = GameController.map;
        Tile[][] tiles = map.getMap();
        this.mapHeight = Variables.MAP_HEIGHT;
        this.mapWidth = Variables.MAP_WIDTH;
        world = new ArrayList<>();
        for (int i = 0; i < this.mapHeight; i++) {
            for (int j = 0; j < this.mapWidth; j++) {
                Cell cell = new Cell();
                if(tiles[i][j].isWall()){
                    cell.exists = true;
                }
                world.add(cell);
            }
        }
        convertToRayCastingMap();
    }

    /**
     * method represents map in list edges as a list of cells with edges
     * representation in edges list
     */
    public void convertToRayCastingMap(){

        // coordinates of point at which to start conversion into map with edges
        int startX = 0, startY = 0;
        // height and width of map with edges
        int width = this.mapWidth, height = this.mapHeight;
        // width of a tile and pitch for conversion
        int blockWidth = 1, pitch = this.mapWidth;

        edges.clear();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < 4; k++) {
                    int i1 = (j + startY) * pitch + (i + startX);
                    world.get(i1).edgeExists[k] = false;
                    world.get(i1).edgeId[k] = 0;
                }
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int currentCellIndex = (j + startY) * pitch + (i + startX);		// This
                int n = (j + startY - 1) * pitch + (i + startX);		// Northern Neighbour
                int s = (j + startY + 1) * pitch + (i + startX);		// Southern Neighbour
                int w = (j + startY) * pitch + (i + startX - 1);	// Western Neighbour
                int e = (j + startY) * pitch + (i + startX + 1);	// Eastern Neighbour

                // North = 0 | South = 1 | East = 2 | West = 3|

                if(world.get(currentCellIndex).exists){
                    if( isInMap(currentCellIndex, 3) && !world.get(w).exists){
                        if (isInMap(currentCellIndex, 0) && world.get(n).edgeExists[3]){
                            float p = edges.get(world.get(n).edgeId[3]).getEndY();
                            edges.get(world.get(n).edgeId[3]).setEndY(p+blockWidth);
                            world.get(currentCellIndex).edgeId[3] = world.get(n).edgeId[3];

                        }
                        else
                        {
                            Edge edge = new Edge();

                            edge.setStartX( (startX + i) * blockWidth);
                            edge.setStartY((startY+j)*blockWidth);
                            edge.setEndX(edge.getStartX());
                            edge.setEndY(edge.getStartY() + blockWidth);
                            int edge_id = edges.size();

                            edges.add(edge);

                            world.get(currentCellIndex).edgeId[3] = edge_id;
                        }
                        world.get(currentCellIndex).edgeExists[3] = true;
                    }

                    if( isInMap(currentCellIndex, 2)  && !world.get(e).exists){
                        if (isInMap(currentCellIndex, 0)  && world.get(n).edgeExists[2]){
                            float p = edges.get(world.get(n).edgeId[2]).getEndY();
                            edges.get(world.get(n).edgeId[2]).setEndY(p+blockWidth);
                            world.get(currentCellIndex).edgeId[2] = world.get(n).edgeId[2];
                        }
                        else
                        {
                            Edge edge = new Edge();

                            edge.setStartX( (startX + (i+1)) * blockWidth);
                            edge.setStartY((startY+j)*blockWidth);
                            edge.setEndX(edge.getStartX());
                            edge.setEndY(edge.getStartY() + blockWidth);

                            // Add edge to Polygon Pool
                            int edge_id = edges.size();
                            edges.add(edge);

                            // Update tile information with edge information
                            world.get(currentCellIndex).edgeId[2] = edge_id;
                        }
                        world.get(currentCellIndex).edgeExists[2] = true;
                    }

                    if( isInMap(currentCellIndex, 0) && !world.get(n).exists){
                        if (isInMap(currentCellIndex, 3)  && world.get(w).edgeExists[0]){
                            float p = edges.get(world.get(w).edgeId[0]).getEndX();
                            edges.get(world.get(w).edgeId[0]).setEndX(p+blockWidth);
                            world.get(currentCellIndex).edgeId[0] = world.get(w).edgeId[0];
                        }
                        else
                        {
                            Edge edge = new Edge();

                            edge.setStartX( (startX + i) * blockWidth);
                            edge.setStartY((startY+j)*blockWidth);
                            edge.setEndX(edge.getStartX() + blockWidth);
                            edge.setEndY(edge.getStartY());

                            // Add edge to Polygon Pool
                            int edge_id = edges.size();
                            edges.add(edge);

                            // Update tile information with edge information
                            world.get(currentCellIndex).edgeId[0] = edge_id;
                        }
                        world.get(currentCellIndex).edgeExists[0] = true;
                    }

                    if( isInMap(currentCellIndex, 1) && !world.get(s).exists){
                        if (isInMap(currentCellIndex, 3)  && world.get(w).edgeExists[1]){
                            float p = edges.get(world.get(w).edgeId[1]).getEndX();
                            edges.get(world.get(w).edgeId[1]).setEndX(p+blockWidth);
                            world.get(currentCellIndex).edgeId[1] = world.get(w).edgeId[1];

                        }
                        else
                        {
                            Edge edge = new Edge();

                            edge.setStartX( (startX + i) * blockWidth);
                            edge.setStartY((startY+(j+1))*blockWidth);
                            edge.setEndX(edge.getStartX() + blockWidth);
                            edge.setEndY(edge.getStartY());

                            // Add edge to Polygon Pool
                            int edge_id = edges.size();
                            edges.add(edge);

                            // Update tile information with edge information
                            world.get(currentCellIndex).edgeId[1] = edge_id;
                        }
                        world.get(currentCellIndex).edgeExists[1] = true;
                    }
                }
            }
        }

        // add edges on each of the 4 map boarders as a limit of the viewing range; agent cannot see past map
        edges.add(new Edge(startX, startY, width, startY));
        edges.add(new Edge(startX, startY, startX, height));
        edges.add(new Edge(width, 0, width, height));
        edges.add(new Edge(0, height, width, height));
    }

    /**
     * @return List of [x,y] coordinates of tiles in agent's viewing range
     */
    public ArrayList<int[]> getVisibleTiles(Agent agent){

        float
                agentX = agent.getX(),
                agentY = agent.getY(),
                visionRange = Variables.AGENT_VISION_RANGE,
                angle = agent.getAngle();
        float[]
                limits  = getViewingLimits(angle);

        calculateVision( agentX, agentY, visionRange, limits); // creates endpoints of vision polygon

        printPolygon();

        double[][] polPoints = getPolygonCoords(agent.getPosition());

        ArrayList<int[]> listOfVisibleTiles = new ArrayList<>();

        // for every tile that can possibly be seen by agent with respect to its vision range
        // if tile is in its vision polygon, add to list of visible tiles
        for(int x = (int) (agentX-(visionRange+1)); x < agentX+visionRange+1; x++){
            for(int y = (int) (agentY - (visionRange + 1)); y < agentY + visionRange + 1; y++ ){
                if(Map.inMap(x,y)){
                    Tile tile = map.getTile(x,y);
                    if(pnpoly(polPoints[0], polPoints[1], tile.getPosition()[0]+0.5, tile.getPosition()[1]+0.5)){
                        listOfVisibleTiles.add(new int[]{tile.getPosition()[0], tile.getPosition()[1]});
                    }
                }
            }
        }

        return listOfVisibleTiles;

    }

    static boolean pnpoly(double[] vertx, double[] verty, double testx, double testy) {
        int nvert = vertx.length;
        int i, j;
        boolean c = false;
        for (i = 0, j = nvert-1; i < nvert; j = i++) {
            if ( ((verty[i]>testy) != (verty[j]>testy)) &&
                    (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
                c = !c;
        }
        return c;
    }

    private double[][] getPolygonCoords(int[] agentPosition){
        double[] polX = new double[visionPolygonPoints.size()+1];
        double[] polY = new double[visionPolygonPoints.size()+1];

        polX[0] = agentPosition[0];
        polY[0] = agentPosition[1];

        for (int i = 0; i < visionPolygonPoints.size(); i++) {
            polX[i+1] = visionPolygonPoints.get(i).getEndX();
            polY[i+1] = visionPolygonPoints.get(i).getEndY();
        }

        return new double[][]{polX, polY};

    }

    /**
     * method fills visionPolygonPoints with points describing agent's "vision polygon"
     * @param originX: x-coordinate of agent
     * @param originY: y-coordinate of agent
     * @param radius: agent's viewing distance
     * @param limits: [limit1,limit2] of agent's viewing angle respective to its orientation
     */
    public void calculateVision(float originX, float originY, float radius, float[] limits) {

        visionPolygonPoints.clear();

        for (Edge edge1 : edges) {
            for (int i = 0; i < 2; i++) {
                float rayVectorX, rayVectorY;
                rayVectorX = (i == 0 ? edge1.getStartX() : edge1.getEndX()) - originX;
                rayVectorY = (i == 0 ? edge1.getStartY() : edge1.getEndY()) - originY;

                float baseAngle = (float) atan2(rayVectorY, rayVectorX);
                float angle = 0;

                for (int j = 0; j < 2; j++) {
                    float limit1 = limits[0], limit2 = limits[1];

                    if(limit1 <= baseAngle && baseAngle <= limit2){
                        if (j == 0) {
                            angle = baseAngle - 0.0001f;
                        }
                        if (j == 1) {
                            angle = baseAngle + 0.0001f;
                        }

                        rayVectorX = (float) (radius * cos(angle));
                        rayVectorY = (float) (radius * sin(angle));

                        float lowestDistance = Float.POSITIVE_INFINITY;
                        float closestIntersectX = 0, closestIntersectY = 0, closestIntersectAng = 0;

                        int interceptionCount = 0;

                        for (Edge edge2 : edges) {

                            float edgeVectorX = edge2.getEndX() - edge2.getStartX(),
                                    edgeVectorY = edge2.getEndY() - edge2.getStartY();

                            float[] edgeStart = {edge2.getStartX(), edge2.getStartY()},
                                    edgeEnd = {edge2.getEndX(), edge2.getEndY()},
                                    rayStart = {originX, originY},
                                    rayEnd = {originX + rayVectorX, originY + rayVectorY};

                            if (Math.abs(edgeVectorX - rayVectorX) > 0.0f && Math.abs(edgeVectorY - rayVectorY) > 0.0f) {

                                float[] closestIntersectData = getDistanceOfIntersectionAlongRay(rayStart, rayEnd, edgeStart, edgeEnd);
                                float distance = closestIntersectData[0], edgeVectorCoef = closestIntersectData[1];

                                if (0 <= distance && distance <= 1 && 0 <= edgeVectorCoef && edgeVectorCoef <= 1) {
                                    interceptionCount++;
                                    if (distance < lowestDistance) {
                                        lowestDistance = distance;
                                        closestIntersectX = closestIntersectData[2];
                                        closestIntersectY = closestIntersectData[3];
                                        closestIntersectAng = (float) atan2(closestIntersectY - originY, closestIntersectX - originX);
                                    }
                                }
                            }
                        }

                        if(interceptionCount == 0) {
                            closestIntersectX = rayVectorX + originX;
                            closestIntersectY = rayVectorY + originY;
                            closestIntersectAng = angle;
                        }
                        visionPolygonPoints.add(new Ray(closestIntersectAng, closestIntersectX, closestIntersectY));
                    }
                }
            }
        }

        visionPolygonPoints.sort(Comparator.comparing(Ray::getAngle));
        visionPolygonPoints = removeDuplicates(visionPolygonPoints);
    }

    private ArrayList<Ray> removeDuplicates(ArrayList<Ray> list){
        Ray temp = list.get(list.size()-1);
        ArrayList<Ray> output = new ArrayList<>();

        for(Ray ray : list){
            if(!ray.sameAs(temp))
                output.add(ray);temp=ray;
        }
        return output;
    }

    private float[] getViewingLimits(float angle){
        float limit1, limit2;
        limit1 = (float) (toRadians(angle) - Variables.FIELD_OF_VIEW/2);
        limit2 = (float) (toRadians(angle) + Variables.FIELD_OF_VIEW/2);

        return new float[]{limit1,limit2};
    }

    private boolean isInMap(int currentIndex, int directionOfNeighbour){

        // North = 0 | South = 1 | East = 2 | West = 3|

        int currentX = currentIndex % this.mapHeight;
        int currentY = currentIndex / this.mapWidth;

        if(currentX == 0 && directionOfNeighbour == 3) {
            return false;
        }
        else if (currentY == 0 && directionOfNeighbour == 0) {
            return false;
        }
        else if (currentX == this.mapWidth-1 && directionOfNeighbour == 2) {
            return false;
        }
        else if (currentY == this.mapHeight-1 && directionOfNeighbour == 1) {
            return false;
        }
        return true;
    }

    private float[] getDistanceOfIntersectionAlongRay(float[] rayStart, float[] rayEnd,float[] edgeStart, float[] edgeEnd){

        // see: https://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect

        float rx = rayStart[0], ry = rayStart[1]; // ray start point
        float ex = edgeStart[0], ey = edgeStart[1]; // end start point

        float rvx = rayEnd[0] - rayStart[0], rvy = rayEnd[1] - rayStart[1]; // ray vector
        float evx = edgeEnd[0] - edgeStart[0], evy = edgeEnd[1] - edgeStart[1]; // edge vector

        // to get numerator of t
        float[] e_r = {ex-rx, ey-ry};
        float t_numerator = crossProduct(e_r, new float[]{evx, evy});
        // to get denominator of t
        float t_denominator = crossProduct(new float[]{rvx, rvy}, new float[]{evx, evy});
        // to get t
        float t = t_numerator/t_denominator;

        // to get u
        float u_numerator = crossProduct(e_r, new float[]{rvx, rvy});
        float u_denominator = t_denominator;

        float u = u_numerator/u_denominator;

        float px = rx + t * rvx, py = ry + t * rvy;

        return new float[]{t, u , px, py};

    }

    private float crossProduct(float[] vect1, float[] vect2){
        return vect1[0]*vect2[1] - vect1[1]*vect2[0];
    }

    public void printPolygon(){
        String s = "polygon(O,";

        for(Ray r : visionPolygonPoints){
            float x  = r.getEndX(), y = r.getEndY();
            s+="("+x+","+y+"),";
        }

        s += "O)";

        System.out.println(s);
    }
}
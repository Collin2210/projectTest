package rayTracer;

import Controller.Map;
import Controller.Tile;
import Controller.Variables;
import base.*;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import static rayTracer.Driver.dist;

public class RayCaster {
    int mapHeight, mapWidth;
    Map map;
    private LinkedList<Line2D.Float> lines;
    Polygon p;
    ArrayList<Point2D> listOfPoints ;
    private static Random r = new Random(100);
    LinkedList<Line2D.Float> rays;
    Agent a;
    private ArrayList<int[]> visionArea = new ArrayList<>();


    public RayCaster(Agent a){
        this.map = GameController.map;
        this.a = a;
        Tile[][] tiles = map.getTiles();
        this.mapHeight = map.getMapHeight();
        this.mapWidth = map.getMapWidth();
        lines = buildLines();
    }

    private LinkedList<Line2D.Float> buildLines(){
        LinkedList<Line2D.Float> lines = map.getVariables().getRayCastingWalls();
        return lines;
    }


    public LinkedList<Line2D.Float> calcRays(LinkedList<Line2D.Float> lines, int x, int y, int resolution, int maxDist){
        float[] distants = new float[resolution];
        LinkedList<Line2D.Float> rays = new LinkedList<>();
        // (angle (4 directions) * 90-visionrange/2)/2 and
        int start = (int) ((-Variables.FIELD_OF_VIEW/2+ a.getAngleDeg())/2),
                end = (int)(Variables.FIELD_OF_VIEW/2+a.getAngleDeg())/2;
        for (int i = start; i < end; i++) {
            double direction = (Math.PI*2) * ((double) i/resolution);
            float minDist = maxDist;
            for (Line2D.Float line : lines){
                float distance = getRayCast(x,y, x+(float) Math.cos(direction)*maxDist, y+(float) Math.sin(direction)* maxDist, line.x1, line.y1, line.x2,line.y2);
                if (distance<minDist&& distance>0){
                    minDist = distance;
                }
            }
            rays.add(new Line2D.Float(x,y, x+(float) Math.cos(direction)*minDist, y+(float) Math.sin(direction)* minDist));
        }
        return rays;
    }

    public static float getRayCast(float p0_x, float p0_y, float p1_x, float p1_y, float p2_x, float p2_y, float p3_x, float p3_y) {
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = p1_x - p0_x;
        s1_y = p1_y - p0_y;
        s2_x = p3_x - p2_x;
        s2_y = p3_y - p2_y;

        float s, t;
        s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
        t = (s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            // Collision detected
            float x = p0_x + (t * s1_x);
            float y = p0_y + (t * s1_y);

            return dist(p0_x, p0_y, x, y);
        }

        return -1; // No collision
    }

    public Polygon createPolygon(){

        listOfPoints = new ArrayList<>();
        p = new Polygon();
        if(rays.size()<2){
            return p;
        }
        Line2D line = rays.get(0);
        addPoint(p, line.getP1());
        listOfPoints.add(line.getP1());

        for (int i = 1; i < rays.size(); i++) {
            addPoint(p, line.getP2());
            listOfPoints.add(line.getP2());
            line = rays.get(i);
        }
        addPoint(p, line.getP2());
        listOfPoints.add(line.getP2());
        return p;
    }

    public void addPoint(Polygon p, Point2D point) {
        int x = (int) point.getX();
        int y = (int) point.getY();
        p.addPoint(x, y);
    }


    public boolean contains(Point test) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = listOfPoints.size() - 1; i < listOfPoints.size(); j = i++) {
            if ((listOfPoints.get(i).getY() > test.y) != (listOfPoints.get(j).getY() > test.y) &&
                    (test.x < (listOfPoints.get(j).getX() - listOfPoints.get(i).getX()) * (test.y - listOfPoints.get(i).getY()) / (listOfPoints.get(j).getY()-listOfPoints.get(i).getY()) + listOfPoints.get(i).getX())) {
                result = !result;
            }
        }
        return result;
    }


    public void calculate(Agent a){

        rays = calcRays(lines, a.getX(), a.getY(), 180, GameController.variables.getVisionRange());
        p = createPolygon();
    }

    public ArrayList<int[]> getVisibleTiles(Agent agent){

        float
                agentX = agent.getX(),
                agentY = agent.getY(),
                visionRange = GameController.variables.getVisionRange(),
                angle = (float) agent.getAngleDeg();

        if(agent.isOnTower())
            visionRange += GameController.TOWER_VISION_BONUS;

        ArrayList<int[]> listOfVisibleTiles = new ArrayList<>();

        for(int x = (int) (agentX-(visionRange+1)); x < agentX+visionRange+1; x++){
            for(int y = (int) (agentY - (visionRange + 1)); y < agentY + visionRange + 1; y++ ){
                int[] position = {x,y};
                if(Map.inMap(position)){
                    Tile tile2 = map.getTile(x,y);
                    if(contains(new Point(tile2.getPosition()[0], tile2.getPosition()[1]))){
                        listOfVisibleTiles.add(new int[]{tile2.getPosition()[0], tile2.getPosition()[1]});
                    }
                }
            }
        }

        return listOfVisibleTiles;

    }
}

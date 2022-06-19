package rayTracer;

import Controller.Map;
import Controller.Tile;
import Controller.Variables;
import QLearning.RewardTable;
import base.*;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class RayCaster {
    int mapHeight, mapWidth;
    Map map;
    private LinkedList<Line2D.Float> lines;
    Polygon p;
    ArrayList<Point2D> listOfPoints ;
    private static Random r = new Random(100);
    LinkedList<Line2D.Float> rays;
    Agent a;

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

    public static float getRayCast(float firstPointX, float firstPointY, float secondPointX, float secondPointY, float thirdPointX, float thirdPointY, float fourthPointX, float fourthPointY) {

        float rayPointOneX = secondPointX - firstPointX;
        float rayPointOneY = secondPointY - firstPointY;
        float rayPointTwoX = fourthPointX - thirdPointX;
        float rayPointTwoY = fourthPointY - thirdPointY;

        float start = (-rayPointOneY * (firstPointX - thirdPointX) + rayPointOneX * (firstPointY - thirdPointY)) / (-rayPointTwoX * rayPointOneY + rayPointOneX * rayPointTwoY);
        float end = (rayPointTwoX * (firstPointY - thirdPointY) - rayPointTwoY * (firstPointX - thirdPointX)) / (-rayPointTwoX * rayPointOneY + rayPointOneX * rayPointTwoY);

        if (start >= 0 && start <= 1 && end >= 0 && end <= 1) {
            float x = firstPointX + (end * rayPointOneX);
            float y = firstPointY + (end * rayPointOneY);
            return (float) RewardTable.distanceBetweenPoints((int) firstPointX, (int) firstPointY, (int) x, (int) y);
        }

        return -1;
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
        rays = calcRays(lines, a.getX(), a.getY(), 180, a.getVisionRange());
        p = createPolygon();
    }

    public ArrayList<int[]> getVisibleTiles(Agent agent){

        float
                agentX = agent.getX(),
                agentY = agent.getY(),
                visionRange = agent.getVisionRange(),
                angle = (float) agent.getAngleDeg();

        if(agent.isOnTower())
            visionRange += GameController.TOWER_VISION_BONUS;

        ArrayList<int[]> listOfVisibleTiles = new ArrayList<>();

        for(int x = (int) (agentX-(visionRange+1)); x < agentX+visionRange+1; x++){
            for(int y = (int) (agentY - (visionRange + 1)); y < agentY + visionRange + 1; y++ ){
                int[] position = {x,y};
                if(Map.inMap(position) && !map.getTile(x,y).hasShade()){
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

package rayTracer;

import javax.swing.*;
import javax.swing.text.Segment;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.List;

public class Driver implements Runnable, MouseMotionListener {

    public static final int WIDTH = 1600, HEIGHT = 900;
    public static final int numLines = 12;

    private JFrame frame;
    private Canvas canvas;
    private static Random r = new Random(100);

    private LinkedList<Line2D.Float> lines;
    private int mousex=0, mousey=0;
    private LinkedList<Point2D.Float> polyPoints;
    Polygon p;
    ArrayList<Point2D> listOfPoints;



    private Driver(){
        lines = buildLines();
        frame = new JFrame("Raycast");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(canvas = new Canvas());
        canvas.addMouseMotionListener(this);
        frame.setSize(WIDTH,HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        new Thread(this).start();
    }

    private LinkedList<Line2D.Float> buildLines(){
        LinkedList<Line2D.Float> lines = new LinkedList<>();
        for (int i = 0; i < numLines; i++) {
            int x1 = r.nextInt(WIDTH);
            int y1 = r.nextInt(HEIGHT);
            int x2 = r.nextInt(WIDTH);
            int y2 = r.nextInt(HEIGHT);
            lines.add(new Line2D.Float(x1,y1,x2,y2));
        }
        return lines;
    }

    private LinkedList<Point2D.Float> getPolyPoints(){
        LinkedList<Point2D.Float> polygonPoints = new LinkedList<>();
        for (int i = 0; i < 75; i++) {

        }
        return polygonPoints;
    }

    @Override
    public void run() {
        while(true){
            tick();
            render();
        }
    }

    public void tick(){
    }

    public void render(){
        BufferStrategy bs = canvas.getBufferStrategy();
        if(bs == null){
            canvas.createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0, canvas.getWidth(), canvas.getHeight());

        g.setColor(Color.GREEN);
        for (Line2D.Float line:lines) {
            g.drawLine((int) line.x1, (int) line.y1, (int) line.x2, (int) line.y2);
        }

        g.setColor(Color.RED);
        LinkedList<Line2D.Float> rays = calcRays(lines, mousex, mousey, 180, 400);
        for (Line2D.Float ray:rays) {
            g.drawLine((int) ray.x1, (int) ray.y1, (int) ray.x2, (int) ray.y2);
        }

        g.setColor(Color.YELLOW);
        Polygon p = createPolygon();
// make a for loop here for x and y - and + the vision range around agentPos
        if(contains(new Point(50,50))){
            System.out.println("Point is inside");
        }
        else{
            System.out.println("Not Inside");
        }

        g.drawPolygon(p);

        g.dispose();
        bs.show();
    }

    private LinkedList<Line2D.Float> calcRays(LinkedList<Line2D.Float> lines, int x, int y, int resolution, int maxDist){
        float[] distants = new float[resolution];
        LinkedList<Line2D.Float> rays = new LinkedList<>();
        // (angle (4 directions) * 90-visionrange/2)/2 and
        for (int i = (-75+180)/2; i < (75+180)/2; i++) {
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

    public static void main(String[] args) {
        new Driver();
    }


    public static float dist(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
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

    @Override
    public void mouseDragged(MouseEvent e) {
        mousex = e.getX();
        mousey = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousex = e.getX();
        mousey = e.getY();
    }

    public Polygon createPolygon(){

        listOfPoints = new ArrayList<>();
        p = new Polygon();
        LinkedList<Line2D.Float> rays = calcRays(lines, mousex, mousey, 180, 400);
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

    protected void addPoint(Polygon p, Point2D point) {
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

}
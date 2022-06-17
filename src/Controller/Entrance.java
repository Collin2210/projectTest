package Controller;

import java.util.ArrayList;

public abstract class Entrance extends MapObject {

    private ArrayList<Integer> coordIn = new ArrayList<Integer>();

    private ArrayList<Integer> coordOut = new ArrayList<Integer>();

    private ArrayList<int[]> pointsIn = new ArrayList<>();
    int[] pointOut = new int[2];

    private double degreeOut;

    public Entrance(int x1, int y1, int x2, int y2, int x3, int y3, double degree){
        super(); //ensures that id is added
        this.coordIn.add(x1); this.coordIn.add(y1); this.coordIn.add(x2); this.coordIn.add(y2); //starting points
        this.coordOut.add(x3); this.coordOut.add(y3); //exit point
        this.degreeOut = degree;
        
        super.coords = new ArrayList<Integer>(coordIn);
        super.coords.addAll(this.coordOut); //this allows to retrieve all coords at once with getCoords from MapObject
        super.points = computePoints();
    }

    /**
     * Returns array list that has IN points (int[]) in the list and the OUT point (int[]) as the last element
     * */
    public ArrayList<int[]> computePoints(){
        int[] start = new int[2];
        int[] end = new int[2];

        start[0] = coords.get(0);
        start[1] = coords.get(1);

        end[0] = coords.get(2);
        end[1] = coords.get(3);

        ArrayList<int[]> points = new ArrayList<>();

        for(int i = start[0]; i<end[0]; i++){
            for(int j = start[1]; j<end[1]; j++){
                int[] coords = new int[2];
                coords[0] = i;
                coords[1] = j;
                points.add(coords);
            }
        }

        pointsIn = points;
        int[] out = new int[2];
        out[0] = coords.get(4);
        out[1] = coords.get(5);
        pointOut = out;
        points.add(out);
        return points;
    }

    public ArrayList<int[]> getPointsIn(){
        return pointsIn;
    }

    public int[] getPointOut(){
        return pointOut;
    }

    public double getDegreeOut(){
        return degreeOut;
    }

    public ArrayList<Integer> getCoordIn() {
        return coordIn;
    }

    public void setCoordIn(ArrayList<Integer> coordIn) {
        this.coordIn = coordIn;
    }

    public ArrayList<Integer> getCoordOut() {
        return coordOut;
    }

    public void setCoordOut(ArrayList<Integer> coordOut) {
        this.coordOut = coordOut;
    }
}

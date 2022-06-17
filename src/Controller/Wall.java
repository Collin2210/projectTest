package Controller;

import java.util.ArrayList;

public class Wall extends Rectangle {

    public int x1,y1,x3,y3;

    public Wall(int x1, int y1,int x3,int y3){
        super(x1,y1,x3,y3);
        this.x1 = x1;
        this.y1 = y1;
        this.x3 = x3;
        this.y3 = y3;
        super.points = computePoints();
    }

    public ArrayList<int[]> computePoints(){
        int[] start = new int[2];
        int[] end = new int[2];

        ArrayList<int[]> points = new ArrayList<>();

        for(int i=coords.get(0);i<=coords.get(2);i++){
            int[] c = new int[2];
            c[0] = i;
            c[1] = coords.get(1);
            points.add(c);
        }
        for(int i=coords.get(6);i<=coords.get(4);i++){
            int[] c = new int[2];
            c[0] = i;
            c[1] = coords.get(5);
            points.add(c);
        }

        for(int i=coords.get(1);i<=coords.get(7);i++){
            int[] c = new int[2];
            c[0] = coords.get(0);
            c[1] = i;
            points.add(c);
        }

        for(int i=coords.get(3);i<=coords.get(5);i++){
            int[] c = new int[2];
            c[0] = coords.get(2);
            c[1] = i;
            points.add(c);
        }

        /*for(int[] p: points){
            System.out.println(p[0]+", "+p[1]);
        }*/

        return points;
    }

}
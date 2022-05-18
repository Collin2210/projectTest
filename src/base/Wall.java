package base;



import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Rectangle;

public class Wall extends Rectangle {

    public Wall(int x1, int y1,int x3,int y3){
        super(x1,y1,x3,y3);
    }

    public ArrayList<int[]> getPoints(){
        int[] start = new int[2];
        int[] end = new int[2];

        ArrayList<int[]> points = new ArrayList<>();
        ArrayList<Integer> coords = new ArrayList<Integer>();

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
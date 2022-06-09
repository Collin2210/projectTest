package base;

import Controller.Map;
import Controller.Variables;

import java.util.ArrayList;

public class GuardAlgo {
    public static int[] partitionMap(){
        Map map = GameController.map;
        Variables var = GameController.variables;
        int
                numberOfGuards = var.getNumberOfGuards(),
                mapHeight = var.getHeight(),
                mapWidth = var.getWidth();

    }

    private static int[][] partitionRectangleIn2(int[] rect){
        int
                origin = rect[0],
                height = rect[1],
                width = rect[2];

        // partition highest dimension
        int highestDimIndex = 1; // height
        if(width > height)
            highestDimIndex = 2; // width

        int[]
    }

    private static int[] createSubRect1(int[] rect, int highestDimIndex){
        rect[highestDimIndex] = rect[highestDimIndex]/2;
        return rect;
    }

    private static int[] createSubRect2(int[] rect, int highestDimIndex){
        rect[highestDimIndex] = rect[highestDimIndex]/2;
        rect[0] = rect[0]+rect[highestDimIndex];
        return rect;
    }
}

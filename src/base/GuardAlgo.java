package base;

import Controller.Map;
import Controller.Variables;

import java.util.ArrayList;
import java.util.Arrays;

public class GuardAlgo {

    private static final int
            HEIGHT_INDEX = 2,
            WIDTH_INDEX = 3;

    public static ArrayList<double[]> getAreasForGuards(){
        double
                height = GameController.variables.getHeight(),
                width = GameController.variables.getWidth();

        double[] map = {0, 0, height, width};

        return divideArea(map, GameController.variables.getNumberOfGuards());
    }

    private static ArrayList<double[]> divideArea(double[] area, double numOfPartitions){
        ArrayList<double[]> partitions = new ArrayList<>();
        partitions.add(area);
        for(double partition_count = 1; partition_count < numOfPartitions; partition_count++){
            // get rect to partition
            int rect_to_partition_index = getRectToPartition(partitions);
            double[] rect_to_partition = partitions.get(rect_to_partition_index);

            // partition
            byte dimension_to_partition_by = getHighestDimensionIndex(rect_to_partition);
            double[] subRect1 = createSubRect1(rect_to_partition, dimension_to_partition_by);
            double[] subRect2 = createSubRect2(rect_to_partition, dimension_to_partition_by);

            // replace in list
            partitions.remove(rect_to_partition_index);
            partitions.add(subRect1);
            partitions.add(subRect2);
        }

        return partitions;
    }

    private static int getRectToPartition(ArrayList<double[]> rectangles){
        double[] rect_with_highest_area = rectangles.get(0);
        double highest_area = Double.MIN_VALUE;

        for(double[] r : rectangles){
            double area = r[HEIGHT_INDEX] * r[WIDTH_INDEX];
            if(area > highest_area){
                highest_area = area;
                rect_with_highest_area = r;
            }
        }

        return rectangles.indexOf(rect_with_highest_area);
    }

    private static byte getHighestDimensionIndex(double[] rect){
        if(rect[HEIGHT_INDEX] >= rect[WIDTH_INDEX])
            return HEIGHT_INDEX;
        return WIDTH_INDEX;
    }

    private static double[] createSubRect1(double[] rect, byte highestDimIndex){
        double[] subRect = Arrays.copyOf(rect, rect.length);
        subRect[highestDimIndex] = rect[highestDimIndex]/2;
        return subRect;
    }

    private static double[] createSubRect2(double[] rect, byte highestDimIndex){
        double[] subRect = Arrays.copyOf(rect, rect.length);
        subRect[highestDimIndex] = rect[highestDimIndex]/2;

        if(highestDimIndex == HEIGHT_INDEX)
            subRect[1] = subRect[1] + subRect[highestDimIndex];
        else
            subRect[0] = subRect[0] + subRect[highestDimIndex];
        return subRect;
    }
}

package RayCasting;

class Cell {

    // North = 0 | South = 1 | East = 2 | West = 3|

    public int[] edgeId;
    public boolean[] edgeExists;
    public boolean exists;

    public Cell(){
        edgeId = new int[4];
        edgeExists = new boolean[4];
        exists = false;
    }
}

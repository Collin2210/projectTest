package Path;

import Controller.Map;
import base.GameController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Position implements Comparable<Position> {
    int x;

    int y;

    //Move Function
    public double weight = 0;


    //Cost Function
    public double cost = 0;



    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public float getDistance(){
        return 0;
    }

    public boolean samePos(Position pos){
        return this.x == pos.x && this.y == pos.y;
    }

    public Collection<Position> getNeighbours(){

        Map map = GameController.map;

        List<Position> neighbours = new ArrayList<>();
        neighbours.add(new Position(this.x+1, this.y));
        neighbours.add(new Position(this.x+1, this.y-1));
        neighbours.add(new Position(this.x+1, this.y+1));
        neighbours.add(new Position(this.x, this.y-1));

        neighbours.add(new Position(this.x, this.y+1));
        neighbours.add(new Position(this.x-1, this.y+1));
        neighbours.add(new Position(this.x-1, this.y-1));
        neighbours.add(new Position(this.x-1, this.y));

        List<Position> delete = new ArrayList<>();
        for(Position pos : neighbours){

            if (pos.x < 0 || pos.y < 0 || pos.x >= map.getMapWidth() || pos.y >= map.getMapHeight()){
                delete.add(pos);
            }else{
                if (getFieldCost(pos.x, pos.y) == Integer.MAX_VALUE){
                    delete.add(pos);
                }else if (Math.abs(this.x-pos.x) > 0 && Math.abs(this.y-pos.y) > 0){

                    //going diagonally
                    if (getFieldCost(pos.x, this.y) == 1 && getFieldCost(this.x, pos.y) == 1){
                        delete.add(pos);
                    }

                }

            }



            // check if position has a wall or is outside the map.
            // remove from neighboours if so
        }

        for (Position pos : delete){
            neighbours.remove(pos);
        }


        return neighbours;
    }

    public Collection<Position> getNeighbours(int[][] map){

        List<Position> neighbours = new ArrayList<>();
        if(Map.inMap(this.x+1, this.y))
            neighbours.add(new Position(this.x+1, this.y));
        if(Map.inMap(this.x, this.y-1))
            neighbours.add(new Position(this.x, this.y-1));
        if(Map.inMap(this.x, this.y+1))
            neighbours.add(new Position(this.x, this.y+1));
        if(Map.inMap(this.x-1, this.y))
            neighbours.add(new Position(this.x-1, this.y));

        List<Position> delete = new ArrayList<>();
        for(Position pos : neighbours){

            if (pos.x < 0 || pos.y < 0 || pos.x > map.length || pos.y > map[0].length){
                delete.add(pos);
            }else{
                if (map[pos.x][pos.y] == 1){
                    delete.add(pos);

                }else if (Math.abs(this.x-pos.x) > 0 && Math.abs(this.y-pos.y) > 0){
                    //if going diagonally
                    if (map[pos.x][this.y] == 1 && map[this.x][pos.y] == 1){
                        delete.add(pos);
                    }
                }
            }

            // check if position has a wall or is outside the map.
            // remove from neighboours if so
        }

        for (Position pos : delete){
            neighbours.remove(pos);
        }


        return neighbours;
    }


    public Collection<Position> getNeighbours(int[][] map, List<Position> exclude){


        List<Position> neighbours = new ArrayList<>(this.getNeighbours(map));

        List<Position> toExclude = new ArrayList<>();

        for (Position pos : neighbours){
            for (Position _pos : exclude){
                if (pos.samePos(_pos)){
                    toExclude.add(pos);
                }
            }
        }

        for (Position pos : toExclude){
            neighbours.remove(pos);
        }


        return neighbours;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }


    public double getCost(Position from, Position target){

        return this.weight+this.manhattanDistance(target);

    }

    public double euclideanDistance(Position targetPos){
        return Math.sqrt(Math.pow(targetPos.y-this.y, 2) + Math.pow(targetPos.x-this.x, 2));
    }

    public double manhattanDistance(Position targetPos){
        return Math.abs(targetPos.y-this.y) + Math.abs(targetPos.x-this.x);
    }

    public static double euclideanDistanceStatic(Position fromPos, Position targetPos){
        return Math.sqrt(Math.pow(targetPos.y-fromPos.y, 2) + Math.pow(targetPos.x-fromPos.x, 2));
    }

    public static double manhattanDistanceStatic(Position fromPos, Position targetPos){
        return Math.abs(targetPos.y-fromPos.y) + Math.abs(targetPos.x-fromPos.x);
    }

    public static int getFieldCost(int x, int y){
        if(GameController.map.hasWall(x,y))
            return Integer.MAX_VALUE;
        return 1;
    }

    @Override
    public int compareTo(Position o) {
        //return x == o.x && y == o.y ? 1 : 0;
        return this.weight > o.weight ? 1 : 0;
    }
}

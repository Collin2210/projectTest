package base;

public class Map {

    private final Tile[][] map;

    public Map(){
        this.map = new Tile[Variables.MAP_HEIGHT][Variables.MAP_WIDTH];

        for (int x = 0; x < Variables.MAP_HEIGHT; x++) {
            for (int y = 0; y < Variables.MAP_WIDTH; y++) {
                map[x][y] = new Tile(x, y);
            }
        }

    }

    public static boolean inMap(int x, int y){
        return 0 <= x && x < Variables.MAP_HEIGHT
                && 0 <= y && y < Variables.MAP_WIDTH;
    }


    public Tile getTile(int x, int y){
        return this.map[x][y];
    }

    public Tile[][] getMap() {
        return map;
    }
}

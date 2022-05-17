package base;

import java.util.ArrayList;

public class Guard extends Agent{

    private ArrayList<Tile> seenTiles;

    public Guard(int[] position) {
        super(position);
        seenTiles = new ArrayList<>();
    }

    private void explore(){
        // get random action that is not seen
    }
}

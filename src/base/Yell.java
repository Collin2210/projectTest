package base;

import Controller.Map;
import Controller.Tile;
import Controller.Variables;

import java.util.ArrayList;

public class Yell {
    /**
     * when guard comes to the position
     * checks within vision range for intruder
     * if intruder is there, se
     */
    Map map;
    Agent agent;
    Guard guard;
    Intruder intruder;
    private int timer = 10;
    public boolean isVisited;
    private int stressLevel;//needed for strength of yell?

    /**
     * propagate yell around guard vision
     */
    public void propagateYell() {
        //timer = 4;
        int yellRange = Variables.GUARD_YELL_RANGE;
        int
                x = agent.getX(),
                y = agent.getY();
        int
                yellStartX = x - yellRange,
                yellEndX = x + yellRange;
        int
                yellStartY = y + yellRange,
                yellEndY = y - yellRange;

        for (int i = yellStartX; i < yellEndX; i++) {
            for (int j = yellStartY; j < yellEndY; j++) {
                if (Map.inMap(i, j)) {
                    Tile tile = GameController.map.getTile(i, j);
                    tile.setYell();
                    yellPositions.add(new int[] {i,j});
                }
            }
        }
    }


    public int getTimer() {
        return timer;
    }

}
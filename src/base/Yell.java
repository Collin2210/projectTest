package base;

import Controller.Map;
import Controller.Tile;
import Controller.Variables;

import java.util.ArrayList;

public class Yell {
    /**
     * after checking vision range of guard to check if intruder is present
     * if yes, set yell=true for that guard
     * after doing it for all guards, go through the agent list
     * if yell boolean is true for 1 of the agents, check the distance between other agents and that agent
     * if within yelling radius, the guard moves towards the original guard
     *
     */
    private Guard guard;
    //private int timer;
    private int yellRadius=20;

    public ArrayList<int[]> yellPositions=new ArrayList<>();

    public Yell(Agent agent){
        guard = (Guard) agent;
        guard.startYelling();
        //timer = 4;
    }

    /**
     * propagate yell around guard vision
     */
    public void propagateYell() {
        //timer = 4;
        int yellRange = Variables.GUARD_YELL_RANGE;
        int
                x = guard.getX(),
                y = guard.getY();
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

    /**
     *
     */
    public void doYell(){
        for(Agent agent: GameController.agents){
            if (agent instanceof Guard){
                Guard guard = (Guard) agent;
                
                //guard.yell();
                



            }
        }



    }
/*
    public boolean CheckYell() {
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
                    Tile tile = map.getTile(i, j);
                    return tile.hasYell();
                }
            }
        }
        return false;
    }

    */


    public void remove(){
        for(int[] pos : yellPositions){
            GameController.map.getTile(pos[0],pos[1]).removeYell();
        }
        yellPositions.clear();
    }

    public int getYellRadius(){return yellRadius;}
    
    

}
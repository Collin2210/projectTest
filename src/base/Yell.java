package base;

import java.util.ArrayList;

public class Yell extends AudioObject {
    /**
     * after checking vision range of guard to check if intruder is present
     * if yes, set yell=true for that guard
     * after doing it for all guards, go through the agent list
     * if yell boolean is true for 1 of the agents, check the distance between other agents and that agent
     * if within yelling radius, the guard moves towards the original guard
     *
     */

    public static int YELL_RADIUS = 20;

    public ArrayList<int[]> yellPositions=new ArrayList<>();

    public Yell(Agent agent) {
        super(agent, YELL_RADIUS);
    }

}
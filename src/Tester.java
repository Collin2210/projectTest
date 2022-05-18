import base.GameController;
import base.Teleporter;

public class Tester {
    public static void main(String[] args) {

        GameController g = new GameController();

        int[][] agents = {{30,30}};
        g.addAgents(agents);

        int[][] goalPos = {{0,8}, {0,9}};
        g.addGoalTiles(goalPos);
        g.runRaycast();
        g.print();

    }
}

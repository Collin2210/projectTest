import base.GameController;

public class Tester {
    public static void main(String[] args) {

        GameController g = new GameController();

        int[][] agents = {{5,3}};
        g.addAgents(agents);

        int[][] goalPos = {{0,8}, {0,9}};
        g.addGoalTiles(goalPos);

        g.addVision();

        g.print();

    }
}

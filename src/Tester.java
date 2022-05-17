import base.GameController;

public class Tester {
    public static void main(String[] args) {

        GameController g = new GameController();

        int[][] goalPos = {{0,3}, {0,4}};
        g.addGoalTiles(goalPos);

        int[][] wallPos = { {2,0}, {2,1}, {2,2}, {3,4}};
        g.addWalls(wallPos);

        int[][] intruders = {{4,0}, {4,4}};
        g.addIntruder(intruders);

//        int[][] guards = { {1,4} };
//        g.addGuards(guards);

        GameController.print();

        g.makeAgentsLearn();

        g.makeAgentsMoveSmartly();

    }
}

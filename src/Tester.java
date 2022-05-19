import Controller.Variables;
import base.GameController;

public class Tester {
    public static void main(String[] args) {

        GameController g = new GameController();
        g.startGame();


//        GameController g = new GameController();
//
//        int[][] intruders = {{49,0}};
//        g.addIntruder(intruders);
//
        int[][] guards = {{7,47}};
//        g.addGuards(guards);
//
//        int[][] goalPos = {{0,49}, {0,48}};
//        g.addGoalTiles(goalPos);

        g.runRaycast();
        GameController.print();

        g.makeAgentsLearn();

        g.makeAgentsMoveSmartly();
    }
}

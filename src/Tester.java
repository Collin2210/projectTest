import base.GameController;

public class Tester {
    public static void main(String[] args) {

        GameController g = new GameController();
        g.startGame();
        GameController.print();
        g.makeAgentsLearn();
//        g.makeAgentsMoveSmartly();
    }
}
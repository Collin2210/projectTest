
import Controller.Position;
import base.GameController;

public class Tester {
    public static void main(String[] args) {

        //System.out.println(new Position(1, 1).equals(new Position(1, 1)));


        GameController g = new GameController();
        g.startGame();
        GameController.print();
        g.makeAgentsLearn();
        g.makeAgentsMoveSmartly();


    }
}

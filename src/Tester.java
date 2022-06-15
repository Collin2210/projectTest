
//import Controller.Position;
import base.GameController;

public class Tester {
    public static void main(String[] args) {
        GameController g = new GameController();
        g.startGame();
        GameController.print();
        g.makeAgentsLearn();
//        System.out.println("guards won " + GameController.numOfGuardWins);
//        System.out.println("intruders won " + GameController.numOfIntruderWins);
        g.makeAgentsMoveSmartly();
    }
}


/**
 *  NOTE :  a* takes diagonal movement into account
 * */
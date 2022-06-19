import QLearning.QLearning;
import base.GameController;

public class Tester {

    public static void main(String[] args) {
        // change lc and print move count

        for (int i = 1; i < 100; i++){
            System.out.println("start for learning cycle " + i);
            QLearning.LEARNING_CYCLES = i;
            GameController g = new GameController();
            g.startGame();

            // no guards
            g.makeAgentsLearn();

            // with guards
            g.addGuards();
            g.runRaycast();
            g.makeAgentsMoveSmartly();
        }

    }
}

// 1) experiments on learning cycle:  change learning cycles and check moveCount during learning phase1


// 2) change learning cycles and check win% for 100 games


// 3) change trace length // yell range and check win%


// 4)
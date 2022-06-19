package base;

import QLearning.QLearning;
import base.GameController;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tester {

    public static int numOfGamesWonIntruder;
    public static ArrayList<String> gamesWon = new ArrayList<>();

    public static void main(String[] args) {

        // change lc and print move count
        for (int i = 1; i < 101; i+=10){

            System.out.println("start for learning cycle " + i);
            QLearning.LEARNING_CYCLES = i;

            numOfGamesWonIntruder = 0;

            for (int j = 0; j < 10; j++) {
                GameController g = new GameController();
                g.startGame();

                // no guards
                g.makeAgentsLearn();

                // with guards
                g.addGuards();
                g.runRaycast();
                g.makeAgentsMoveSmartly();
            }
            String s = String.valueOf(numOfGamesWonIntruder);
            gamesWon.add(s);

            System.out.println("number of games won for lC = " + i +" :" + numOfGamesWonIntruder);

            String[] a = gamesWon.toArray(new String[0]);
        }
    }
}

// 1) experiments on learning cycle:  change learning cycles and check moveCount during learning phase1


// 2) change learning cycles and check win% for 10 games


// 3) change trace length // yell range and check win%


// 4)
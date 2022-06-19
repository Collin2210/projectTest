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
    public static int numOfGamesWonGuard;
    public static ArrayList<String> gamesWon = new ArrayList<>();

    public static void main(String[] args) {

        // change lc and print move count
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

// 1) experiments on learning cycle:  change learning cycles and check moveCount during learning phase1 DONEE

// 2) change learning cycles and check win% for 10 games

// 3) change trace length // yell range and check win%
    // change learning cycles and print win percentage

// 4)
package base;

import Controller.Tile;

import java.util.Arrays;

public class GameEndChecker {

    private static byte timeSpentInGoalByIntruders = 0;

    private static boolean intrudersWin = false;
    private static boolean guardsWin = false;

    public static boolean isInTerminalState(int moveCount) {

        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RESET = "\u001B[0m";

        if (GameEndChecker.intruderInGoalFor3Sec()){
            System.out.println(ANSI_GREEN + moveCount + ANSI_RESET);
            return true;
        }
        if(allIntrudersAreCaught()) {
            System.out.println(ANSI_RED + moveCount + ANSI_RESET);
            return true;
        }
        return false;
    }

    private static boolean intruderInGoalFor3Sec(){
        // check if there is at least an agent in goal area
        boolean atLeastAnIntruderInGoal = false;
        for(Agent a : GameController.agents) {
            if (a.getClass() == Intruder.class) {
                Tile t = GameController.map.getTile(a.getX(), a.getY());
                if(GameController.goalTiles.contains(t))
                    atLeastAnIntruderInGoal = true;
            }
        }

        // if there is 1<= agents in goal, increment time spent
        if(atLeastAnIntruderInGoal)
            timeSpentInGoalByIntruders++;
            // else, bring back clock to 0
        else timeSpentInGoalByIntruders = 0;

        // if agents have spent 3 seconds in goal, end game
        if(timeSpentInGoalByIntruders == 3) {
            intrudersWin = true;
            System.out.println("intruders win");
            GameController.numOfIntruderWins++;
            return true;
        }
        return false;
    }

    /**
     * @return true, if there are no more intruders in Gamecontroller.agents.
     * Indeed, when a guard catches an intruder, said intruder is removed from arraylist.
     */
    private static boolean allIntrudersAreCaught(){
        for(Agent a : GameController.agents){
            if(a.getClass() == Intruder.class) {
                return false;
            }
        }
        System.out.println("guards win");
        return true;
    }
}

package base;

import Controller.Tile;
import QLearning.QLearning;
import java.util.Arrays;

public class GameEndChecker {

    private static byte timeSpentInGoalByIntruders = 0;

    private static boolean intrudersWin = false;
    private static boolean guardsWin = false;

    public static boolean isInLearningTerminalState(){
        // for every agent
        for(Agent a : GameController.agents){
            // if agent is an intruder
            if(a.getClass() == Intruder.class){
                // return true if intruder is on a goal tile
                Tile t = GameController.map.getTile(a.getPosition());
                if(GameController.goalTiles.contains(t)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInTerminalState() {
        if (GameEndChecker.intruderInGoalFor3Sec()){
            Tester.numOfGamesWonIntruder++;
            System.out.println("intruders won");
            return true;
        }
        if(allIntrudersAreCaught()) {
            Tester.numOfGamesWonGuard++;
            System.out.println("Guards won");
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
        for(Intruder i : GameController.Intruders){
            if(!i.isCaught())
                return false;
        }
        return true;
    }
}

package base;

import Controller.Tile;
import QLearning.RewardTable;

public class GameEndChecker {

    private static byte timeSpentInGoalByIntruders = 0;

    private static boolean intrudersWin = false;
    private static boolean guardsWin = false;

    public static boolean isInTerminalState() {
        if (GameEndChecker.intruderInGoalFor3Sec())
            return true;
        if(guardCatchesIntruder())
            return true;
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

    private static boolean guardCatchesIntruder(){
        for(Agent a : GameController.agents){
            if(a.getClass() == Guard.class){
                // find a guard that has been following an agent
                if(((Guard) a).isFollowingAgent()){
                    Intruder intruder = ((Guard) a).getIntruderToCatch();
                    int
                            intruderX = intruder.getX(),
                            intruderY = intruder.getY(),
                            guardX = a.getX(),
                            guardY = a.getY();

                    // get distance between guard and intruder it is chasing
                    double distance = RewardTable.distanceBetweenPoints(guardX,guardY,intruderX,intruderY);
                    // if distance is small enough, end game
                    if(distance <= 1) {
                        System.out.println("guards win");
                        GameController.numOfGuardWins++;
                        guardsWin = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

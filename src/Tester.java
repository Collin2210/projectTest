import base.GameController;
import base.Teleporter;

public class Tester {
    public static void main(String[] args) {

        GameController g = new GameController();

        int[][] goalPos = {{0,4}, {0,3}};
        g.addGoalTiles(goalPos);

        int[][] wallPos = { {2,0}, {2,1}, {2,2}, {2,3}, {2,4}, {3,4}};
        g.addWalls(wallPos);

        int[]
                teleporter = {4,4},
                teleporterDestination = {1,2};
        int angle = 0;
        Teleporter tele = new Teleporter(teleporter,teleporterDestination,angle);

        Teleporter[] teleporters = {tele};
        g.addTeleporters(teleporters);

        int[][] intruders = {{4,0}};
        g.addIntruder(intruders);

        GameController.print();

        g.makeAgentsLearn();

        g.makeAgentsMoveSmartly();

    }
}

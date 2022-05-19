import base.GameController;
import base.Variables;

public class Tester {
    public static void main(String[] args) {

        GameController g = new GameController();

        int[][] goalPos = {{0,Variables.MAP_WIDTH-1}, {0,Variables.MAP_WIDTH-2}};
        g.addGoalTiles(goalPos);

        int[][] wallPos = {};
        g.addWalls(wallPos);

//        int[]
//                teleporter = {4,4},
//                teleporterDestination = {1,2};
//        int angle = 0;
//        Teleporter tele = new Teleporter(teleporter,teleporterDestination,angle);

//        Teleporter[] teleporters = {tele};
//        g.addTeleporters(teleporters);

//        int[][] intruders = {{Variables.MAP_HEIGHT-1,0}, {Variables.MAP_HEIGHT-1,Variables.MAP_HEIGHT-1,Variables.MAP_WIDTH-1}};
//        g.addIntruder(intruders);

        int[][] guards = { {0,0} };
        g.addGuards(guards);

        GameController.print();

        g.makeAgentsLearn();

        g.makeAgentsMoveSmartly();

    }
}

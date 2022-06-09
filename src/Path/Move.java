package Path;

import java.util.*;
import base.*;
import static Path.Position.getFieldCost;

public class Move {
    // 8 possible moves: each is 45° angles
    //convertor: angle to director based on a range

    //Left: -90 ° = -45 x 2
    public void leftMove(Agent Agent){
        Agent.setX(Agent.getX()-1);
    }
    //Right: +90° = +45 x 2
    public void rightMove(Agent Agent){
        Agent.setX(Agent.getX()+1);
    }
    //Up: +0°
    public void upMove(Agent Agent){
        Agent.setY(Agent.getY()+1);
    }
    //Down: -180° = -45 x 4
    public void downMove(Agent Agent){
        Agent.setY(Agent.getY()-1);
    }
    //Diagonal left-Up: +45°
    public void diagonalLeftUpMove(Agent Agent){
        Agent.setX(Agent.getX()-1);
        Agent.setY(Agent.getY()+1);
    }
    //Diagonal Right-Up: -45°
    public void diagonalRightUpMove(Agent Agent){
        Agent.setX(Agent.getX()+1);
        Agent.setY(Agent.getY()+1);
    }
    //Diagonal left-Down: +225 = -45 x 5
    public void diagonalLeftDownMove(Agent Agent){
        Agent.setX(Agent.getX()-1);
        Agent.setY(Agent.getY()-1);
    }
    //Diagonal Right-Down: +135° = +45 x 3
    public void diagonalRightDownMove(Agent Agent){
        Agent.setX(Agent.getX()+1);
        Agent.setY(Agent.getY()-1);
    }

    public boolean canMoveThere(Map map, int x, int y){
        /**
         * returns true if an agent can move to the (x,y) field
         * on the map that is passed to this method
         */

        if(getFieldCost(x,y)==Integer.MAX_VALUE){
            return false;
        }
        else{
            return true;
        }
    }

    public  ArrayList <int[]> legalMoveGenerator(Agent Agent, Map map){

        //call all the above methods

        //check for validity;
        // if output coordinates == MAX_VALUE
        //if another agent is currently on this position

        //return the list of possible tile coordinates to visit
        return new ArrayList<>();

    }

       /* HELPER METHOD FOR A_STAR:
        if we make 1 move in direction of Goal
                the cost of the Move
                        Empty_Cell is 1 : later we will discriminate on
                        Wall is positive_infinity (Integer.MAX_VALUE)
                the remaining distance to Local_Goal
                        Map_Limit
                        TeamMate_Trace
                        Opponent_Trace
        */




    public int[] goalUpdator(Agent Agent, Map map) {
    /* Sort the option from most to less costly
        Need to sort the distance from AgentPosition to all know Map Limits
        Need to sort the distance from AgentPosition to all know Traces
     Outputs the Minimum of both
     */
        return new int[]{};

    }

    // Calculates the Manhattan distance
    public int manhattanHeuristic(int x, int y, int goalX, int goalY){
        return (x - goalX) + (y - goalY);
    }



    /* A_Star : Path Finding Algorithm
     * OUTPUT: an integer that describes how costy(beneficial) the potential Move is to the Agent
     * */

    public int aStar(Agent Agent, Map map, int initialCost){
        int x = Agent.getX();
        int y = Agent.getY();

        int[] newGoal = goalUpdator(Agent, map);

        /*What are the possible Moves to reach the Goal ?
            Candidate Paths

        int goalDistance =  manhattanHeuristic(x, y, newGoal[0], newGoal[1]);

         */
        return 0;
    }


    public static ArrayList<int[]> getPath(Position startPos, Position targetPos, Map map){


        HashMap<Position, Boolean> vis = new HashMap<>();
        HashMap<Position, Position> prev = new HashMap<>();

        List<Position> directions = new LinkedList<>();
        Queue<Position> q = new LinkedList<>();
        Position current = startPos;
        q.add(current);
        vis.put(current, true);
        while(!q.isEmpty()){
            current = q.remove();
            if (current.samePos(targetPos)){
                break;
            }else{
                for(Position node : current.getNeighbours()){
                    boolean contains = false;
                    for (Position oNode : vis.keySet()){
                        if (oNode.samePos(node)){
                            contains = true;
                        }
                    }

                    if(!contains){
                        q.add(node);
                        vis.put(node, true);
                        prev.put(node, current);
                    }
                }
            }
        }
        if (!current.samePos(targetPos)){
            System.out.println("Could not reach");
        }


        for(Position pos = current; pos != null; pos = prev.get(pos)) {
            directions.add(pos);
        }

        Collections.reverse(directions);

        return PosArrToIntArr(directions);
    }

    private static ArrayList<int[]> PosArrToIntArr(List<Position> positions){
        ArrayList<int[]> intArr = new ArrayList<>();

        for(Position p : positions)
            intArr.add(PositionToArray(p));

        return intArr;
    }

    private static int[] PositionToArray(Position p){
        return new int[] {p.getX(), p.getY()};
    }


//public int
//       while(/* Goal not reached, equivalent to Position of Agent != Goal Position */){
//        aStar(agent, map, initalCost);
//    }


    //D*
}
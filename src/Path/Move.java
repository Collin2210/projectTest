package Path;

import base.Agent;
import base.GameController;

import java.lang.Integer;
import java.util.*;

import static Path.Position.getFieldCost;

public class Move {
    // 8 possible moves: each is 45° angles
    //convertor: angle to director based on a range

    //Left: -90 ° = -45 x 2
    public void leftMove(Agent agent){
        agent.setX(agent.getX()-1);
    }
    //Right: +90° = +45 x 2
    public void rightMove(Agent agent){
        agent.setX(agent.getX()+1);
    }
    //Up: +0°
    public void upMove(Agent agent){
        agent.setY(agent.getY()+1);
    }
    //Down: -180° = -45 x 4
    public void downMove(Agent agent){
        agent.setY(agent.getY()-1);
    }
    //Diagonal left-Up: +45°
    public void diagonalLeftUpMove(Agent agent){
        agent.setX(agent.getX()-1);
        agent.setY(agent.getY()+1);
    }
    //Diagonal Right-Up: -45°
    public void diagonalRightUpMove(Agent agent){
        agent.setX(agent.getX()+1);
        agent.setY(agent.getY()+1);
    }
    //Diagonal left-Down: +225 = -45 x 5
    public void diagonalLeftDownMove(Agent agent){
        agent.setX(agent.getX()-1);
        agent.setY(agent.getY()-1);
    }
    //Diagonal Right-Down: +135° = +45 x 3
    public void diagonalRightDownMove(Agent agent){
        agent.setX(agent.getX()+1);
        agent.setY(agent.getY()-1);
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

    public  ArrayList <int[]> legalMoveGenerator(Agent agent, Map map){

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




    public int[] goalUpdator(Agent agent, Map map) {
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
     * OUTPUT: an integer that describes how costly(beneficial) the potential Move is to the Agent
     * */
    public int aStar(Agent agent, Map map){
        int x = agent.getX();
        int y = agent.getY();

        int[] newGoal = goalUpdator(agent, map);

        /*What are the possible Moves to reach the Goal ?
            Candidate Paths
        */

        int goalDistance =  manhattanHeuristic(x, y, newGoal[0], newGoal[1]);

        return 0;
    }

    public static List<Position> getPath(int[] start, int[] target){
        Controller.Map map = GameController.map;

        Position startPos = new Position(start[0], start[1]);
        Position targetPos = new Position(target[0], target[1]);

        int[][] mapMatrix = new int[map.getMapWidth()][map.getMapHeight()];

        for (int i = 0; i < map.getMapWidth(); i++) {
            for (int j = 0; j < map.getMapHeight(); j++) {
                int val = 0; // Nothing
                if (map.hasWall(i, j)){
                    val = 1;
                }
                mapMatrix[i][j] = val;
            }
        }
        return getPath(startPos, targetPos, mapMatrix, false);
    }



    public static List<Position> getPath(Position startPos, Position targetPos, int[][] map){
        return getPath(startPos, targetPos, map, false);
    }



    public static List<Position> getPath(Position startPos, Position targetPos, int[][] map, boolean useEuclidean){

       /* Variables vars = new Variables();
        vars.setHeight(10);
        vars.setWidth(10);
        vars.createWall(1,0, 1, 4);

        Map map = new Map(vars);

        */


        long startTime = System.currentTimeMillis();
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
                Comparator<Position> comparator;
                if (useEuclidean){
                    comparator = new Comparator<Position>() {
                        @Override
                        public int compare(Position o1, Position o2) {
                            return Double.compare(o1.euclideanDistance(targetPos), o2.euclideanDistance(targetPos));
                        }
                    };
                }else{
                    comparator = new Comparator<Position>() {
                        @Override
                        public int compare(Position o1, Position o2) {
                            return Double.compare(o1.manhattanDistance(targetPos), o2.manhattanDistance(targetPos));
                        }
                    };
                }


                List<Position> neighbours = new ArrayList<>(current.getNeighbours(map));
                neighbours.sort(comparator);



                for(Position node : neighbours){
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
            return null;
            //Could not reach.
        }


        for(Position pos = current; pos != null; pos = prev.get(pos)) {
            directions.add(pos);
        }

        Collections.reverse(directions);

        return directions;
    }


    public static List<Position> DFS(Position start, Position targetPos, int[][] map)
    {
        long startTime = System.currentTimeMillis();
        List<Position> visited = new ArrayList<>();
        Stack<Position> stack = new Stack<>();
        List<Position> directions = new LinkedList<>();
        HashMap<Position, Position> prev = new HashMap<>();
        stack.push(start);

        Position lastPos = null;

        boolean found = false;
        while(!stack.empty())
        {

            Position currentPos = stack.peek();
            stack.pop();


            if (currentPos.samePos(targetPos)){
                found = true;
                lastPos = currentPos;
                long took = System.currentTimeMillis() - startTime;
                System.out.println("DFS took "+ took+"ms to complete the path.");
                break;
            }


            boolean alreadyVisited = false;

            for (Position pos : visited){
                if (pos.samePos(currentPos)){
                    alreadyVisited = true;
                }
            }


            if(!alreadyVisited)
            {
                visited.add(currentPos);
            }

            List<Position> neighbours = new ArrayList<>(currentPos.getNeighbours(map, visited));

            Comparator comparator = new Comparator<Position>() {
                @Override
                public int compare(Position o1, Position o2) {
                    return Double.compare(o1.manhattanDistance(targetPos), o2.manhattanDistance(targetPos));
                }
            };

            neighbours.sort(comparator);

            Iterator<Position> itr = neighbours.iterator();


            while (itr.hasNext())
            {
                Position itrPos = itr.next();
                boolean didVisit = false;
                for (Position visPos : visited){
                    if (itrPos.samePos(visPos)){
                        didVisit = true;
                    }
                }
                if (!didVisit){
                    for (Position key : prev.keySet()){
                        if (key.samePos(itrPos)){
                            prev.remove(key);
                            break;
                        }
                    }
                    prev.put(itrPos, currentPos);
                    stack.push(itrPos);
                }
            }

        }

        for(Position pos = lastPos; pos != null; pos = prev.get(pos)) {
            directions.add(pos);
        }
        Collections.reverse(directions);

        if (found)
            return directions;
        return null;
    }

    public static List<Position> properAStar(Position start, Position targetPos, int[][] map){
        long startTime = System.currentTimeMillis();

        PriorityQueue<Position> closedList = new PriorityQueue<>();
        PriorityQueue<Position> openList = new PriorityQueue<>();

        HashMap<Position, Position> parents = new HashMap<>();


        Position[][] fixedPos = new Position[map.length][map[0].length];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                fixedPos[i][j] = new Position(i, j);
            }
        }

        start = fixedPos[start.x][start.y];
        targetPos = fixedPos[targetPos.x][targetPos.y];


        Position end = null;

        //start.f = start.g + start.calculateHeuristic(target);
        openList.add(start);

        while(!openList.isEmpty()){
            Position n = openList.peek();
            //n.weight = n.manhattanDistance(targetPos);
            if(n.samePos(targetPos)){
                end = n;
                break;
                //return n;
            }

            for(Position pos : n.getNeighbours(map)){

                pos = fixedPos[pos.x][pos.y];

                double posWeight = pos.manhattanDistance(targetPos);
                double totalWeight = n.weight + posWeight;
                if(!openList.contains(pos) && !closedList.contains(pos)){
                    parents.put(pos, n);
                    pos.weight = totalWeight;
                    pos.cost = pos.weight + posWeight;
                    openList.add(pos);
                } else {
                    if(totalWeight < pos.weight){
                        parents.put(pos, n);
                        //m.parent = n;
                        pos.weight = totalWeight;
                        pos.cost = pos.weight+posWeight;
                        //m.g = totalWeight;
                        //m.f = m.g + m.calculateHeuristic(target);

                        if(closedList.contains(pos)){
                            closedList.remove(pos);
                            openList.add(pos);
                        }
                    }

                }
            }


            openList.remove(n);
            closedList.add(n);
        }



        if (end == null){
            return null;
        }

        long took = System.currentTimeMillis() - startTime;
        System.out.println("AStar took "+ took+"ms to complete the path.");

        List<Position> path = new ArrayList<>();

        while(parents.containsKey(end)){
            path.add(end);
            //ids.add(n.id);
            end = parents.get(end);
        }
        Collections.reverse(path);
        return path;
    }

//public int
//       while(/* Goal not reached, equivalent to Position of Agent != Goal Position */){
//        aStar(agent, map, initalCost);
//    }


    //D*
}

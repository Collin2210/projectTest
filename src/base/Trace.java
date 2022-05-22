package base;

/* MARKER 1 : Trace aka pheromons
 *       1.0 : WHAT cost is associated (?) should be very tiny or cost free: have a simple implementation
 *       1.1 Type: historical past time information, long distance
 *       1.2  Inspiration <-- the ants communication strategy
 *       1.3 Limits <-- disappears after 10 time steps (EXP PARAMETER)
 *       1.4 Encoded information <-- level alert INTENSITY
 *               level 1. GREEN harvest (add stress aka stimuli )
 *               level 2. ORANGE <-- could need support (potential not yet critical) <-- attract a little
 *               level 3. RED <-- need support <-- attract a lot
 *
 * Remarks: if more than 1 agent leaves a trace on a tile,
 *          then the trace with Highest trace level overwrittes the Tile.
 */

/* TO DO :
 *   Finish writing the method below in connection with the reward table criterias
 * */

import Controller.Map;

import java.util.ArrayList;

public class Trace {
    /* INSTANCE 1: lifeTime
     *   Should be proportional to length so that they evolve in parallel
     *   Should be longer that the vision range to entail this longer distance communication purpose
     */
    private int lifeTime = 10;

    //INSTANCE 2: between 1 and 3: green, orange, red alert level
    private int stress = 0;

    //INSTANCE 3: owner/producer : from which we derive the position
    private Agent agent;

    private int X_co;
    private int Y_co;

    //DEFAULT CONSTRUCTOR
    public Trace(){
        /*stress initialized with zero*/
    }
    //CONSTRUCTOR with VARIABLE
    public Trace(Agent a){
        agent = a;
        lifeTime = 10;
        X_co = agent.getX();
        Y_co = agent.getY();
        AlertLevel(); // assess the stress associated to current situation
    }

    //HELPER METHODS : to modify private instances *******************************************************************

    /* LOCAL LIFETIME TRILOGY: create, update, delete(reset)
     * ATTENTION: outside loop :
     *   Make sure to empty the memory space associated to the Trace
     *   When its lifeTime becomes zero
     */
    public void decreaseLifeTime(){
        lifeTime--;
    }

    public int getLifeTime(){return lifeTime;}

    public int getStress(){return stress;}

    public Agent getOwner(){return agent;}

    public boolean isTeamTrace(Agent a ){
        if(agent/*trace Owner*/.getClass() == a.getClass()){
            return true;
        }
        else
            return false;
    }

    public int getX_co(){ return X_co;}
    public int getY_co(){ return Y_co;}
    /* METHOD 1:  ALERT LEVEL
     *   Compute the agent's stress based on environmental information such as
     *   * The number of opponent within vision range
     *   * The average distance of the opponent to the Agent
     *           NB: larger d <-- reduces stress for an intruder
     *               larger d <-- increases stress for a guard
     * */
    public void AlertLevel() {
        int counter = 0;
        for (int[] tilePos : agent.visionT) {
            for (Agent a : GameController.agents) {
                if (a.getClass() == Intruder.class) {
                    int ax = a.getX(), ay = a.getY();
                    if (ax == tilePos[0] && ay == tilePos[1]) {
                        counter++;
                    }
                }
            }
        }
        this.stress = counter;
    }

    /* METHOD: printTrace
     * Connection between Trace element from Agent and the global info source via Map coordinates
     * Pointer in Tile
     */
    public void printTrace(){
        GameController.map.getTile( agent.getX(),  agent.getY()).setTrace(); //turns the boolean on !
        GameController.map.getTile(agent.getX(), agent.getY()).addTrace(this);//passes the entire object as an argument
    }

    public String toString(){
        return "("+getX_co()+","+getY_co()+")";
    }
}

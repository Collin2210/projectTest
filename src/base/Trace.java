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

public class Trace {
    /* INSTANCE 1: lifeTime
     *   Should be proportional to length so that they evolve in parallel
     *   Should be longer that the vision range to entail this longer distance communication purpose
     */
    private int lifeTime = 10;

    /* INSTANCE 2: length
     * Could be depending on the size of the map
     *  has to grow until this, starts at zero then from 10 to infinite gotta repaint it
     */
    private int length = 10;

    /* INSTANCE 3:  an  array of integer array
     * should store the coordonnates of the Tiles where the trace is located
     */
    private int [][] TracePath = new int[length][2];

    // INSTANCE 4: current position to be filled
    private int stepCounter = 0;

    //INSTANCE 5: between 1 and 3: green, orange, red alert level
    private int stress = 0;

    //INSTANCE 6: owner/producer
    private Agent agent;

    //DEFAULT CONSTRUCTOR
    public Trace(){
        /*stress initialized with zero*/
    }
    //CONSTRUCTOR with VARIABLE
    public Trace(Agent a){
        agent = a;
    }


    //HELPER METHODS : to modify private instances *******************************************************************
    //STEP COUNTER TRILOGY: create, update, delete(reset)
    public void increaseStepCounter() {
        stepCounter++;
    }

    public void resetStepCounter() {
        if (stepCounter == length) {
            stepCounter = 0;
        }// next time step the oldest trace will be Overriden
    }

    /* LOCAL LIFETIME TRILOGY: create, update, delete(reset)
     * ATTENTION: outside loop :
     *   Make sure to empty the memory space associated to the Trace
     *   When its lifeTime becomes zero
     */
    public void decreaseLifeTime(){
        lifeTime--;
    }

    public int getLifeTime(){return lifeTime;}

    public void UpdateLifeTime(int i, int j) {
        for (int u = 0; u < stepCounter; u++) {
            if (TracePath[u][3] > 0) {
                TracePath[u][3]--; //decrese lifeTime by One
            }
        }
    }

    public int getStress(){return stress;}

    public boolean isTeamTrace(Agent a ){
        if(agent/*trace Owner*/.getClass() == a.getClass()){
            return true;
        }
        else
            return false;
    }

    public boolean sameCoordonnates(Agent other){
        if(agent.getX() == other.getX() && agent.getX() == other.getX())
            return true;
        else
            return false;
    }

    /* METHOD 2: UPDATE TRACE . MOST IMPORTANT BECAUSE ONLY CALL INSIDE OF AGENT
     *   Ensures that the agent current position is added to the Trace
     * CALLED AFTER MOVE DECISION !!
     */
    public void UpdateTrace() {
        //connection to AGENT
        TracePath[stepCounter][0] = agent.getX();//Tile coordonnates
        TracePath[stepCounter][1] = agent.getY();
        TracePath[stepCounter][2] = lifeTime;

        //Extract then derive information form the Tile where the Agent is located
        stress = AlertLevel();

        //update main parameters for next time step
        increaseStepCounter(); //general agent steps
        resetStepCounter();

        printTrace();// connection to Tile
    }

    /* METHOD 1:  ALERT LEVEL
     *   Compute the agent's stress based on environmental information such as
     *   * The number of opponent within vision range
     *   * The average distance of the opponent to the Agent
     *           NB: larger d <-- reduces stress for an intruder
     *               larger d <-- increases stress for a guard
     * */
    public int AlertLevel(){
        int AgentCounter = 0;
        for(int i = 0; i < GameController.agents.size(); i++){
            if(sameCoordonnates(agent))
                AgentCounter++;
        }
        if(AgentCounter > 1)
            return 2;
        else
            return AgentCounter;
    }

    /* METHOD: printTrace
     * Connection between Trace element from Agent and the global info source via MapCoordonnates
     * Pointer in Tile
     */
    public void printTrace(){
        GameController.map.getTile( agent.getX(),  agent.getY()).setTrace(); //turns the boolean on !
        GameController.map.getTile(agent.getX(), agent.getY()).addTrace(this);//passes the entire object as an argument
    }
}

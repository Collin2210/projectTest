package base;

public class Tile{

    private int[] position;
    private boolean
            isWall,
            isGoal,
            isYell,
            hasTeleport,
            isATeleportDestination,
            hasTrace; //yes but which one, what stress level? From what agent?

    public Trace AgentTrace;

    public Tile(int x, int y) {
        this.position = new int[]{x, y};
        isWall = false;
        isGoal = false;
        hasTeleport = false;
        isATeleportDestination = false;
        hasTrace = false;
        AgentTrace = new Trace();
        isYell = false;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public void setPosition(int x, int y){
        position[0] = x;
        position[1] = y;
    }

    public void setWall(){
        isWall = true;
    }
    public void setYell(){
        isYell = true;
    }
    public void setGoal(){
        isGoal = true;
    }

    public boolean isAtPosition(int[] position){
        return ( this.position[0] == position[0]
                && this.position[1] == position[1] );
    }

    public boolean isWall() {
        return isWall;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public void setTeleport(){this.hasTeleport = true;}

    public boolean hasTeleport(){return this.hasTeleport;}

    public void setAsTeleportDestination(){this.isATeleportDestination = true;}

    public boolean isATeleportDestination(){return this.isATeleportDestination;}

    public void setTrace(){ hasTrace = true;}

    public void addTrace(Trace mostRecent){
        AgentTrace = mostRecent;
    }
    public Trace getTrace(){ return AgentTrace;}

    public boolean hasTrace(){return hasTrace; }
}
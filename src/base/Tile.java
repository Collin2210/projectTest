package base;

public class Tile{

    private int[] position;
    private boolean
            isWall,
            isGoal,
            isYell,
            isFootsteps;

    public Tile(int x, int y) {
        this.position = new int[]{x, y};
        isWall = false;
        isGoal = false;
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

    public void setGoal(){
        isGoal = true;
    }

    public boolean setYell(){
        isYell=true;
        return isYell;
    }

    public boolean setFootsteps(){
        isFootsteps=true;
        return isFootsteps;
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



    public boolean isYell(){
        return isYell;
    }

    public boolean isFootsteps(){
        return isFootsteps;
    }
}
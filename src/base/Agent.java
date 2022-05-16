package base;

public class Agent {

    private final int[] position;
    private float angle;

    public Agent(int[] position) {
        this.position = position;
        this.angle = (float) Math.toRadians(270);
    }

    public void moveRight(){
        int newX = position[0] + 1;
        if(Map.inMap(newX, position[1]))
            setPosition(newX, position[1]);
    }

    public void moveUp(){
        int newY = position[1] + 1;
        if(Map.inMap(position[0], newY))
            setPosition(position[0], newY);
    }

    public void moveLeft(){
        int newX = position[0] - 1;
        if(Map.inMap(newX, position[1]))
            setPosition(newX, position[1]);
    }

    public void moveDown(){
        int newY = position[1] - 1;
        if(Map.inMap(position[0], newY))
            setPosition(position[0], newY);
    }

    public int[] getPosition(){
        return position;
    }

    public void setPosition(int x, int y){
        position[0] = x;
        position[1] = y;
    }

    public int getX(){
        return position[0];
    }

    public int getY(){
        return position[1];
    }

    public float getAngle(){
        return this.angle;
    }

    public void setAngle(float a){
        this.angle = a;
    }
}

package Visuals.entities;

import Visuals.engine.graphics.models.TexturedModel;
import Visuals.engine.io.Input;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.input.Keyboard;
import org.lwjglx.util.vector.Vector2f;
import org.lwjglx.util.vector.Vector3f;

public class Player extends Entity {

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;

    private float currSpeed = 0;
    private float currTurnSpeed = 0;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int entityID) {
        super(model, position, rotX, rotY, rotZ, scale,entityID);
    }

    public void move(Vector2f newPos, int angle){
        Vector3f newPosition = new Vector3f();
        newPosition.x = newPos.x;
        newPosition.z = newPos.y;
        newPosition.y = 0;
        super.setPosition(newPosition);
        super.setRotY((float)angle);
    }

    public void checkInput(){

        Vector3f newPosition = new Vector3f();
        newPosition.x = this.getPosition().x;
        newPosition.z = this.getPosition().z;
        newPosition.y = 0;

        if(Input.isKeyDown(GLFW.GLFW_KEY_UP)){
            newPosition.z +=1;
        }
        else  if(Input.isKeyDown(GLFW.GLFW_KEY_DOWN)){
            newPosition.z -=1;
        }

        if(Input.isKeyDown(GLFW.GLFW_KEY_RIGHT)){
            newPosition.x += 1;
        }
        else  if(Input.isKeyDown(GLFW.GLFW_KEY_LEFT)){
            newPosition.x -= 1;
            System.out.println("moveeed");
        }

        super.setPosition(newPosition);
    }




}

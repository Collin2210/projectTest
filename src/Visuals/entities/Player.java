package Visuals.entities;

import Visuals.engine.graphics.models.TexturedModel;
import org.lwjglx.util.vector.Vector2f;
import org.lwjglx.util.vector.Vector3f;

public class Player extends Entity {

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int entityID) {
        super(model, position, rotX, rotY, rotZ, scale,entityID);
    }

    public void move(Vector2f newPos){
        Vector3f newPosition = new Vector3f();
        newPosition.x = newPos.x;
        newPosition.z = newPos.y;
        newPosition.y = 0;
        super.setPosition(newPosition);
    }

}

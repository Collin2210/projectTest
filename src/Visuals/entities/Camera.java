package Visuals.entities;

import Visuals.engine.io.Input;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.util.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    private float distanceFromPlayer = 15;
    private float angleAroundPlayer = 90;

    private Vector3f position = new Vector3f(0,30,0);
    private float pitch = 50;
    private float yaw = 0;
    private Player player;

    public Camera(Player player){
        this.player = player;
    }

    public void move(){
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
    }


    private void calculateCameraPosition(float horDistance, float vertDistance){
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horDistance*Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horDistance*Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + vertDistance;
        this.yaw = 180 - (player.getRotY()+angleAroundPlayer);
    }

    private float calculateHorizontalDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom(){
        if(Input.isKeyDown(GLFW_KEY_W)&& distanceFromPlayer<=100){
            distanceFromPlayer += 1;
        }
        else if (Input.isKeyDown(GLFW.GLFW_KEY_S)&& distanceFromPlayer>10){
            distanceFromPlayer -= 1;
        }
    }

    private void calculatePitch(){

        if(Input.isKeyDown(GLFW_KEY_LEFT_SHIFT)){
            double pitchChange = Input.getMouseY() * 0.001;
            pitch -= pitchChange;
        }
        if(Input.isKeyDown(GLFW_KEY_SPACE)){
            double pitchChange = Input.getMouseY() * 0.001;
            pitch += pitchChange;
        }
    }

    private void calculateAngleAroundPlayer(){

        if(Input.isKeyDown(GLFW.GLFW_KEY_A)){
            double angleChange = Input.getMouseX() *0.001;
            angleAroundPlayer -= angleChange;
        }
        if(Input.isKeyDown(GLFW.GLFW_KEY_D)){
            double angleChange = Input.getMouseX() *0.001;
            angleAroundPlayer += angleChange;
        }
    }

    public Vector3f getPosition() {
        return position;
    }
    public float getPitch() {
        return pitch;
    }
    public float getYaw() {
        return yaw;
    }

}
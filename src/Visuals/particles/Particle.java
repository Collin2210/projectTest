package Visuals.particles;


import Visuals.engine.io.Window;
import Visuals.entities.Camera;
import org.lwjglx.util.vector.Vector2f;
import org.lwjglx.util.vector.Vector3f;

public class Particle {

    private Vector3f position;
    private Vector3f velocity;
    private float gravity;
    private float duration;
    private float rotation;
    private float scale;
    private float timeCounter = 0;
    private float playerGravity = -70;

    private ParticleTexture texture;

    public float getDistance() {
        return distance;
    }

    private Vector2f textureOffset1 = new Vector2f();
    private Vector2f textureOffset2 = new Vector2f();
    private float blend;
    private float distance;



    public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravity, float duration, float rotation, float scale) {
        this.position = position;
        this.velocity = velocity;
        this.gravity = gravity;
        this.duration = duration;
        this.rotation = rotation;
        this.scale = scale;
        this.texture = texture;
        ParticleBrain.addParticle(this);
    }

    protected boolean update(Camera camera){
        velocity.y += playerGravity * gravity * Window.getFrameTimeSeconds();
        Vector3f change = new Vector3f(velocity);
        change.scale(Window.getFrameTimeSeconds());
        Vector3f.add(change, position, position);
        distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
        updateTextureCoords();
        timeCounter += Window.getFrameTimeSeconds();
        return timeCounter < duration;
    }

    private void updateTextureCoords(){
        float existance = timeCounter/ duration;
        int stageCount = texture.getNumRows() *texture.getNumRows();
        float atlasProgression = existance * stageCount;
        int index1 = (int) Math.floor(atlasProgression);
        int index2 = index1<stageCount-1 ? index1+ 1 : index1;
        this.blend = atlasProgression%1;

        setTextureOffset(textureOffset1, index1);
        setTextureOffset(textureOffset2,index2);

    }

    private void setTextureOffset(Vector2f offset, int index){
        int col = index % texture.getNumRows();
        int row = index/ texture.getNumRows();
        offset.x = (float) col / texture.getNumRows();
        offset.y = (float) row / texture.getNumRows();
    }






    public Vector2f getTextureOffset1() {
        return textureOffset1;
    }

    public Vector2f getTextureOffset2() {
        return textureOffset2;
    }

    public float getBlend() {
        return blend;
    }

    public ParticleTexture getTexture() {
        return texture;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getTimeCounter() {
        return timeCounter;
    }

    public void setTimeCounter(float timeCounter) {
        this.timeCounter = timeCounter;
    }


}

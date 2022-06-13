package Visuals.shaders.skybox;

import Visuals.engine.io.Window;
import Visuals.entities.Camera;
import Visuals.maths.Maths;
import org.lwjglx.util.vector.Matrix4f;
import org.lwjglx.util.vector.Vector3f;
import Visuals.shaders.ShaderProgram3D;


public class SkyboxShader extends ShaderProgram3D{

    private static final String VERTEX_FILE = "src/Visuals/shaders/skybox/skyboxVertexShader.vert";
    private static final String FRAGMENT_FILE = "src/Visuals/shaders/skybox/skyboxFragmentShader.frag";

    private static final float ROTATE_SPEED = 1f;

    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColour;
    private int location_cubeMap;
    private int location_cubeMap2;
    private int location_blendFactor;

    private float rotation = 0;

    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }


    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }


    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        rotation += ROTATE_SPEED * Window.getFrameTimeSeconds();
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
        super.loadMatrix(location_viewMatrix, matrix);
    }

    public void loadFogColour(float r, float g, float b){
        super.loadVector(location_fogColour, new Vector3f(r,g,b));
    }

    public void connectTextureUnits(){
        super.loadInt(location_cubeMap, 0);
        super.loadInt(location_cubeMap2, 1);
    }


    public void loadBlendFactor(float blend){
        super.loadFloat(location_blendFactor, blend);
    }

// get locations of Uniforms
    @Override
    protected void getAllUniformLocation() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColour = super.getUniformLocation("fogColour");
        location_blendFactor = super.getUniformLocation("blendFactor");
        location_cubeMap = super.getUniformLocation("cubeMap");
        location_cubeMap2 = super.getUniformLocation("cubeMap2");
    }
// binding of attributes
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}

package Visuals.GUI;

import org.lwjglx.util.vector.Matrix4f;
import Visuals.shaders.ShaderProgram3D;

public class GUIShader extends ShaderProgram3D {

    private static final String VERTEX_FILE = "src/Visuals/GUI/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/Visuals/GUI/guiFragmentShader.txt";

    private int location_transformationMatrix;

    public GUIShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocation() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }



}

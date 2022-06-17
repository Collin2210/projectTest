package Visuals.shadows;

import Visuals.shaders.ShaderProgram3D;
import org.lwjglx.util.vector.Matrix4f;


public class ShadowShader extends ShaderProgram3D {
	
	private static final String VERTEX_FILE = "src/Visuals/shadows/shadowVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/Visuals/shadows/shadowFragmentShader.txt";
	
	private int location_mvpMatrix;

	protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocation() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");
		
	}
	
	protected void loadMvpMatrix(Matrix4f mvpMatrix){
		super.loadMatrix(location_mvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
	}

}

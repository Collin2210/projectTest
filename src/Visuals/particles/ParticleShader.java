package Visuals.particles;

import org.lwjglx.util.vector.Matrix4f;

import Visuals.shaders.ShaderProgram3D;
import org.lwjglx.util.vector.Vector2f;

public class ParticleShader extends ShaderProgram3D {

	private static final String VERTEX_FILE = "src/Visuals/particles/particleVShader.txt";
	private static final String FRAGMENT_FILE = "src/Visuals/particles/particleFShader.txt";

	private int location_modelViewMatrix;
	private int location_projectionMatrix;
	private int location_textureOffset1;
	private int location_textureOffset2;
	private int location_textureCoordinateInfo;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocation() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_textureOffset1 = super.getUniformLocation("textureOffset1");
		location_textureOffset2 = super.getUniformLocation("textureOffset2");
		location_textureCoordinateInfo = super.getUniformLocation("textureCoordinateInfo");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	protected void loadTextureCoordinateInfo(Vector2f offset1, Vector2f offset2, float amountRows, float blend){
		super.load2DVector(location_textureOffset1, offset1);
		super.load2DVector(location_textureOffset2, offset2);
		super.load2DVector(location_textureCoordinateInfo, new Vector2f(amountRows, blend));
	}

	protected void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrix(location_modelViewMatrix, modelView);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}

package Visuals.engine.graphics;

import Visuals.engine.graphics.models.RawModel;
import Visuals.engine.graphics.models.TexturedModel;
import Visuals.engine.graphics.textures.ModelTexture;
import Visuals.entities.Entity;
import Visuals.maths.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;
import org.lwjglx.util.vector.Matrix4f;
import Visuals.shaders.StaticShader;

import java.util.List;
import java.util.Map;

public class EntityRenderer {

	private StaticShader shader;

	public EntityRenderer( StaticShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities){
		for(TexturedModel model:entities.keySet()){
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch){
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	public void prepareTexturedModel ( TexturedModel model1){

		RawModel model = model1.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model1.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		GL46.glEnable(GL11.GL_BLEND);
		if (texture.isHasTransparency()){
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(),texture.getReflectivity());
		GL46.glActiveTexture(GL46.GL_TEXTURE0);
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, model1.getTexture().getID());

	}

	private void unbindTexturedModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),entity.getRotY(),entity.getRotZ(),entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(),entity.getTextureYOffset());
	}

}
package Visuals.shaders.skybox;

import Visuals.engine.graphics.Loader;
import Visuals.engine.graphics.models.RawModel;
import Visuals.engine.io.Window;
import Visuals.entities.Camera;
import org.lwjgl.opengl.GL46;
import org.lwjglx.util.vector.Matrix4f;

public class SkyboxRenderer {

    private static final float SIZE = 500f;

    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };


    private static final String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};
    private static final String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};

    private RawModel cube;
    private int texture;
    private int nightTexture;
    private SkyboxShader shader;
    private float time = 0;

    public SkyboxShader getShader() {
        return shader;
    }

    public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix){

        cube = loader.loadToVAO(VERTICES, 3);
        texture = loader.loadCubeMap(TEXTURE_FILES);
        //nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
        shader = new SkyboxShader();
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Camera camera, float r, float g, float b){

        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadFogColour(r,g,b);
        GL46.glDepthMask(false);
        GL46.glDepthRange(0f ,1f);
        GL46.glBindVertexArray(cube.getVaoID());
        GL46.glEnableVertexAttribArray(0);
        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        bindTextures();
        GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, cube.getVertexCount());
        GL46.glDisableVertexAttribArray(0);
        GL46.glBindVertexArray(0);

        GL46.glDepthRange(0f ,1f);
        GL46.glDepthMask(true);
        shader.stop();
    }

    private void bindTextures(){
        time += Window.getFrameTimeSeconds()*1000;
        time%=24000;
        float blendFactor = -((time/1000f)-13)*((time/1000f)-13) * 0.02f + 1;

        if(blendFactor < 0) blendFactor = 0;

        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, texture);
        GL46.glActiveTexture(GL46.GL_TEXTURE1);
        GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, nightTexture);
        shader.loadBlendFactor(blendFactor);
    }

}

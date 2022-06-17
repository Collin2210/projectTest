package Visuals.engine.graphics;

import Visuals.engine.graphics.models.TexturedModel;
import Visuals.entities.Camera;
import Visuals.entities.Entity;
import Visuals.entities.Light;
import Visuals.main.Main;
import Visuals.normalMappingRenderer.NormalMappingRenderer;
import Visuals.shadows.ShadowMapMasterRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;
import org.lwjglx.util.vector.Matrix4f;
import org.lwjglx.util.vector.Vector4f;
import Visuals.shaders.StaticShader;
import Visuals.shaders.terrain.TerrainShader;
import Visuals.shaders.skybox.SkyboxRenderer;
import Visuals.terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 10000000f;

    public static final float RED = 0.5444f;
    public static final float GREEN = 0.62f;
    public static final float BLUE = 0.69f;

    private Matrix4f projectionMatrix;
    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;
    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();
    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();
    private SkyboxRenderer skyboxRenderer;
    private NormalMappingRenderer normalMapRenderer;
    private Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<TexturedModel, List<Entity>>();

    private ShadowMapMasterRenderer shadowMapRenderer;


    public MasterRenderer(Loader loader, Camera camera) {
        enableCulling();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
        normalMapRenderer = new NormalMappingRenderer(projectionMatrix);
        this.shadowMapRenderer = new ShadowMapMasterRenderer(camera);
    }


    public static void enableCulling(){
        glEnable(GL46.GL_CULL_FACE);
        GL46.glCullFace(GL46.GL_BACK);
    }

    public static void disableCulling(){
        GL46.glDisable(GL46.GL_CULL_FACE);
    }

    public void render(List<Light> lights, Camera camera, Vector4f clipPlane){
        prepare();

        shader.start();
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColour(RED,GREEN,BLUE);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);

        renderer.render(entities);



        shader.stop();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        normalMapRenderer.render(normalMapEntities,clipPlane,lights,camera);
        glDisable(GL_BLEND);
        terrainShader.start();
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColour(RED,GREEN,BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        skyboxRenderer.render(camera, RED,GREEN,BLUE);
        terrains.clear();
        entities.clear();
        normalMapEntities.clear();
    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if(batch!=null){
            batch.add(entity);
        } else{
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel,newBatch);
        }
    }

    public void processNormalMapEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = normalMapEntities.get(entityModel);
        if(batch!=null){
            batch.add(entity);
        } else{
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            normalMapEntities.put(entityModel,newBatch);

        }
    }

    public void renderShadowMap(List<Entity> entityList, Light sun){
        for (Entity entity: entityList) {
            processEntity(entity);

            
        }
        shadowMapRenderer.render(entities,sun);
        entities.clear();

    }

    public int getShadowMapTexturex(){
        return shadowMapRenderer.getShadowMap();
    }

    public void cleanUp(){
        shader.cleanUp();
        terrainShader.cleanUp();
        skyboxRenderer.getShader().cleanUp();
        normalMapRenderer.cleanUp();
        shadowMapRenderer.cleanUp();
    }

    public void prepare(){
        glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL46.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(RED, GREEN, BLUE, 1);
    }

    private void createProjectionMatrix(){
        projectionMatrix = new Matrix4f();
        float aspectRatio = (float) Main.WIDTH / (float) Main.HEIGHT;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }


}

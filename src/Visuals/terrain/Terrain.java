package Visuals.terrain;

import Visuals.engine.graphics.Loader;
import Visuals.engine.graphics.models.RawModel;
import Visuals.engine.graphics.textures.TerrainTexture;
import Visuals.engine.graphics.textures.TerrainTexturePack;
import Visuals.maths.Maths;
import org.lwjglx.util.vector.Vector2f;
import org.lwjglx.util.vector.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Terrain {

    public static final float SIZE = 300;
    private static final float MAX_HEIGHT = 1;
    private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;

    private float x;
    private float z;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private float[][] heights;
    HeightGenerator heightGenerator;

    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightmap, String normalMap){
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.x = gridX*SIZE;
        this.z = gridZ*SIZE;
        this.model = generateTerrainMap(loader,heightmap, normalMap);
        heightGenerator = new HeightGenerator(gridX,gridZ, 256, new Random().nextInt(100000000));
    }

    private RawModel generateTerrainMap(Loader loader, String heightMap, String normalMap){

        HeightGenerator heigtGen = new HeightGenerator();


        BufferedImage image = null;
        BufferedImage normalMap2 = null;
        try {
            image = ImageIO.read(new File("res/3D/" +heightMap + ".png"));
            normalMap2 = ImageIO.read(new File("res/3D/" +normalMap + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int VERTEX_COUNT = 0;
        if (image != null) {
            VERTEX_COUNT = image.getHeight();
        }
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;

        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                float height = getHeight(j,i,image, heigtGen);
                heights[j][i] = height;
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormalMap(j,i,normalMap2, heigtGen);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }

        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }


    private Vector3f calculateNormalMap(int x, int z, BufferedImage image, HeightGenerator generator){
        float heightL = getHeight(x-1,z, image,generator);
        float heightR = getHeight(x+1, z, image,generator);
        float heightD = getHeight(x,z-1, image,generator);
        float heightU = getHeight(x, z+1, image,generator);
        Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }


    private float getHeight(int x, int y, BufferedImage image, HeightGenerator generator){
//        if(x<0 || x>=image.getHeight() || y<0 || y>=image.getHeight()){
//            return 0;
//        }
//        float height = image.getRGB(x, y);
//        height += MAX_PIXEL_COLOUR/2f;
//        height /= MAX_PIXEL_COLOUR/2f;
//        height *= MAX_HEIGHT;
//        return height;

        return generator.generateHeight(x,y);

    }

    public float getHeightOfTerrain(float worldX, float worldZ){
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = SIZE/((float)heights.length -1);
        int gridX = (int) Math.floor(terrainX/gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ/gridSquareSize);
        if(gridX >= heights.length -1 || gridZ >= heights.length -1 || gridX < 0 || gridZ < 0){
            return 0;
        }
        float xCoord = (terrainX % gridSquareSize)/gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;
        float answer;
        if(xCoord <= (1-zCoord)){
            answer = Maths
                    .barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ], 0), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));

        } else{
            answer = Maths
                    .barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));

        }
        return answer;

    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

}

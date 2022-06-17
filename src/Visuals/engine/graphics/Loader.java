package Visuals.engine.graphics;

import de.matthiasmann.twl.utils.PNGDecoder;
import Visuals.engine.graphics.models.RawModel;
import Visuals.engine.graphics.textures.TextureData;
import org.lwjgl.opengl.*;
import org.lwjglx.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static de.matthiasmann.twl.utils.PNGDecoder.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.newdawn.slick.opengl.renderer.SGL.*;

public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    public RawModel loadToVAO(float[] positions, float[] textureCoords,float [] normals, int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3, positions);
        storeDataInAttributeList(1,2, textureCoords);
        storeDataInAttributeList(2,3, normals);
        unbindVAO();
        return new RawModel(vaoID,indices.length);
    }

    public RawModel loadToVAO(float[] positions, float[] textureCoords,float [] normals,float[] tangents, int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3, positions);
        storeDataInAttributeList(1,2, textureCoords);
        storeDataInAttributeList(2,3, normals);
        storeDataInAttributeList(3,3, tangents);

        unbindVAO();
        return new RawModel(vaoID,indices.length);
    }

    public RawModel loadToVAO(float[] positions, int dimensions){
        int vaoID = createVAO();
        this.storeDataInAttributeList(0,dimensions,positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length/dimensions);
    }


    public int loadTexture(String fileName) throws IOException {
        Texture texture = null;
        try{

            texture = TextureLoader.getTexture("png", new FileInputStream("res/3D/" + fileName + ".png"));
            GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);
            GL46.glTextureParameteri(GL46.GL_TEXTURE_2D,GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR);
            GL46.glTextureParameterf(GL46.GL_TEXTURE_2D,GL46.GL_MAX_TEXTURE_LOD_BIAS,-2.4f);

        } catch (IOException e){
            e.printStackTrace();
        }
        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }

    public int loadTextureTransparent(String fileName) throws IOException {
        Texture texture = null;

        try{
            glDepthMask(false);
            texture = TextureLoader.getTexture("png", new FileInputStream("res/3D/" + fileName + ".png"));

            GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);

            GL46.glTextureParameteri(GL46.GL_TEXTURE_2D,GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR);
            GL46.glTextureParameterf(GL46.GL_TEXTURE_2D,GL46.GL_MAX_TEXTURE_LOD_BIAS,-2.4f);
            glDepthMask(true);


        } catch (IOException e){
            e.printStackTrace();
        }
        int textureID = texture.getTextureID();
        textures.add(textureID);

        return textureID;
    }

    public void cleanUp(){
        for(int vao:vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo:vbos){
            GL15.glDeleteBuffers(vbo);
        }
        for(int texture:textures){
            GL46.glDeleteTextures(texture);
        }
    }

    public int loadCubeMap(String[] textureFiles){
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

        for (int i = 0; i<textureFiles.length;i++){
            TextureData data = decodeTextureFile("res/3D/" + textureFiles[i] + ".png");
            GL46.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }
        GL46.glTexParameteri(GL46.GL_TEXTURE_CUBE_MAP, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR);
        GL46.glTexParameteri(GL46.GL_TEXTURE_CUBE_MAP, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR);
        textures.add(texID);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
        return texID;
    }

    private TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try{
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e){
            e.printStackTrace();
            System.err.println("Tried to load texture "+ fileName + ", didn't work");
            System.exit(-1);
        }
        return new TextureData(buffer, width, height);
    }


    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber,int coordinateSize, float[] data){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT,false,0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int [] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float [] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}
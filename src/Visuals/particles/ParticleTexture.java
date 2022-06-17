package Visuals.particles;

public class ParticleTexture {
    private int textureID;
    private int numRows;



    public ParticleTexture(int textureID, int numRows) {
        this.textureID = textureID;
        this.numRows = numRows;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }
}

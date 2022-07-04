package Core.Objects.Models;

import IO.Image;

public class Texture {
    private int textureID;
    private Image image;

    public Texture(int textureID, Image image) {
        this.textureID = textureID;
        this.image = image;
    }

    public Texture() {
        this.textureID = -1;
        this.image = null;
    }

    public int getTextureID() {
        return textureID;
    }

    public Image getImage() {
        return image;
    }
}

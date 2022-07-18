package GLEngine.Core.Objects.Models;

import GLEngine.IO.DDS.DDSFile;
import GLEngine.IO.Image;

public class Texture {
    private int textureID;
    private Image image;
    private DDSFile ddsImage;
    public Texture(int textureID, Image image) {
        this.textureID = textureID;
        this.image = image;
    }
    public Texture(int textureID, DDSFile ddsImage) {
        this.textureID = textureID;
        this.ddsImage = ddsImage;
    }

    public Texture() {
        this.textureID = -1;
        this.image = null;
    }

    public Texture(int id){
        this.textureID = id;
    }

    public int getTextureID() {
        return textureID;
    }

    public Image getImage() {
        return image;
    }
    public DDSFile getDdsImage(){ return ddsImage; }
}

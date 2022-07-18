package GLEngine.Core.Objects.Models;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class RenderSettings {
    public boolean wireframe = false;
    public boolean cullFace = true;
    public boolean depthTest = true;
    public int drawMode = GL_TRIANGLES;

    public RenderSettings(){}

    public RenderSettings(boolean wireframe, boolean cullFace, boolean depthTest) {
        this.wireframe = wireframe;
        this.cullFace = cullFace;
        this.depthTest = depthTest;
    }

    public RenderSettings(boolean wireframe, boolean cullFace, boolean depthTest, int drawMode) {
        this.wireframe = wireframe;
        this.cullFace = cullFace;
        this.depthTest = depthTest;
        this.drawMode = drawMode;
    }
}

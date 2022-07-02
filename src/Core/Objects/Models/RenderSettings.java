package Core.Objects.Models;

public class RenderSettings {
    public boolean wireframe = false;
    public boolean cullFace = true;
    public boolean depthTest = true;


    public RenderSettings(){}

    public RenderSettings(boolean wireframe, boolean cullFace, boolean depthTest) {
        this.wireframe = wireframe;
        this.cullFace = cullFace;
        this.depthTest = depthTest;
    }
}

package GLEngine.Core.Objects.Components.Rendering;

import GLEngine.Core.Objects.Components.Component;
import GLEngine.Core.Objects.Models.Mesh;
import GLEngine.Core.Objects.Models.RenderSettings;
import GLEngine.Core.Objects.Models.Texture;
import GLEngine.Core.Shaders.ShaderProgram;
import GLEngine.IO.DDS.DDSFile;
import GLEngine.IO.Image;

public class MeshRenderer extends Component {
    private Mesh mesh;
    private Texture texture;
    private RenderSettings renderSettings = new RenderSettings();
    private ShaderProgram shader;

    public MeshRenderer(Mesh mesh, Image texture) {
        this.mesh = mesh;
        this.texture = new Texture(texture.createTexture(), texture);
    }

    public MeshRenderer(Mesh mesh, DDSFile ddsImage){
        this.mesh = mesh;
        this.texture = new Texture(ddsImage.createTexture(), ddsImage);
    }

    public MeshRenderer(Mesh mesh, Texture texture) {
        this.mesh = mesh;
        this.texture = texture;
    }

    public MeshRenderer(Mesh mesh, int texture) {
        this.mesh = mesh;
        this.texture = new Texture(texture);
    }

    public MeshRenderer(MeshRenderer meshRenderer) {
        this.mesh = new Mesh(meshRenderer.mesh);
        this.texture = new Texture(meshRenderer.texture.getTextureID());
    }

    public MeshRenderer(Image texture){
        this.texture = new Texture(texture.createTexture(), texture);
        this.mesh = null;
    }

    public MeshRenderer(){
        this.texture = new Texture();

        this.mesh = new Mesh();
    }

    public Mesh getMesh(){
        if(mesh == null)
        {
            return new Mesh();
        }
        return mesh;
    }

    public Texture getTexture() {
        return texture;
    }

    public RenderSettings getRenderSettings() {
        return renderSettings;
    }

    public void setRenderSettings(RenderSettings renderSettings) {
        this.renderSettings = renderSettings;
    }

    public ShaderProgram getShader() {
        return shader;
    }

    public void setShader(ShaderProgram shader) {
        this.shader = shader;
    }
}

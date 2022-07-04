package Core.Objects.Components.Rendering;

import Core.Objects.Components.Component;
import Core.Objects.Models.Mesh;
import Core.Objects.Models.RenderSettings;
import Core.Objects.Models.Texture;
import IO.Image;

public class MeshRenderer extends Component {
    private Mesh mesh;
    private Texture texture;
    private RenderSettings renderSettings = new RenderSettings();

    public MeshRenderer(Mesh mesh, Image texture) {
        this.mesh = mesh;
        this.texture = new Texture(texture.createTexture(), texture);
    }

    public MeshRenderer(Mesh mesh, int texture) {
        this.mesh = mesh;
        this.texture = new Texture(texture, null);
    }

    public MeshRenderer(MeshRenderer meshRenderer) {
        this.mesh = new Mesh(meshRenderer.mesh);
        this.texture = meshRenderer.texture;

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
}

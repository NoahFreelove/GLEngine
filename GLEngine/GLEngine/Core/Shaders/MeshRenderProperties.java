package GLEngine.Core.Shaders;

import GLEngine.Core.Interfaces.EditorVisible;
import GLEngine.Core.Objects.Components.Component;

// Only used for GLEE
public class MeshRenderProperties extends Component {
    @EditorVisible
    public String modelPath = "";
    @EditorVisible
    public String texturePath = "";

    public MeshRenderProperties(String modelPath, String texturePath) {
        this.modelPath = modelPath;
        this.texturePath = texturePath;
    }
}

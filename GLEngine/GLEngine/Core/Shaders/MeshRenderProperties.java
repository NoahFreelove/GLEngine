package GLEngine.Core.Shaders;

import GLEngine.Core.Interfaces.EditorVariableAttribute;
import GLEngine.Core.Interfaces.EditorVisible;
import GLEngine.Core.Objects.Components.Component;

// Only used for GLEE
public class MeshRenderProperties extends Component {
    @EditorVisible
    @EditorVariableAttribute(header = "Model and Texture")
    public String modelPath = "";
    @EditorVisible
    public String texturePath = "";

    public MeshRenderProperties(String modelPath, String texturePath) {
        this.modelPath = modelPath;
        this.texturePath = texturePath;
    }
}

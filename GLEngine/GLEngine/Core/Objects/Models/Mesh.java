package GLEngine.Core.Objects.Models;

import GLEngine.IO.OBJ.ModelToBuffer;
import GLEngine.IO.OBJ.ModelBuffer;
import GLEngine.Logging.LogType;
import GLEngine.Logging.Logger;
import org.lwjgl.BufferUtils;

public class Mesh{

    private Model model;
    private ModelBuffer objectBuffer = null;

    public Mesh(){}

    public Mesh(Model model) {
        this.model = model;
        generateBuffers();
    }

    public Mesh(Mesh mesh) {
        this.model = new Model(mesh.model, true);
        generateBuffers();
    }

    public Model getModel() {
        return model;
    }

    public ModelBuffer getObjectBuffer() {
        return objectBuffer;
    }

    private void generateBuffers(){
        if(model == null)
        {
            Logger.log("Model is null", LogType.Debug);
            objectBuffer = new ModelBuffer(BufferUtils.createFloatBuffer(0),BufferUtils.createFloatBuffer(0),BufferUtils.createFloatBuffer(0));
            return;
        }
        objectBuffer = ModelToBuffer.modelToBuffer(model);
    }
}

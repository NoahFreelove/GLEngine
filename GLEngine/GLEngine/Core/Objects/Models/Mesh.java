package GLEngine.Core.Objects.Models;

import GLEngine.IO.OBJ.ModelToBuffer;
import GLEngine.IO.OBJ.ModelBuffer;
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
        this.model = new Model(mesh.model);
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
            System.out.println("Model is null");
            objectBuffer = new ModelBuffer(BufferUtils.createFloatBuffer(0),BufferUtils.createFloatBuffer(0),BufferUtils.createFloatBuffer(0));
            return;
        }
        objectBuffer = ModelToBuffer.modelToBuffer(model);
    }
}

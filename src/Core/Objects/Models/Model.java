package Core.Objects.Models;

import IO.OBJ.Obj;

public class Model {
    public ModelType type = ModelType.OBJ;
    private Obj objModel;

    public Model(Obj objModel){
        type = ModelType.OBJ;
        this.objModel = objModel;
    }

    public Model(Object model){
        if(model instanceof Obj)
        {
            type = ModelType.OBJ;
            this.objModel = (Obj) model;
        }
        else
            System.out.println("Unsupported model type: " + model.getClass().getSimpleName());
    }

    public Object getPrimaryObject(){
        switch (type) {
            case OBJ:
                return objModel;
            default:
                return null;
        }
    }

    public Obj getObjModel(){
        return objModel;
    }
}

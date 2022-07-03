package Core.Objects.Models;

import IO.CustomModels.CustomModel;
import IO.OBJ.Obj;

public class Model {
    public ModelType type = ModelType.OBJ;
    private Obj objModel;
    private CustomModel customModel;

    public Model(Obj objModel){
        type = ModelType.OBJ;
        this.objModel = objModel;
    }

    public Model(CustomModel customModel){
        type = ModelType.CUSTOM;
        this.customModel = customModel;
    }

    public Model(Object model){
        if(model instanceof Obj)
        {
            type = ModelType.OBJ;
            this.objModel = (Obj) model;
        }
        else if(model instanceof CustomModel)
        {
            type = ModelType.CUSTOM;
            this.customModel = (CustomModel) model;
        }
        else
            System.out.println("Unsupported model type: " + model.getClass().getSimpleName());
    }

    public Object getPrimaryObject(){
        return switch (type) {
            case OBJ -> objModel;
            case CUSTOM -> customModel;
            case NONE -> null;
        };
    }

    public Obj getObjModel(){
        return objModel;
    }

    public CustomModel getCustomModel(){
        return customModel;
    }
}

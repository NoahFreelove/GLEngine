package GLEngine.Core.Objects.Components.Physics;

import GLEngine.Core.Objects.Components.Component;
import GLEngine.Core.Objects.GameObject;
import GLEngine.Core.Objects.Models.Model;
import GLEngine.Core.Objects.Models.RenderSettings;
import GLEngine.Core.Worlds.WorldManager;
import GLEngine.IO.CustomModels.Cube;
import GLEngine.IO.Image;
import org.joml.Vector3f;

public class BoundingBox extends Component {
    GameObject boundingBox;
    @Override
    public void ParentAdded() {
        Rigidbody rb = ((Rigidbody)getParent().getComponentByType(Rigidbody.class));
        if (rb == null)
            return;
        Vector3f dimensions = rb.getDimensions();
        if(dimensions == null)
            dimensions = new Vector3f(1,1,1);
        boundingBox = new GameObject(new Vector3f(1,1,1), new Vector3f(0,0,0), new Vector3f(1,1,1), new Model(new Cube(dimensions)), new Image("bin/blue.png"));
        boundingBox.getMeshRenderer().setRenderSettings(new RenderSettings(true, false, true));
        WorldManager.getCurrentWorld().AddGizmo(boundingBox);
    }

    @Override
    public void Update(float deltaTime){
        if(boundingBox !=null){
            boundingBox.setPosition(getParentPosition());
        }
    }
}

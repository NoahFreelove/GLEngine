package Core.Objects.Components.Physics;

import Core.Objects.Components.Component;
import Core.Objects.GameObject;
import Core.Objects.Models.Model;
import Core.Objects.Models.RenderSettings;
import Core.Scenes.WorldManager;
import IO.CustomModels.Cube;
import IO.Image;
import org.joml.Vector3f;

public class BoundingBox extends Component {
    GameObject boundingBox;
    @Override
    public void OnAdded() {
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
        boundingBox.setPosition(getParentPosition());
    }
}

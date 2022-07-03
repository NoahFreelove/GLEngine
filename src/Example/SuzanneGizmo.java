package Example;

import Core.Objects.Components.Component;
import Core.Objects.Components.Physics.Rigidbody;
import Core.Objects.GameObject;
import Core.Objects.Models.Model;
import Core.Objects.Models.RenderSettings;
import Core.Scenes.WorldManager;
import IO.CustomModels.BoundingBox;
import IO.Image;
import org.joml.Vector3f;

public class SuzanneGizmo extends Component {
    GameObject boundingBox;
    @Override
    public void OnAdded() {
        Vector3f dimensions = ((Rigidbody)getParent().getComponentByType(Rigidbody.class)).getDimensions();
        if(dimensions == null)
            dimensions = new Vector3f(1,1,1);
        boundingBox = new GameObject(new Vector3f(1,1,1), new Vector3f(0,0,0), new Vector3f(1,1,1), new Model(new BoundingBox(dimensions)), new Image("bin/blue.png"));
        boundingBox.getMeshRenderer().setRenderSettings(new RenderSettings(true, false, true));
        WorldManager.getCurrentWorld().AddGizmo(boundingBox);
    }

    @Override
    public void Update(){
        boundingBox.setPosition(getParentPosition());
    }
}

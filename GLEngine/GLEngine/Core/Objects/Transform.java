package GLEngine.Core.Objects;

import GLEngine.Core.Interfaces.EditorVisible;
import GLEngine.Core.Objects.Components.Component;
import org.joml.Vector3f;

public class Transform extends Component {
    @EditorVisible
    private Vector3f position = new Vector3f(0,0,0);
    @EditorVisible
    private Vector3f rotation = new Vector3f(0,0,0);
    @EditorVisible
    private Vector3f scale = new Vector3f(1,1,1);

    public Transform(){}

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}

package Example;

import Core.Objects.Components.Component;
import Core.Objects.Components.Physics.Rigidbody;

import javax.vecmath.Vector3f;

public class SuzanneController extends Component {
    public final Rigidbody rb;

    public SuzanneController(Rigidbody rb)
    {
        this.rb = rb;
    }

    public void Move(float x, float y, float z) {
        rb.getRigidBody().applyCentralForce(new Vector3f(x, y, z));
    }
}

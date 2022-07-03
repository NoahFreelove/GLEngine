package Core.Objects.Components.Colliders;

import com.bulletphysics.collision.shapes.BoxShape;
import org.joml.Vector3f;

public class BoxCollider extends Collider {

    public BoxCollider(Vector3f position, Vector3f dimensions) {
        super(position);
        object.setCollisionShape(new BoxShape(new javax.vecmath.Vector3f(dimensions.x, dimensions.y, dimensions.z)));
    }
}


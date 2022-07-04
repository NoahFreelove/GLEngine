package Core.Objects.Components.Colliders;

import com.bulletphysics.collision.shapes.SphereShape;
import org.joml.Vector3f;

public class SphereCollider extends Collider{
    public SphereCollider(Vector3f position, float radius) {
        super(position);
        object.setCollisionShape(new SphereShape(radius));
    }
}

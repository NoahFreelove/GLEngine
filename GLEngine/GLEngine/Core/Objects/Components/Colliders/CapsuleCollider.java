package GLEngine.Core.Objects.Components.Colliders;

import GLEngine.Core.Interfaces.EditorVisible;
import com.bulletphysics.collision.shapes.CapsuleShape;

public class CapsuleCollider extends Collider{

    @EditorVisible
    private float radius;
    @EditorVisible
    private float height;

    public CapsuleCollider(float radius, float height) {
        super();
        object.setCollisionShape(new CapsuleShape(radius,height));
    }

    public CapsuleCollider() {
        super();
        object.setCollisionShape(new CapsuleShape(1,1));
    }

    @Override
    public void OnCreated(){
        object.setCollisionShape(new CapsuleShape(radius,height));
        super.OnCreated();
    }
}

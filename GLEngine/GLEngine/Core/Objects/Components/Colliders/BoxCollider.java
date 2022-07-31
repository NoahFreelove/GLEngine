package GLEngine.Core.Objects.Components.Colliders;

import GLEngine.Core.Interfaces.EditorVisible;
import GLEngine.Core.Objects.GameObject;
import GLEngine.Core.Worlds.WorldManager;
import com.bulletphysics.collision.shapes.BoxShape;
import org.joml.Vector3f;

public class BoxCollider extends Collider {

    @EditorVisible
    public Vector3f dimensions = new Vector3f();

    @EditorVisible
    private float friction = 1f;

    public BoxCollider(){
        super();
    }

    public BoxCollider(Vector3f position, Vector3f dimensions) {
        super(position, new Vector3f(0,0,0));
        object.setCollisionShape(new BoxShape(new javax.vecmath.Vector3f(dimensions.x, dimensions.y, dimensions.z)));
    }
    public BoxCollider(Vector3f position, Vector3f dimensions, Vector3f rot) {
        super(position, rot);
        object.setCollisionShape(new BoxShape(new javax.vecmath.Vector3f(dimensions.x, dimensions.y, dimensions.z)));
        this.dimensions = dimensions;
    }

    @Override
    public void OnCreated(){
        position = getParentPosition();
        object.setCollisionShape(new BoxShape(new javax.vecmath.Vector3f(dimensions.x, dimensions.y, dimensions.z)));
        object.setFriction(friction);
        super.OnCreated();

    }

    public static BoxCollider GenerateBoxColliderForObject(GameObject gameObject){
        BoxCollider bc = new BoxCollider(gameObject.getPosition(), gameObject.getScale(), gameObject.getRotation());
        return bc;
    }
}


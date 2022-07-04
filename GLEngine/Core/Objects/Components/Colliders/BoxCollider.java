package Core.Objects.Components.Colliders;

import Core.Objects.GameObject;
import com.bulletphysics.collision.shapes.BoxShape;
import org.joml.Vector3f;

public class BoxCollider extends Collider {

    public BoxCollider(Vector3f position, Vector3f dimensions) {
        super(position);
        object.setCollisionShape(new BoxShape(new javax.vecmath.Vector3f(dimensions.x, dimensions.y, dimensions.z)));
    }
    public BoxCollider(Vector3f position, Vector3f dimensions, boolean wall) {
        super(position);
        if(wall)
            object.setCollisionShape(new BoxShape(new javax.vecmath.Vector3f(dimensions.x-0.2f, dimensions.y, dimensions.z-0.2f)));
        else  object.setCollisionShape(new BoxShape(new javax.vecmath.Vector3f(dimensions.x, dimensions.y, dimensions.z)));
    }



    public static BoxCollider GenerateBoxColliderForObject(GameObject gameObject, boolean isWall){
        BoxCollider bc = new BoxCollider(gameObject.getPosition(), gameObject.getScale(), isWall);
        return bc;
    }
}


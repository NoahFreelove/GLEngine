package Core.Objects.Components.Colliders;

import Core.Objects.Components.Component;
import Core.Scenes.WorldManager;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.linearmath.Transform;
import org.joml.Vector3f;

import javax.vecmath.Matrix4f;

public class BoxCollider extends Component {
    CollisionObject box;

    public BoxCollider(Vector3f position, Vector3f dimensions) {
        box = new CollisionObject();
        box.setCollisionShape(new BoxShape(new javax.vecmath.Vector3f(dimensions.x, dimensions.y, dimensions.z)));
        Matrix4f positionMatrix = new Matrix4f();
        positionMatrix.setIdentity();
        positionMatrix.setTranslation(new javax.vecmath.Vector3f(position.x, position.y, position.z));
        box.setWorldTransform(new Transform(positionMatrix));
    }


    @Override
    public void ParentAdded() {
        WorldManager.getCurrentWorld().addCollider(box);
    }
}


package Core.Objects.Components.Colliders;

import Core.Objects.Components.Component;
import Core.Scenes.WorldManager;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.linearmath.Transform;
import org.joml.Vector3f;

import javax.vecmath.Matrix4f;

public class Collider extends Component {
    protected CollisionObject object;

    public Collider(Vector3f position){
        object = new CollisionObject();
        Matrix4f positionMatrix = new Matrix4f();
        positionMatrix.setIdentity();
        positionMatrix.setTranslation(new javax.vecmath.Vector3f(position.x, position.y, position.z));
        object.setWorldTransform(new Transform(positionMatrix));
    }

    @Override
    public void ParentAdded() {
        WorldManager.getCurrentWorld().addCollider(object);
    }
}

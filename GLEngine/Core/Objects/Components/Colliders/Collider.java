package Core.Objects.Components.Colliders;

import Core.Objects.Components.Component;
import Core.Worlds.WorldManager;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.linearmath.Transform;
import org.joml.Vector3f;

import javax.vecmath.Matrix4f;

public class Collider extends Component {
    protected CollisionObject object;

    public Collider(Vector3f position){
        object = new CollisionObject();
        object.activate(true);

        Matrix4f positionMatrix = new Matrix4f();
        positionMatrix.setIdentity();
        positionMatrix.setTranslation(new javax.vecmath.Vector3f(position.x, position.y, position.z));
        object.setWorldTransform(new Transform(positionMatrix));
    }

    @Override
    public void ParentAdded() {
        WorldManager.getCurrentWorld().addCollider(object);

    }

    @Override
    public void ParentTransformed(Vector3f newPos, Vector3f newRot, Vector3f newScale) {
        Matrix4f positionMatrix = new Matrix4f();
        positionMatrix.setIdentity();
        positionMatrix.setTranslation(new javax.vecmath.Vector3f(newPos.x, newPos.y, newPos.z));
        positionMatrix.setScale(newScale.x());
        object.setWorldTransform(new Transform(positionMatrix));
    }
}

package GLEngine.Core.Objects.Components.Colliders;

import GLEngine.Core.Interfaces.EditorVisible;
import GLEngine.Core.Objects.Components.Component;
import GLEngine.Core.Worlds.HashObject;
import GLEngine.Core.Worlds.WorldManager;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.linearmath.Transform;
import org.joml.Vector3f;

import javax.vecmath.Matrix4f;

public class Collider extends Component {
    public CollisionObject object;

    @EditorVisible
    protected Vector3f position = new Vector3f();

    public Collider() {
        this.object = new CollisionObject();
    }

    public Collider(Vector3f position, Vector3f rot){
        object = new CollisionObject();
        object.activate(true);
        this.position = position;
        Matrix4f positionMatrix = new Matrix4f();
        positionMatrix.setIdentity();
        positionMatrix.setTranslation(new javax.vecmath.Vector3f(position.x, position.y, position.z));

        position.rotateX((float) Math.toRadians(rot.x()));
        position.rotateY((float) Math.toRadians(rot.y()));
        position.rotateZ((float) Math.toRadians(rot.z()));

        object.setWorldTransform(new Transform(positionMatrix));
    }

    @Override
    public void OnCreated() {
        WorldManager.getLoadingWorld().addCollider(object);
        WorldManager.getLoadingWorld().RegisterCollider(new HashObject(getParent(), object.getCollisionShape().hashCode()));
        setPosition(position);
    }

    @Override
    public void ParentTransformed(Vector3f newPos, Vector3f newRot, Vector3f newScale) {
        transform(newPos, newRot, newScale);
    }

    public void setPosition(Vector3f position){
        Transform T =  new Transform();
        object.getWorldTransform(T);
        T.origin.set(position.x(), position.y(), position.z());
        transform(T);
        this.position = new Vector3f(position);
    }

    public void transform(Transform T){
        object.setWorldTransform(T);
    }

    public void transform(Vector3f newPos, Vector3f newRot, Vector3f newScale){
        Matrix4f positionMatrix = new Matrix4f();
        positionMatrix.setIdentity();
        positionMatrix.setTranslation(new javax.vecmath.Vector3f(newPos.x, newPos.y, newPos.z));
        // rotate the matrix
        positionMatrix.rotX((float) Math.toRadians(newRot.x()));
        positionMatrix.rotY((float) Math.toRadians(newRot.y()));
        positionMatrix.rotZ((float) Math.toRadians(newRot.z()));
        // scale the matrix
        positionMatrix.m00 *= newScale.x();
        positionMatrix.m01 *= newScale.x();
        positionMatrix.m02 *= newScale.x();
        positionMatrix.m10 *= newScale.y();
        positionMatrix.m11 *= newScale.y();
        positionMatrix.m12 *= newScale.y();
        positionMatrix.m20 *= newScale.z();
        positionMatrix.m21 *= newScale.z();
        positionMatrix.m22 *= newScale.z();

        object.setWorldTransform(new Transform(positionMatrix));
    }
}

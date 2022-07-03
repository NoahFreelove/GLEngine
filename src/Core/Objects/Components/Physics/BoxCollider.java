package Core.Objects.Components.Physics;

import com.bulletphysics.collision.broadphase.BroadphaseNativeType;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Vector3f;

public class BoxCollider extends CollisionShape {
    @Override
    public void getAabb(Transform transform, Vector3f vector3f, Vector3f vector3f1) {

    }

    @Override
    public BroadphaseNativeType getShapeType() {
        return null;
    }

    @Override
    public void setLocalScaling(Vector3f vector3f) {

    }

    @Override
    public Vector3f getLocalScaling(Vector3f vector3f) {
        return null;
    }

    @Override
    public void calculateLocalInertia(float v, Vector3f vector3f) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setMargin(float v) {

    }

    @Override
    public float getMargin() {
        return 0;
    }
}

package GLEngine.Core.Objects.Components.Colliders;

import GLEngine.Core.Objects.Components.Component;
import GLEngine.Core.Worlds.WorldManager;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * An infinite plane?
 */
public class Plane extends Component {
    Vector3f pos;
    RigidBody rb;
    public Plane(org.joml.Vector3f initPos){
        this.pos = new Vector3f(initPos.x(), initPos.y(), initPos.z());
    }

    @Override
    public void OnAdded() {

        CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 0.25f);
        MotionState groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(
                new Quat4f(0, 0, 0, 1),
                new Vector3f(0, 0, 0), 1.0f)));

        RigidBodyConstructionInfo groundBodyConstructionInfo = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new Vector3f(0, 0, 0));

        rb = new RigidBody(groundBodyConstructionInfo);
        rb.setWorldTransform(new Transform(new Matrix4f(
                new Quat4f(0, 0, 0, 1),
                pos, 1.0f)));
    }
    @Override
    public void OnCreated(){
        WorldManager.getCurrentWorld().addRigidBody(rb);
    }
}

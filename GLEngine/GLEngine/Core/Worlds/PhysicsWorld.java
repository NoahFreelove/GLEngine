package GLEngine.Core.Worlds;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

import javax.vecmath.Vector3f;

public class PhysicsWorld {
    private DynamicsWorld physicsWorldObject;

    public PhysicsWorld(){
        BroadphaseInterface broadphase = new DbvtBroadphase();
        CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
        Dispatcher dispatcher = new CollisionDispatcher(collisionConfig);
        ConstraintSolver solver = new SequentialImpulseConstraintSolver();
        physicsWorldObject = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
        physicsWorldObject.setGravity(new Vector3f(0,-9.8f, 0));
    }

    public DynamicsWorld getPhysicsWorldObject() {
        return physicsWorldObject;
    }
}

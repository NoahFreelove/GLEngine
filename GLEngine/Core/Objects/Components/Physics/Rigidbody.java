package Core.Objects.Components.Physics;

import Core.Objects.Components.Component;
import Core.Worlds.HashObject;
import Core.Worlds.WorldManager;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class Rigidbody extends Component {
    private RigidBody rigidBody;
    private Vector3f dimensions;
    private CollisionShape colliderShape;
    private float mass = 1f;
    private float stepThreshold = 0.3f;

    public Rigidbody(org.joml.Vector3f colliderDimensions) {
        this.dimensions = new Vector3f(colliderDimensions.x(), colliderDimensions.y(), colliderDimensions.z());
        colliderShape = new BoxShape(dimensions);
    }

    public Rigidbody(org.joml.Vector3f colliderDimensions, CollisionShape collider) {
        this.dimensions = new Vector3f(colliderDimensions.x(), colliderDimensions.y(), colliderDimensions.z());
        this.colliderShape = collider;
    }

    public Rigidbody(org.joml.Vector3f colliderDimensions, CollisionShape collider, float mass) {
        this.dimensions = new Vector3f(colliderDimensions.x(), colliderDimensions.y(), colliderDimensions.z());
        this.colliderShape = collider;
        this.mass = mass;
    }

    @Override
    public void OnAdded(){
        rigidBody = new RigidBody(mass, new DefaultMotionState(), colliderShape);

        WorldManager.getCurrentWorld().RegisterCollider(new HashObject(getParent(), rigidBody.getCollisionShape().hashCode()));

        Vector3f position = new Vector3f(getParentPosition().x(), getParentPosition().y(), getParentPosition().z());

        Matrix4f transform = new Matrix4f();

        transform.setIdentity();
        transform.setTranslation(position);
        rigidBody.setWorldTransform(new Transform(transform));
    }

    @Override
    public void ParentAdded(){
        WorldManager.getCurrentWorld().getPhysicsWorld().getPhysicsWorldObject().addRigidBody(rigidBody);
    }

    @Override
    public void Update(float deltaTime){
        if(!isActive())
            return;
        rigidBody.activate();
        Transform transform = new Transform();
        rigidBody.getWorldTransform(transform);
        setParentPosition(new org.joml.Vector3f(transform.origin.x,transform.origin.y,transform.origin.z));
    }
    public org.joml.Vector3f getDimensions(){
        return new org.joml.Vector3f(dimensions.x, dimensions.y, dimensions.z);
    }

    public RigidBody getRigidBody() {
        return rigidBody;
    }

    public void disableGravity(){
        rigidBody.setGravity(new Vector3f(0,0,0));
    }

    public void setPosition(org.joml.Vector3f newPos) {
        Vector3f position = new Vector3f(newPos.x(), newPos.y(), newPos.z());
        Matrix4f transform = new Matrix4f();

        transform.setIdentity();
        transform.setTranslation(position);
        rigidBody.setWorldTransform(new Transform(transform));
    }


    public boolean StepRaycast(){
        boolean isFalling;
        // Floor case
        Vector3f fromPos = new Vector3f(getParentPosition().x(),getParentPosition().y()- getParentScale().y() ,getParentPosition().z());
        Vector3f dir = new Vector3f();
        rigidBody.getLinearVelocity(dir);
        isFalling = (Math.abs(dir.y)>0.000001);
        dir.normalize();
        org.joml.Vector3f toPos = new org.joml.Vector3f(dir.x,dir.y,dir.z);
        toPos.mul(1f);
        javax.vecmath.Vector3f finalVec =new javax.vecmath.Vector3f(fromPos.x + toPos.x(), fromPos.y +toPos.y(), fromPos.z +toPos.z());
        CollisionWorld.ClosestRayResultCallback res = new CollisionWorld.ClosestRayResultCallback(fromPos,finalVec);
        WorldManager.getCurrentWorld().getPhysicsWorld().getPhysicsWorldObject().rayTest(fromPos,finalVec,res);

        // Threshold cast
        fromPos = new Vector3f(getParentPosition().x(),getParentPosition().y()- getParentScale().y()+stepThreshold ,getParentPosition().z());
        dir = new Vector3f();
        rigidBody.getLinearVelocity(dir);
        dir.normalize();
        toPos = new org.joml.Vector3f(dir.x,dir.y,dir.z);
        toPos.mul(1f);
         finalVec =new javax.vecmath.Vector3f(fromPos.x + toPos.x(), fromPos.y +toPos.y(), fromPos.z +toPos.z());
        CollisionWorld.ClosestRayResultCallback res2 = new CollisionWorld.ClosestRayResultCallback(fromPos,finalVec);
        WorldManager.getCurrentWorld().getPhysicsWorld().getPhysicsWorldObject().rayTest(fromPos,finalVec,res2);

        return (res.hasHit()) && !res2.hasHit() && !isFalling;
    }

    public void Step(){
        setPosition(new org.joml.Vector3f(getParentPosition().x, getParentPosition().y+stepThreshold, getParentPosition().z));
    }
}


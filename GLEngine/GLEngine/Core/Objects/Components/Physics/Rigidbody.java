package GLEngine.Core.Objects.Components.Physics;

import GLEngine.Core.Objects.Components.Component;
import GLEngine.Core.Worlds.HashObject;
import GLEngine.Core.Worlds.WorldManager;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import org.joml.Quaternionf;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class Rigidbody extends Component {
    private RigidBody rigidBody;
    private Vector3f dimensions;
    private CollisionShape colliderShape;
    private float mass = 1f;
    protected float stepThreshold = 0.5f;

    public Rigidbody(org.joml.Vector3f colliderDimensions) {
        this.dimensions = new Vector3f(colliderDimensions.x(), colliderDimensions.y(), colliderDimensions.z());
        colliderShape = new BoxShape(dimensions);
    }

    public Rigidbody(org.joml.Vector3f colliderDimensions, float mass) {
        this.dimensions = new Vector3f(colliderDimensions.x(), colliderDimensions.y(), colliderDimensions.z());
        colliderShape = new BoxShape(dimensions);
        this.mass = mass;
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

    public void setRotation(Quaternionf rot){
        Transform t = new Transform();
        getRigidBody().getWorldTransform(t);
        t.setRotation(new Quat4f(rot.x(), rot.y(), rot.z(), rot.w()));
    }

    public void setMass(float newMass){
        this.mass = newMass;
        rigidBody.setMassProps(newMass, new Vector3f());
    }


    public boolean StepRaycast(){
        boolean isFalling;
        Vector3f fromPos = new Vector3f(getParentPosition().x(), (getParentPosition().y()-dimensions.y),getParentPosition().z());
        //System.out.println(fromPos.y);


        Vector3f dir = new Vector3f();
        rigidBody.getLinearVelocity(dir);

        isFalling = (Math.abs(dir.y)>0.01);
        dir.normalize();
        org.joml.Vector3f toPos = new org.joml.Vector3f(dir.x,dir.y,dir.z);
        toPos.mul(2f);
        javax.vecmath.Vector3f finalVec =new javax.vecmath.Vector3f(fromPos.x + toPos.x(), fromPos.y, fromPos.z +toPos.z());
        CollisionWorld.ClosestRayResultCallback res = new CollisionWorld.ClosestRayResultCallback(fromPos,finalVec);

        WorldManager.getCurrentWorld().getPhysicsWorld().getPhysicsWorldObject().rayTest(fromPos,finalVec,res);

        // Threshold cast
        fromPos.y += stepThreshold;
        dir = new Vector3f();
        rigidBody.getLinearVelocity(dir);
        dir.normalize();
        toPos = new org.joml.Vector3f(dir.x,dir.y,dir.z);
        toPos.mul(2f);

        finalVec = new javax.vecmath.Vector3f(fromPos.x + toPos.x(), fromPos.y +stepThreshold, fromPos.z +toPos.z());


        CollisionWorld.ClosestRayResultCallback res2 = new CollisionWorld.ClosestRayResultCallback(fromPos,finalVec);
        WorldManager.getCurrentWorld().getPhysicsWorld().getPhysicsWorldObject().rayTest(fromPos,finalVec,res2);

        //System.out.println("Is Falling:" + isFalling);
        //System.out.println("res: " + res.hasHit());
        //System.out.println("res2: " + res2.hasHit());

        if(res.hasHit() && res2.hasHit()){
            Vector3f velocity = new Vector3f();
            rigidBody.getLinearVelocity(velocity);
            rigidBody.setLinearVelocity(new Vector3f(velocity.x, -9.8f, velocity.z));
        }

        //System.out.println(res.hasHit() + " : " + res2.hasHit());

        return (res.hasHit()) && !res2.hasHit() && !isFalling;
    }

    public void Step(){
        setPosition(new org.joml.Vector3f(getParentPosition().x, getParentPosition().y+stepThreshold+0.2f, getParentPosition().z));
    }

    public void setStepThreshold(float stepThreshold) {
        this.stepThreshold = stepThreshold;
    }
}

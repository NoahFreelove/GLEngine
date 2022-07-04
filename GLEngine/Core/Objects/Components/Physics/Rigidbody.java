package Core.Objects.Components.Physics;

import Core.Objects.Components.Component;
import Core.Worlds.WorldManager;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class Rigidbody extends Component {
    private RigidBody rigidBody;
    private Vector3f dimensions;
    private CollisionShape colliderShape;
    private float mass = 1f;
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
}


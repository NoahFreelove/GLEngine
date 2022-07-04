package Example;

import Core.Input.Input;
import Core.Input.KeyEvent;
import Core.Objects.Components.Component;
import Core.Objects.Components.Physics.Rigidbody;
import Core.Objects.Components.Rendering.Camera;
import Core.Objects.GameObject;
import Core.Worlds.WorldManager;
import Core.Window;
import com.bulletphysics.collision.dispatch.CollisionWorld;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class SuzanneController extends Component {
    public final Rigidbody rb;
    private final Matrix4f suzanneOrigin = new Matrix4f();
    private final long window;

    private Camera cam1;
    private Camera cam2;

    private float suzanneSpeed = 10;
    private GameObject cameraModel;

    private boolean canJump = false;

    public SuzanneController(Rigidbody rb, Camera cam1, Camera cam2, GameObject cameraModel)
    {
        this.cameraModel = cameraModel;
        this.rb = rb;
        suzanneOrigin.setTranslation(new javax.vecmath.Vector3f(0,2,0));
        window = Window.CreateWindow().getWindowHandle();
        this.cam1 = cam1;
        this.cam2 = cam2;

        cam2.addHorizAngle((float) Math.toRadians(180));
    }

    public void SetVelocity(Vector3f velocity){
        rb.getRigidBody().setLinearVelocity(velocity);
    }

    @Override
    public void Update(float deltaTime){
        Vector3f currVelocity = new Vector3f();
        rb.getRigidBody().getLinearVelocity(currVelocity);

        Vector3f fromPos = new Vector3f(getParentPosition().x(),getParentPosition().y(),getParentPosition().z());
        Vector3f toPos = new Vector3f(getParentPosition().x(),getParentPosition().y()-1,getParentPosition().z());

        CollisionWorld.ClosestRayResultCallback res = new CollisionWorld.ClosestRayResultCallback(fromPos,toPos);
        WorldManager.getCurrentWorld().getPhysicsWorld().getPhysicsWorldObject().rayTest(fromPos,toPos,res);

        if(res.hasHit()){
            canJump = true;
        }

        Vector3f velocity = new Vector3f(0,currVelocity.y,0);
        if (Input.isKeyPressed(GLFW_KEY_UP)){
            velocity.z = -suzanneSpeed;
        }
        else if (Input.isKeyPressed(GLFW_KEY_DOWN)){
            velocity.z = suzanneSpeed;
        }
        else velocity.z = 0;

        if (Input.isKeyPressed(GLFW_KEY_LEFT)){
            velocity.x = -suzanneSpeed;
        }
        else if (Input.isKeyPressed(GLFW_KEY_RIGHT)){
            velocity.x = suzanneSpeed;
        }
        else velocity.x = 0;

        if(Input.isKeyPressed(GLFW_KEY_RIGHT_SHIFT))
            Jump();

        SetVelocity(velocity);

        if (glfwGetKey( window, GLFW_KEY_R ) == GLFW_PRESS){
            SuzanneExample.suzanneController.rb.setPosition(new org.joml.Vector3f(0,2,0));
            SetVelocity(new Vector3f(0,0,0));
        }

        if (Input.isKeyPressed(GLFW_KEY_F1)){
            WorldManager.setEnableGizmos(false);
        }
        if (Input.isKeyPressed(GLFW_KEY_F2)){
            WorldManager.setEnableGizmos(true);
        }

        if (Input.isKeyPressed(GLFW_KEY_F3)){
            Window.GetInstance().setActiveCamera(cam1);
            cameraModel.getMeshRenderer().setActive(false);
        }

        if (Input.isKeyPressed(GLFW_KEY_F4)){
            Window.GetInstance().setActiveCamera(cam2);
            cameraModel.getMeshRenderer().setActive(true);
        }
    }

    private void Jump(){
        if(canJump)
        {
            rb.getRigidBody().applyCentralForce(new Vector3f(0, suzanneSpeed*50, 0));
            canJump = false;
        }
    }
}

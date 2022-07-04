package Example;

import Core.Objects.Components.Component;
import Core.Objects.Components.Physics.Rigidbody;
import Core.Objects.Components.Rendering.Camera;
import Core.Objects.GameObject;
import Core.Worlds.WorldManager;
import Core.Window;

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

    public SuzanneController(Rigidbody rb, Camera cam1, Camera cam2, GameObject cameraModel)
    {
        this.cameraModel = cameraModel;
        this.rb = rb;
        suzanneOrigin.setTranslation(new javax.vecmath.Vector3f(0,2,0));
        window = Window.CreateWindow().getWindowHandle();
        this.cam1 = cam1;
        this.cam2 = cam2;

        cam2.addHorizAngle(3.14f);
    }

    public void SetVelocity(Vector3f velocity){
        rb.getRigidBody().setLinearVelocity(velocity);
    }

    @Override
    public void Update(float deltaTime){
        Vector3f currVelocity = new Vector3f();
        rb.getRigidBody().getLinearVelocity(currVelocity);

        Vector3f velocity = new Vector3f(0,currVelocity.y,0);
        if (glfwGetKey(window, GLFW_KEY_UP ) == GLFW_PRESS){
            velocity.z = -suzanneSpeed;
        }
        else if (glfwGetKey( window, GLFW_KEY_DOWN ) == GLFW_PRESS){
            velocity.z = suzanneSpeed;
        }
        else velocity.z = 0;

        if (glfwGetKey(window, GLFW_KEY_LEFT ) == GLFW_PRESS){
            velocity.x = -suzanneSpeed;
        }
        else if (glfwGetKey( window, GLFW_KEY_RIGHT ) == GLFW_PRESS){
            velocity.x = suzanneSpeed;
        }
        else velocity.x = 0;

        if (glfwGetKey( window, GLFW_KEY_RIGHT_SHIFT ) == GLFW_PRESS){
            velocity.y = suzanneSpeed;
        }

        SetVelocity(velocity);

        if (glfwGetKey( window, GLFW_KEY_R ) == GLFW_PRESS){
            SuzanneExample.suzanneController.rb.setPosition(new org.joml.Vector3f(0,2,0));
            SetVelocity(new Vector3f(0,0,0));
        }



        if (glfwGetKey( window, GLFW_KEY_F1 ) == GLFW_PRESS){
            WorldManager.setEnableGizmos(false);
        }
        if (glfwGetKey( window, GLFW_KEY_F2 ) == GLFW_PRESS){
            WorldManager.setEnableGizmos(true);
        }

        if (glfwGetKey( window, GLFW_KEY_F3 ) == GLFW_PRESS){
            Window.GetInstance().ActiveCamera = cam1;
            cameraModel.getMeshRenderer().setActive(false);
        }

        if (glfwGetKey( window, GLFW_KEY_F4 ) == GLFW_PRESS){
            Window.GetInstance().ActiveCamera = cam2;
            cameraModel.getMeshRenderer().setActive(true);
        }
        // Suzanne can sometimes deactivate
        SuzanneExample.suzanneController.rb.getRigidBody().activate();
    }
}

package Example;

import Core.Objects.Components.Component;
import Core.Objects.Components.Physics.Rigidbody;
import Core.Scenes.WorldManager;
import Core.Window;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class SuzanneController extends Component {
    public final Rigidbody rb;
    private final Matrix4f suzanneOrigin = new Matrix4f();
    private final long window;

    private float suzanneSpeed = 10;

    public SuzanneController(Rigidbody rb)
    {
        this.rb = rb;
        suzanneOrigin.setTranslation(new javax.vecmath.Vector3f(0,2,0));
        window = Window.CreateWindow().getWindowHandle();
    }

    public void Move(float x, float y, float z) {
        rb.getRigidBody().applyCentralForce(new Vector3f(x, y, z));
    }

    @Override
    public void Update(){
        if (glfwGetKey(window, GLFW_KEY_UP ) == GLFW_PRESS){
            SuzanneExample.suzanneController.Move(0, 0,-suzanneSpeed);
        }
        if (glfwGetKey( window, GLFW_KEY_DOWN ) == GLFW_PRESS){
            SuzanneExample.suzanneController.Move(0,0,suzanneSpeed);
        }
        if (glfwGetKey( window, GLFW_KEY_RIGHT ) == GLFW_PRESS){
            SuzanneExample.suzanneController.Move(suzanneSpeed,0,0);
        }
        if (glfwGetKey( window, GLFW_KEY_LEFT ) == GLFW_PRESS){
            SuzanneExample.suzanneController.Move(-suzanneSpeed,0,0);
        }
        if (glfwGetKey( window, GLFW_KEY_RIGHT_SHIFT) == GLFW_PRESS){
            SuzanneExample.suzanneController.Move(0,suzanneSpeed*5,0);
        }

        if (glfwGetKey( window, GLFW_KEY_R ) == GLFW_PRESS){
            SuzanneExample.suzanneController.rb.getRigidBody().setWorldTransform(new Transform(suzanneOrigin));
        }

        if (glfwGetKey( window, GLFW_KEY_F1 ) == GLFW_PRESS){
            WorldManager.setEnableGizmos(false);
        }
        if (glfwGetKey( window, GLFW_KEY_F2 ) == GLFW_PRESS){
            WorldManager.setEnableGizmos(true);
        }
        // Suzanne can sometimes deactivate
        SuzanneExample.suzanneController.rb.getRigidBody().activate();
    }
}

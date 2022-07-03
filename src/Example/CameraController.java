package Example;

import Core.Objects.Components.Component;
import Core.Objects.Components.Rendering.Camera;
import Core.Window;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class CameraController extends Component {
    private Camera camRef;
    private float speed = 10;
    private float baseSpeed = 10;
    private float sprintSpeed = 20;
    private float mouseSpeed = 0.0005f;
    private long window;

    public CameraController(Camera camRef){
        this.camRef = camRef;
    }

    @Override
    public void Update(float deltaTime){
        window = Window.GetInstance().getWindowHandle();
        CheckKeyboardInput(window, deltaTime, camRef.getDirectionFacingVector(), camRef.getRightVector());
        CheckMouseInput();
    }

    private void CheckMouseInput() {
        DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(window, xPos, yPos);
        camRef.addHorizAngle((float) (mouseSpeed * (camRef.getHalfWidth()-xPos.get(0))));
        camRef.addVertAngle((float) (mouseSpeed * (camRef.getHalfHeight()- yPos.get(0))));
        glfwSetCursorPos(window, camRef.getHalfWidth(),camRef.getHalfHeight());

    }

    private void CheckKeyboardInput(long window, float deltaTime, Vector3f direction, Vector3f right) {
        if (glfwGetKey(window, GLFW_KEY_W ) == GLFW_PRESS){
            getParentPosition().add(direction.mul(deltaTime).mul(speed));
        }
        if (glfwGetKey(window, GLFW_KEY_S ) == GLFW_PRESS){
            getParentPosition().sub(direction.mul(deltaTime).mul(speed));
        }
        if (glfwGetKey(window, GLFW_KEY_D ) == GLFW_PRESS){
            getParentPosition().add(right.mul(deltaTime).mul(speed));
        }
        if (glfwGetKey(window, GLFW_KEY_A ) == GLFW_PRESS){
            getParentPosition().sub(right.mul(deltaTime).mul(speed));
        }
        if (glfwGetKey(window, GLFW_KEY_SPACE ) == GLFW_PRESS){
            getParentPosition().add(new Vector3f(0,1,0).mul(deltaTime).mul(speed));
        }
        if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL ) == GLFW_PRESS){
            getParentPosition().add(new Vector3f(0,-1,0).mul(deltaTime).mul(speed));
        }

        speed = (glfwGetKey( window, GLFW_KEY_LEFT_SHIFT ) == GLFW_PRESS)? sprintSpeed : baseSpeed;
    }

}

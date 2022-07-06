package Example;

import Core.Input.Input;
import Core.Input.KeyEvent;
import Core.Input.MouseEvent;
import Core.Objects.Components.Component;
import Core.Objects.Components.Rendering.Camera;
import Core.Objects.GameObject;
import Core.Worlds.WorldManager;
import Core.Window;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class CameraController extends Component {
    private Camera camRef;
    private float speed = 10;
    private float baseSpeed = 10;
    private float sprintSpeed = 20;
    private float mouseSpeed = 0.0005f;
    private long window;
    private GameObject cameraModel;

    public CameraController(Camera camRef, GameObject cameraModel){
        this.camRef = camRef;
        this.cameraModel = cameraModel;
        this.cameraModel.getMeshRenderer().setActive(false);
        WorldManager.getCurrentWorld().AddGizmo(cameraModel);

        Window.GetInstance().mouseCallbacks.add(new MouseEvent() {
            @Override
            public void mousePressed(int button) {
                if(button == GLFW_MOUSE_BUTTON_1){
                    Raycast(camRef.RayCastHitObject(30));
                }
            }
            @Override
            public void mouseReleased(int button) {

            }
        });
    }

    @Override
    public void Update(float deltaTime){
        window = Window.GetInstance().getWindowHandle();
        CheckKeyboardInput(window, deltaTime, camRef.getDirectionFacingVector(), camRef.getRightVector());
        CheckMouseInput();
        cameraModel.setPosition(camRef.getParentPosition());
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
        if (Input.isKeyPressed(GLFW_KEY_W)) {
            setParentPosition(getParentPosition().add(direction.mul(deltaTime).mul(speed)));
        }
        if (Input.isKeyPressed(GLFW_KEY_S)) {
            setParentPosition(getParentPosition().sub(direction.mul(deltaTime).mul(speed)));
        }
        if (Input.isKeyPressed(GLFW_KEY_D)) {
            setParentPosition(getParentPosition().add(right.mul(deltaTime).mul(speed)));
        }
        if (Input.isKeyPressed(GLFW_KEY_A)) {
            setParentPosition(getParentPosition().sub(right.mul(deltaTime).mul(speed)));
        }
        if (Input.isKeyPressed(GLFW_KEY_SPACE)) {
            setParentPosition(getParentPosition().add(new Vector3f(0,1,0).mul(deltaTime).mul(speed)));
        }
        if (Input.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            setParentPosition(getParentPosition().add(new Vector3f(0,-1,0).mul(deltaTime).mul(speed)));
        }

        speed = (glfwGetKey( window, GLFW_KEY_LEFT_SHIFT ) == GLFW_PRESS)? sprintSpeed : baseSpeed;
    }


    private void Raycast(GameObject object){
        if(GameObject.isValid(object)){
            System.out.println("You Hit:" + object.getIdentity().getName());
        }
    }


}

package Example;

import Core.Camera;
import com.bulletphysics.linearmath.Transform;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class CustomCamera extends Camera {
    private javax.vecmath.Matrix4f suzanneOrigin = new javax.vecmath.Matrix4f();

    public CustomCamera(){
        super();
        suzanneOrigin.setTranslation(new javax.vecmath.Vector3f(0,2,0));
    }

    @Override
    public void CheckInput(long window, float deltaTime, Vector3f direction, Vector3f right){
        super.CheckInput(window,deltaTime,direction,right);
        if (glfwGetKey( window, GLFW_KEY_UP ) == GLFW_PRESS){
            SuzanneExample.suzanneController.Move(0, 0,-speed);
        }
        if (glfwGetKey( window, GLFW_KEY_DOWN ) == GLFW_PRESS){
            SuzanneExample.suzanneController.Move(0,0,speed);
        }
        if (glfwGetKey( window, GLFW_KEY_RIGHT ) == GLFW_PRESS){
            SuzanneExample.suzanneController.Move(speed,0,0);
        }
        if (glfwGetKey( window, GLFW_KEY_LEFT ) == GLFW_PRESS){
            SuzanneExample.suzanneController.Move(-speed,0,0);
        }
        if (glfwGetKey( window, GLFW_KEY_RIGHT_SHIFT) == GLFW_PRESS){
            SuzanneExample.suzanneController.Move(0,speed*5,0);
        }

        if (glfwGetKey( window, GLFW_KEY_R ) == GLFW_PRESS){
            SuzanneExample.suzanneController.rb.getRigidBody().setWorldTransform(new Transform(suzanneOrigin));
        }
        // Suzanne can sometimes deactivate
        SuzanneExample.suzanneController.rb.getRigidBody().activate();
    }
}

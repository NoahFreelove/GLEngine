package Core.Objects.Components.Rendering;

import Core.Objects.Components.Component;
import Core.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Camera extends Component {

    private Matrix4f ViewMatrix;
    private Matrix4f ProjectionMatrix;
    private Matrix4f ModelMatrix = new Matrix4f(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);

    private float horizAngle = 3.14f;
    private float vertAngle = 0f;
    private float fov = 90;

    private float near = 0.1f;
    private float far = 300f;

    protected float speed = 10;
    private float baseSpeed = 10;
    private float sprintSpeed = 20;
    private float mouseSpeed = 0.0005f;
    private double lastTime = glfwGetTime();

    private final float bottomAngle = (float) (-Math.PI/2);
    private final float topAngle = (float) (Math.PI/2);

    public Camera() {
        super();
    }

    public void UpdateRenderMatrix(long window){

        double currentTime = glfwGetTime();
        float deltaTime = (float) (currentTime-lastTime);

        DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(window, xPos, yPos);

        float halfWidth = Window.GetInstance().getWidth()/2f;
        float halfHeight = Window.GetInstance().getHeight()/2f;
        glfwSetCursorPos(window, halfWidth,halfHeight);

        horizAngle += mouseSpeed * (halfWidth-xPos.get(0));
        vertAngle += mouseSpeed * (halfHeight- yPos.get(0));
        vertAngle = clamp(bottomAngle, topAngle, vertAngle);


        Vector3f direction = new Vector3f((float) (Math.cos(vertAngle)* Math.sin(horizAngle)),
                (float) Math.sin(vertAngle),
                (float) (Math.cos(vertAngle) * Math.cos(horizAngle)));

        Vector3f right = new Vector3f((float) Math.sin(horizAngle - Math.PI/2),0f, (float) Math.cos(horizAngle- Math.PI/2));

        Vector3f up = new Vector3f();
        right.cross(direction,up);

        CheckInput(window, deltaTime, direction, right);


        speed = (glfwGetKey( window, GLFW_KEY_LEFT_SHIFT ) == GLFW_PRESS)? sprintSpeed : baseSpeed;

        float FOV = fov;

        ProjectionMatrix = new Matrix4f().perspective((float)Math.toRadians(FOV), (float)Window.GetInstance().getWidth()/ (float)Window.GetInstance().getHeight(),near,far);

        ViewMatrix = new Matrix4f();

        Vector3f addedPos = new Vector3f();
        getParentPosition().add(direction, addedPos);
        ViewMatrix.lookAt(getParentPosition(),addedPos,up);
        lastTime=currentTime;
    }

    public void CheckInput(long window, float deltaTime, Vector3f direction, Vector3f right) {
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
    }

    public static float clamp(float min, float max, float v){
        if(v<min)
            return min;
        return Math.min(v, max);
    }

    public FloatBuffer getMVPBuffer(){
        Matrix4f mvp = new Matrix4f();
        ProjectionMatrix.mul(ViewMatrix, mvp);
        mvp.mul(ModelMatrix, mvp);
        FloatBuffer mvpBuffer = BufferUtils.createFloatBuffer(16);
        matrixToBuffer(mvp, mvpBuffer);
        return mvpBuffer;
    }
    public FloatBuffer getViewMatrixBuffer(){
        FloatBuffer viewMatrixBuffer = BufferUtils.createFloatBuffer(16);
        //Rotate the view matrix
        matrixToBuffer(ViewMatrix, viewMatrixBuffer);
        return viewMatrixBuffer;
    }

    public FloatBuffer getProjectionMatrix(){
        FloatBuffer projectionMatrixBuffer = BufferUtils.createFloatBuffer(16);
        matrixToBuffer(ProjectionMatrix, projectionMatrixBuffer);
        return projectionMatrixBuffer;
    }

    public FloatBuffer getModelMatrix(){
        FloatBuffer modelMatrixBuffer = BufferUtils.createFloatBuffer(16);
        matrixToBuffer(ModelMatrix, modelMatrixBuffer);
        return modelMatrixBuffer;
    }

    private static void matrixToBuffer(Matrix4f m, FloatBuffer dest)
    {
        dest.put(0, m.m00());
        dest.put(1, m.m01());
        dest.put(2, m.m02());
        dest.put(3, m.m03());
        dest.put(4, m.m10());
        dest.put(5, m.m11());
        dest.put(6, m.m12());
        dest.put(7, m.m13());
        dest.put(8, m.m20());
        dest.put(9, m.m21());
        dest.put(10, m.m22());
        dest.put(11, m.m23());
        dest.put(12, m.m30());
        dest.put(13, m.m31());
        dest.put(14, m.m32());
        dest.put(15, m.m33());
    }

    public void setActiveGameObject(Matrix4f modelMatrix)
    {
        ModelMatrix = modelMatrix;
    }

    public void setWireframe(boolean isWireframe){
        glPolygonMode( GL_FRONT_AND_BACK, isWireframe? GL_LINE : GL_FILL );
    }

    public void setCull(boolean shouldCull){
        if(shouldCull)
            glEnable(GL_CULL_FACE);
        else
            glDisable(GL_CULL_FACE);
    }

    public void setDepthTest(boolean depthTest) {
        if(depthTest) {
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LESS);
        }
        else
            glDisable(GL_DEPTH_TEST);
    }

    public void setBackgroundColor(Vector4f backgroundColor) {
        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w);
    }
}

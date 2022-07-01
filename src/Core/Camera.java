package Core;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    static Matrix4f ViewMatrix;
    static Matrix4f ProjectionMatrix;
    static Matrix4f ModelMatrix = new Matrix4f(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);

    static Vector3f position = new Vector3f(0,0,5);
    static float horizAngle = 3.14f;
    static float vertAngle = 0f;
    static float initialFOV = 90;

    static float near = 0.1f;
    static float far = 300f;

    static float speed = 50;
    static float mouseSpeed = 0.0005f;
    static double lastTime = glfwGetTime();

    static final float bottomAngle = (float) (-Math.PI/2);
    static final float topAngle = (float) (Math.PI/2);

    public static void CheckInput(long window){

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

        if (glfwGetKey( window, GLFW_KEY_W ) == GLFW_PRESS){
            position.add(direction.mul(deltaTime).mul(speed));
        }
        if (glfwGetKey( window, GLFW_KEY_S ) == GLFW_PRESS){
            position.sub(direction.mul(deltaTime).mul(speed));
        }
        if (glfwGetKey( window, GLFW_KEY_D ) == GLFW_PRESS){
            position.add(right.mul(deltaTime).mul(speed));
        }
        if (glfwGetKey( window, GLFW_KEY_A ) == GLFW_PRESS){
            position.sub(right.mul(deltaTime).mul(speed));
        }
        if (glfwGetKey( window, GLFW_KEY_SPACE ) == GLFW_PRESS){
            position.add(new Vector3f(0,1,0).mul(deltaTime).mul(speed));
        }
        if (glfwGetKey( window, GLFW_KEY_LEFT_CONTROL ) == GLFW_PRESS){
            position.add(new Vector3f(0,-1,0).mul(deltaTime).mul(speed));
        }
        float FOV = initialFOV;

        ProjectionMatrix = new Matrix4f().perspective((float)Math.toRadians(FOV), (float)Window.GetInstance().getWidth()/ (float)Window.GetInstance().getHeight(),near,far);

        ViewMatrix = new Matrix4f();

        Vector3f addedPos = new Vector3f();
        position.add(direction, addedPos);
        ViewMatrix.lookAt(position,addedPos,up);
        lastTime=currentTime;
    }

    public static float clamp(float min, float max, float v){
        if(v<min)
            return min;
        return Math.min(v, max);
    }

    public static FloatBuffer getMVPBuffer(){
        Matrix4f mvp = new Matrix4f();
        ProjectionMatrix.mul(ViewMatrix, mvp);
        mvp.mul(ModelMatrix, mvp);
        FloatBuffer mvpBuffer = BufferUtils.createFloatBuffer(16);
        matrixToBuffer(mvp, mvpBuffer);
        return mvpBuffer;
    }
    public static FloatBuffer getViewMatrixBuffer(){
        FloatBuffer viewMatrixBuffer = BufferUtils.createFloatBuffer(16);
        matrixToBuffer(ViewMatrix, viewMatrixBuffer);
        return viewMatrixBuffer;
    }

    public static FloatBuffer getProjectionMatrix(){
        FloatBuffer projectionMatrixBuffer = BufferUtils.createFloatBuffer(16);
        matrixToBuffer(ProjectionMatrix, projectionMatrixBuffer);
        return projectionMatrixBuffer;
    }

    public static FloatBuffer getModelMatrix(){
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

    public static void setActiveModelMatrix(Matrix4f modelMatrix)
    {
        ModelMatrix = modelMatrix;
    }
}

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    static Matrix4f ViewMatrix;
    static Matrix4f ProjectionMatrix;


    public static Matrix4f getViewMatrix() {
        return ViewMatrix;
    }

    public static Matrix4f getProjectionMatrix() {
        return ProjectionMatrix;
    }

    static Vector3f position = new Vector3f(0,0,5);
    static float horizAngle = 3.14f;
    static float vertAngle = 0f;
    static float initialFOV = 120;

    static float speed =5;
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

        float halfWidth = Main.width/2f;
        float halfHeight = Main.height/2f;
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

        ProjectionMatrix = new Matrix4f().perspective((float)Math.toRadians(FOV), Main.width/Main.height,0.1f,100f);

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

    public static FloatBuffer getViewBuffer(){
        Matrix4f mvp = new Matrix4f();
        ProjectionMatrix.mul(ViewMatrix, mvp);
        FloatBuffer mvpBuffer = BufferUtils.createFloatBuffer(16);
        matrixToBuffer(mvp,0,mvpBuffer);
        return mvpBuffer;
    }

    private static void matrixToBuffer(Matrix4f m, int offset, FloatBuffer dest)
    {
        dest.put(offset, m.m00());
        dest.put(offset + 1, m.m01());
        dest.put(offset + 2, m.m02());
        dest.put(offset + 3, m.m03());
        dest.put(offset + 4, m.m10());
        dest.put(offset + 5, m.m11());
        dest.put(offset + 6, m.m12());
        dest.put(offset + 7, m.m13());
        dest.put(offset + 8, m.m20());
        dest.put(offset + 9, m.m21());
        dest.put(offset + 10, m.m22());
        dest.put(offset + 11, m.m23());
        dest.put(offset + 12, m.m30());
        dest.put(offset + 13, m.m31());
        dest.put(offset + 14, m.m32());
        dest.put(offset + 15, m.m33());
    }
}

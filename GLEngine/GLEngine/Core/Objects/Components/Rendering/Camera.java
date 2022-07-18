package GLEngine.Core.Objects.Components.Rendering;

import GLEngine.Core.Objects.Components.Component;
import GLEngine.Core.Objects.GameObject;
import GLEngine.Core.Window;
import GLEngine.Core.Worlds.WorldManager;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Camera extends Component {

    private Matrix4f ViewMatrix = new Matrix4f(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);
    private Matrix4f ProjectionMatrix = new Matrix4f(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);
    private Matrix4f ModelMatrix = new Matrix4f(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);

    private float horizAngle = 3.14f;
    private float vertAngle = 0f;
    private float fov = 90;

    private float near = 0.1f;
    private float far = 300f;

    private float bottomAngle = (float) (-Math.PI/2);
    private float topAngle = (float) (Math.PI/2);

    private Vector3f direction;
    private Vector3f right;
    private Vector3f up;

    private float halfWidth;
    private float halfHeight;

    public Camera() {
        super();
    }

    public Camera(float fov, float near, float far, float topMaxAngle, float bottomMaxAngle){
        this.fov = fov;
        this.far = far;
        this.near = near;
        this.topAngle = topMaxAngle;
        this.bottomAngle = bottomMaxAngle;
    }

    public void UpdateRenderMatrix(){
        halfWidth = Window.GetInstance().getWidth()/2f;
        halfHeight = Window.GetInstance().getHeight()/2f;

        vertAngle = clamp(bottomAngle, topAngle, vertAngle);


        direction = new Vector3f((float) (Math.cos(vertAngle)* Math.sin(horizAngle)),
                (float) Math.sin(vertAngle),
                (float) (Math.cos(vertAngle) * Math.cos(horizAngle)));

        right = new Vector3f((float) Math.sin(horizAngle - Math.PI/2),0f, (float) Math.cos(horizAngle- Math.PI/2));

        up = new Vector3f();
        right.cross(direction,up);

        float FOV = fov;

        ProjectionMatrix = new Matrix4f().perspective((float)Math.toRadians(FOV), (float)Window.GetInstance().getWidth()/ (float)Window.GetInstance().getHeight(),near,far);

        ViewMatrix = new Matrix4f();

        Vector3f addedPos = new Vector3f();
        getParentPosition().add(direction, addedPos);
        ViewMatrix.lookAt(getParentPosition(),addedPos,up);
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

    public FloatBuffer getModelMatrixBuffer(){
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

    public void addHorizAngle(float angle){
        horizAngle+=angle;
    }

    public void addVertAngle(float angle){
        vertAngle+=angle;
    }

    public Vector3f getDirectionFacingVector() {
        return direction;
    }

    public Vector3f getRightVector() {
        return right;
    }

    public Vector3f getUpVector() {
        return up;
    }

    public float getHalfWidth() {
        return halfWidth;
    }

    public float getHalfHeight() {
        return halfHeight;
    }

    public static CollisionWorld.ClosestRayResultCallback Raycast(Vector3f startPos, Vector3f directionNormalized, float distance){
        javax.vecmath.Vector3f fromPos = new javax.vecmath.Vector3f(startPos.x(),startPos.y(),startPos.z());
        Vector3f toPos = new Vector3f(directionNormalized);
        toPos.mul(distance);
        javax.vecmath.Vector3f finalVec =new javax.vecmath.Vector3f(fromPos.x + toPos.x(), fromPos.y +toPos.y(), fromPos.z +toPos.z());
        CollisionWorld.ClosestRayResultCallback res = new CollisionWorld.ClosestRayResultCallback(fromPos,finalVec);
        WorldManager.getCurrentWorld().getPhysicsWorld().getPhysicsWorldObject().rayTest(fromPos,finalVec,res);
        return res;
    }

    public boolean RaycastFromCenterCamera(float distance){
        Vector3f rayDirection = new Vector3f(getDirectionFacingVector());
        rayDirection.normalize();
        CollisionWorld.ClosestRayResultCallback result = Raycast(getParentPosition(),rayDirection,distance);

        return result.hasHit();
    }

    public GameObject RayCastHitObject(float distance){
        Vector3f rayDirection = new Vector3f(getDirectionFacingVector());
        rayDirection.normalize();
        CollisionWorld.ClosestRayResultCallback result = Raycast(getParentPosition(),rayDirection,distance);
        int hashCode = -1;
        if(result.collisionObject != null)
        {
            hashCode = result.collisionObject.getCollisionShape().hashCode();
        }
        return WorldManager.getCurrentWorld().getObjectByColliderHash(hashCode);
    }

    public static boolean RaycastFromCenterCamera(Camera cam, float distance){
        return cam.RaycastFromCenterCamera(distance);
    }
}

package Core;

import Core.Objects.GameObject;
import Core.Objects.Models.RenderSettings;
import Core.Scenes.World;
import Core.Scenes.WorldManager;
import Core.Shaders.ShaderManager;
import IO.OBJ.ModelBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width;
    private int height;

    private static Window instance;

    public long window;
    public int program;
    public Camera ActiveCamera;

    int textureID;
    int ModelMatrixID;
    int matrixID;
    int ViewMatrixID;

    private final Callback postInitCallback;

    private World source;

    private static float deltaTime;

    public void run() {
        init();
        initOpenGLRenderSettings();

        if(postInitCallback !=null)
            postInitCallback.call();

        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private Window(int width, int height, World startingWorld, Callback postInitCallback) {
        instance = this;
        this.width = width;
        this.height = height;
        this.source = startingWorld;
        this.postInitCallback = postInitCallback;
        run();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, "GL Engine", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        GL.createCapabilities();
        program = glCreateProgram();
        ShaderManager.initShaders();
        glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    private void loop() {

        int nbFrames = 0;
        double lastTime = 0;



        matrixID = glGetUniformLocation(program, "MVP");
        ViewMatrixID = glGetUniformLocation(program, "V");
        ModelMatrixID = glGetUniformLocation(program, "M");
        textureID = glGetUniformLocation(program, "myTextureSampler");

        int LightID = glGetUniformLocation(program, "LightPosition_worldspace");

        while ( !glfwWindowShouldClose(window) ) {
            double preFrameTime = glfwGetTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glUseProgram(program);

            Vector3f lightPos = new Vector3f(0,4,5);
            glUniform3f(LightID, lightPos.x(), lightPos.y(), lightPos.z());

            if(ActiveCamera != null)
            {
                ActiveCamera.UpdateRenderMatrix(window);
                Render();
            }

            UpdateObjects();
            glfwSwapBuffers(window);
            glfwPollEvents();

            deltaTime = (float) (glfwGetTime() - preFrameTime);
            WorldManager.getCurrentWorld().step(deltaTime);
            // FPS counter
            double currentTime = glfwGetTime();
            nbFrames++;
            if ( currentTime - lastTime >= 1.0 ){
                System.out.printf("%f ms/frame. %d frames%n", 1000.0/nbFrames, nbFrames);
                nbFrames = 0;
                lastTime += 1.0;
            }
        }
    }

    private void UpdateObjects() {
        for (GameObject o : source.GameObjects()) {
            o.Update();
        }
    }



    private void Render() {
        for (GameObject o :
                source.GameObjects()) {
            if(o!=null){
                RenderGameObject(o);
            }
        }
        for (GameObject o :
                source.Gizmos()) {
            if(o!=null){
                RenderGameObject(o);
            }
        }
    }

    private void RenderGameObject(GameObject gameObject){
        RenderSettings rs = gameObject.getMeshRenderer().getRenderSettings();
        ModelBuffer gameObjectBuffer = gameObject.getMeshRenderer().getMesh().getObjectBuffer();
        int textureID = gameObject.getMeshRenderer().getTexture().getTextureID();

        ActiveCamera.setWireframe(rs.wireframe);
        ActiveCamera.setCull(rs.cullFace);
        ActiveCamera.setDepthTest(rs.depthTest);

        // If the object doesn't have a model, we don't render it
        if(gameObjectBuffer == null)
            return;


        if(textureID >-1)
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, textureID);
            glUniform1i(textureID, 0);
        }
        else{
            System.out.println("Object has no texture");
        }

        ActiveCamera.setActiveGameObject(TransformObject(gameObject.getPosition(), gameObject.getRotation(), gameObject.getScale()));

        glUniformMatrix4fv(ModelMatrixID, false, ActiveCamera.getModelMatrix());
        glUniformMatrix4fv(ViewMatrixID, false, ActiveCamera.getViewMatrixBuffer());
        glUniformMatrix4fv(matrixID, false, ActiveCamera.getMVPBuffer());

        glBindVertexArray(gameObjectBuffer.vertexArrayId);

        if(gameObjectBuffer.vertices.limit()>0)
        {
            glEnableVertexAttribArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, gameObjectBuffer.vertexBuffer);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        }

        if(gameObjectBuffer.uvs.limit()>0)
        {
            glEnableVertexAttribArray(1);
            glBindBuffer(GL_ARRAY_BUFFER, gameObjectBuffer.uvBuffer);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        }
        if(gameObjectBuffer.normals.limit()>0)
        {
            glEnableVertexAttribArray(2);
            glBindBuffer(GL_ARRAY_BUFFER, gameObjectBuffer.normalBuffer);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
        }

        glDrawArrays(gameObject.getMeshRenderer().getRenderSettings().drawMode, 0, gameObjectBuffer.vertices.limit());

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    }

    private Matrix4f TransformObject(Vector3f position, Vector3f rotation, Vector3f scale)
    {
        Matrix4f transformedMatrix = new Matrix4f();

        Matrix4f translationMatrix = new Matrix4f();
        translationMatrix.translate(position.x(), position.y(), position.z());
        transformedMatrix.mul(translationMatrix);

        Matrix4f rotationMatrix = new Matrix4f();
        rotationMatrix.rotate((float) Math.toRadians(rotation.x()), new Vector3f(1,0,0));
        rotationMatrix.rotate((float) Math.toRadians(rotation.y()), new Vector3f(0,1,0));
        rotationMatrix.rotate((float) Math.toRadians(rotation.z()), new Vector3f(0,0,1));
        transformedMatrix.mul(rotationMatrix);

        transformedMatrix.scale(scale);

        return transformedMatrix;
    }

    private void initOpenGLRenderSettings() {
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }


    //region Constructors, Getters, Setters
    public static Window CreateWindow(int width, int height, World world){
        return Objects.requireNonNullElseGet(instance, () -> new Window(width, height, world, null));
    }

    public static Window CreateWindow(int width, int height, Callback postInitCallback, World world){
        return Objects.requireNonNullElseGet(instance, () -> new Window(width, height, world, postInitCallback));
    }

    public static Window CreateWindow(int width, int height, Callback postInitCallback){
        return Objects.requireNonNullElseGet(instance, () -> new Window(width, height, new World(), postInitCallback));
    }

    public static Window CreateWindow(int width, int height){
        return Objects.requireNonNullElseGet(instance, () -> new Window(width, height, new World(), null));
    }

    public static Window CreateWindow(){
        return Objects.requireNonNullElseGet(instance, () -> new Window(800, 800, new World(), null));
    }

    public static Window GetInstance(){
        return instance;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindowHandle() {
        return window;
    }

    public int getProgramHandle() {
        return program;
    }
    
    public void setRenderSource(World world){ source = world; }

    public void setVSync(boolean value) {
        glfwSwapInterval(value ? 1 : 0);
    }

    public void setWidth(int width) {
        if(width<=0)
            throw new IllegalArgumentException("Width must be greater than 0");
        this.width = width;
    }

    public void setHeight(int height) {
        if(height<=0)
            throw new IllegalArgumentException("Height must be greater than 0");
        this.height = height;
    }

    public void setTitle(String title){
        glfwSetWindowTitle(window, title);
    }

    public static float getDeltaTime() {
        return deltaTime;
    }
    //endregion

}

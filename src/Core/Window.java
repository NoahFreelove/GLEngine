package Core;

import Core.Objects.GameObject;
import Core.Scenes.Scene;
import Core.Shaders.ShaderManager;
import IO.OBJ.OBJBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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
    public Camera ActiveCamera = new Camera();

    int textureID;
    int ModelMatrixID;
    int matrixID;
    int ViewMatrixID;

    private Callback postInitCallback;

    private Scene source = new Scene();

    public void run() {
        init();
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

    private Window(int width, int height, Scene startingScene, Callback postInitCallback) {
        instance = this;
        this.width = width;
        this.height = height;
        this.postInitCallback = postInitCallback;
        run();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, "This is a window", NULL, NULL);
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
        setBackgroundColor(new Vector4f(1,1,1,0));
        glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    private void loop() {
        initOpenGLRenderSettings();

        int nbFrames = 0;
        double lastTime = 0;


        matrixID = glGetUniformLocation(program, "MVP");
        ViewMatrixID = glGetUniformLocation(program, "V");
        ModelMatrixID = glGetUniformLocation(program, "M");
        textureID = glGetUniformLocation(program, "myTextureSampler");

        int LightID = glGetUniformLocation(program, "LightPosition_worldspace");

        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glUseProgram(program);

            ActiveCamera.CheckInput(window);

            Vector3f lightPos = new Vector3f(4,4,5);
            glUniform3f(LightID, lightPos.x(), lightPos.y(), lightPos.z());

            Render();

            glfwSwapBuffers(window);
            glfwPollEvents();

            double currentTime = glfwGetTime();
            nbFrames++;
            if ( currentTime - lastTime >= 1.0 ){
                System.out.printf("%f ms/frame. %d frames%n", 1000.0/nbFrames, nbFrames);
                nbFrames = 0;
                lastTime += 1.0;
            }
        }
    }



    public void setBackgroundColor(Vector4f backgroundColor) {
        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w);
    }

    private void Render() {
        for (GameObject o :
                source.GameObjects()) {
            if(o!=null){
                RenderGameObject(o);
            }
        }
    }

    private void RenderGameObject(GameObject gameObject){

        OBJBuffer gameObjectBuffer = gameObject.getObjectBuffer();

        if(gameObject.getTexture()>-1)
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, gameObject.getTexture());
            glUniform1i(textureID, 0);
        }

        ActiveCamera.setActiveModelMatrix(TransformObject(gameObject.getPosition(), gameObject.getRotation(), gameObject.getScale()));

        glUniformMatrix4fv(ModelMatrixID, false, ActiveCamera.getModelMatrix());
        glUniformMatrix4fv(ViewMatrixID, false, ActiveCamera.getViewMatrixBuffer());
        glUniformMatrix4fv(matrixID, false, ActiveCamera.getMVPBuffer());

        glBindVertexArray(gameObjectBuffer.vertexArrayId);

        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, gameObjectBuffer.vertexBuffer);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, gameObjectBuffer.uvBuffer);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(2);
        glBindBuffer(GL_ARRAY_BUFFER, gameObjectBuffer.normalBuffer);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, gameObjectBuffer.vertices.limit());

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
        ActiveCamera.setCull(false);
        ActiveCamera.setWireframe(false);
    }
    public static Window CreateWindow(int width, int height, Scene scene){
        return Objects.requireNonNullElseGet(instance, () -> new Window(width, height, scene, null));
    }

    public static Window CreateWindow(int width, int height, Callback postInitCallback, Scene scene){
        return Objects.requireNonNullElseGet(instance, () -> new Window(width, height, scene, postInitCallback));
    }

    public static Window CreateWindow(int width, int height, Callback postInitCallback){
        return Objects.requireNonNullElseGet(instance, () -> new Window(width, height, new Scene(), postInitCallback));
    }
    public static Window CreateWindow(int width, int height){
        return Objects.requireNonNullElseGet(instance, () -> new Window(width, height, new Scene(), null));
    }
    public static Window CreateWindow(){
        return Objects.requireNonNullElseGet(instance, () -> new Window(800, 800, new Scene(), null));

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
    
    public void setRenderSource(Scene scene){ source = scene; }

    public void setVSync(boolean value) {
        glfwSwapInterval(value ? 1 : 0);
    }
}

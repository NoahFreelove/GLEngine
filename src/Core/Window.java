package Core;

import Core.Shaders.ShaderManager;
import IO.DDS.DDSFile;
import IO.Image;
import IO.OBJ.OBJBuffer;
import IO.OBJ.OBJLoader;
import IO.OBJ.Obj;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    // The window handle
    public long window;
    public int program;

    int textureID;
    int ModelMatrixID;
    int matrixID;
    int ViewMatrixID;
    Obj[] sceneObjects;
    Vector4f backgroundColor = new Vector4f(0,0.7f,0.7f,0);

    public void run() {
        init();
        loadObjects();
        loop();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void loadObjects() {
        Obj model;
        Obj model2;
        try {
            model = OBJLoader.loadModel(new File("src/bin/suzanne.obj"));
            model2 = OBJLoader.loadModel(new File("src/bin/skybox.obj"));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        sceneObjects = new Obj[]{model,model2};

    }

    private Window(int width, int height){
        instance = this;
        this.width = width;
        this.height = height;
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
        glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    private void loop() {
        initOpenGLRenderSettings();

        // Set the clear color
        setBackgroundColor();

        int nbFrames = 0;
        double lastTime = 0;

        DDSFile suzanneTexture = new DDSFile();
        try {
            suzanneTexture = new DDSFile("src/bin/uvmap.DDS");
        } catch (IOException e) {
            System.out.println("Error loading DDS File: " + e.getMessage());
        }

        GameObject suzanne = new GameObject(new Vector3f(1,1,1), new Vector3f(90,0,0), new Vector3f(1,1,1), sceneObjects[0], suzanneTexture);

        suzanne.setPosition(new Vector3f(1,1,1));

        GameObject skybox = new GameObject(new Vector3f(1,1,1), new Vector3f(0,0,0), new Vector3f(5,5,5), sceneObjects[1], new Image("src/bin/skybox.bmp"));

        matrixID = glGetUniformLocation(program, "MVP");
        ViewMatrixID = glGetUniformLocation(program, "V");
        ModelMatrixID = glGetUniformLocation(program, "M");
        textureID = glGetUniformLocation(program, "myTextureSampler");

        int LightID = glGetUniformLocation(program, "LightPosition_worldspace");

        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glUseProgram(program);

            Camera.CheckInput(window);

            Vector3f lightPos = new Vector3f(4,4,2);
            glUniform3f(LightID, lightPos.x(), lightPos.y(), lightPos.z());

            RenderGameObject(suzanne);
            RenderGameObject(skybox);

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

    public void setBackgroundColor() {
        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w);
    }

    private void RenderGameObject(GameObject gameObject){

        OBJBuffer gameObjectBuffer = gameObject.getObjectBuffer();

        if(gameObject.getTexture()>-1)
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, gameObject.getTexture());
            glUniform1i(textureID, 0);
        }

        Camera.setActiveModelMatrix(TransformObject(gameObject.getPosition(), gameObject.getRotation(), gameObject.getScale()));

        glUniformMatrix4fv(ModelMatrixID, false, Camera.getModelMatrix());
        glUniformMatrix4fv(ViewMatrixID, false, Camera.getViewMatrixBuffer());
        glUniformMatrix4fv(matrixID, false, Camera.getMVPBuffer());

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
        System.out.println(transformedMatrix);
        transformedMatrix.transform(new Vector4f(position,1));

        // Rotate based on the rotation vector
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
        setCull(false);
        setWireframe(false);
    }

    public static Window CreateWindow(int width, int height){
        return Objects.requireNonNullElseGet(instance, () -> new Window(width, height));
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

    public static Window getInstance() {
        return instance;
    }

    public long getWindowHandle() {
        return window;
    }

    public int getProgramHandle() {
        return program;
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
}

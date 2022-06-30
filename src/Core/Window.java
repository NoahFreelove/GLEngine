package Core;

import Core.Shaders.ShaderManager;
import IO.DDS.DDSFile;
import IO.OBJ.OBJBuffer;
import IO.OBJ.OBJLoader;
import IO.OBJ.Obj;
import IO.OBJ.ObjToBuffer;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.*;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Window {

    private int width = 1500;
    private int height = 1500;
    private static Window instance;

    // The window handle
    public long window;
    public int program;

    public void run() {
        init();
        loop();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
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
    }

    private void loop() {

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glEnable(GL_CULL_FACE);
        glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.4f, 0.0f);

        int vertexArrayId = glGenVertexArrays();
        glBindVertexArray(vertexArrayId);
        int vertexArray2Id = glGenVertexArrays();


        int nbFrames = 0;
        double lastTime = 0;

        Obj model;
        Obj model2;
        try {
            model = OBJLoader.loadModel(new File("src/bin/suzanne.obj"));
            model2 = OBJLoader.loadModel(new File("src/bin/farcube.obj"));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OBJBuffer objectBuffer = ObjToBuffer.objToBuffer(model);
        OBJBuffer objBuffer2 = ObjToBuffer.objToBuffer(model2);

        int vertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, objectBuffer.vertices, GL_STATIC_DRAW);

        int uvBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, uvBuffer);
        glBufferData(GL_ARRAY_BUFFER, objectBuffer.uvs, GL_STATIC_DRAW);

        int normalBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, normalBuffer);
        glBufferData(GL_ARRAY_BUFFER, objectBuffer.normals, GL_STATIC_DRAW);


        glBindVertexArray(vertexArray2Id);
        int vertex2Buffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertex2Buffer);
        glBufferData(GL_ARRAY_BUFFER, objBuffer2.vertices, GL_STATIC_DRAW);

        glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);


        int texture = 0;
        try {
            DDSFile textureFile = new DDSFile("src/bin/uvmap.DDS");
            texture = textureFile.generateTexture();
        } catch (IOException e) {
            System.out.println("Error loading DDS File: " + e.getMessage());
        }
        int matrixID = glGetUniformLocation(program, "MVP");
        int ViewMatrixID = glGetUniformLocation(program, "V");
        int ModelMatrixID = glGetUniformLocation(program, "M");
        int textureID = glGetUniformLocation(program, "myTextureSampler");
        glUseProgram(program);
        int LightID = glGetUniformLocation(program, "LightPosition_worldspace");

        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glUseProgram(program);


            Camera.CheckInput(window);

            glUniformMatrix4fv(matrixID, false, Camera.getMVPBuffer());
            glUniformMatrix4fv(ViewMatrixID, false, Camera.getViewMatrixBuffer());
            glUniformMatrix4fv(ModelMatrixID, false, Camera.getModelMatrix());

            Vector3f lightPos = new Vector3f(4,4,4);
            glUniform3f(LightID, lightPos.x(), lightPos.y(), lightPos.z());

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture);
            glUniform1i(textureID, 0);

            glBindVertexArray(vertexArrayId);
            glEnableVertexAttribArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            glEnableVertexAttribArray(1);
            glBindBuffer(GL_ARRAY_BUFFER, uvBuffer);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            glEnableVertexAttribArray(2);
            glBindBuffer(GL_ARRAY_BUFFER, normalBuffer);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            glDrawArrays(GL_TRIANGLES, 0, objectBuffer.vertices.limit());


            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);

            glBindVertexArray(vertexArray2Id);

            glEnableVertexAttribArray(0);

            glBindBuffer(GL_ARRAY_BUFFER, vertex2Buffer);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            glDrawArrays(GL_TRIANGLES, 0, objBuffer2.vertices.limit()/4);

            glDisableVertexAttribArray(0);


            glfwSwapBuffers(window);
            glfwPollEvents();


            double currentTime = glfwGetTime();
            nbFrames++;
            if ( currentTime - lastTime >= 1.0 ){
                // printf and reset timer
                System.out.printf("%f ms/frame. %d frames/s\n%n", 1000.0/nbFrames, nbFrames);
                nbFrames = 0;
                lastTime += 1.0;
            }
        }
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

    public long getWindow() {
        return window;
    }

    public int getProgram() {
        return program;
    }
}

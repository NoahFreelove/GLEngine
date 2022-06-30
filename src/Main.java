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

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Main {

    public static int width = 1500;
    public static int height = 1500;

    // The window handle
    public static long window;
    public static int program;
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

        //glEnable(GL_CULL_FACE);
        //glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.4f, 0.0f);

        //region arrays
        float[] UV = {
                0.000059f, 1.0f-0.000004f,
                0.000103f, 1.0f-0.336048f,
                0.335973f, 1.0f-0.335903f,
                1.000023f, 1.0f-0.000013f,
                0.667979f, 1.0f-0.335851f,
                0.999958f, 1.0f-0.336064f,
                0.667979f, 1.0f-0.335851f,
                0.336024f, 1.0f-0.671877f,
                0.667969f, 1.0f-0.671889f,
                1.000023f, 1.0f-0.000013f,
                0.668104f, 1.0f-0.000013f,
                0.667979f, 1.0f-0.335851f,
                0.000059f, 1.0f-0.000004f,
                0.335973f, 1.0f-0.335903f,
                0.336098f, 1.0f-0.000071f,
                0.667979f, 1.0f-0.335851f,
                0.335973f, 1.0f-0.335903f,
                0.336024f, 1.0f-0.671877f,
                1.000004f, 1.0f-0.671847f,
                0.999958f, 1.0f-0.336064f,
                0.667979f, 1.0f-0.335851f,
                0.668104f, 1.0f-0.000013f,
                0.335973f, 1.0f-0.335903f,
                0.667979f, 1.0f-0.335851f,
                0.335973f, 1.0f-0.335903f,
                0.668104f, 1.0f-0.000013f,
                0.336098f, 1.0f-0.000071f,
                0.000103f, 1.0f-0.336048f,
                0.000004f, 1.0f-0.671870f,
                0.336024f, 1.0f-0.671877f,
                0.000103f, 1.0f-0.336048f,
                0.336024f, 1.0f-0.671877f,
                0.335973f, 1.0f-0.335903f,
                0.667969f, 1.0f-0.671889f,
                1.000004f, 1.0f-0.671847f,
                0.667979f, 1.0f-0.335851f
        };

        float[] cube2 = {
                5.0f,5.0f,5.0f,
                5.0f,5.0f, 7.0f,
                5.0f, 7.0f, 7.0f,
                7.0f, 7.0f,5.0f,
                5.0f,5.0f,5.0f,
                5.0f, 7.0f,5.0f,
                7.0f,5.0f, 7.0f,
                5.0f,5.0f,5.0f,
                7.0f,5.0f,5.0f,
                7.0f, 7.0f,5.0f,
                7.0f,5.0f,5.0f,
                5.0f,5.0f,5.0f,
                5.0f,5.0f,5.0f,
                5.0f, 7.0f, 7.0f,
                5.0f, 7.0f,5.0f,
                7.0f,5.0f, 7.0f,
                5.0f,5.0f, 7.0f,
                5.0f,5.0f,5.0f,
                5.0f, 7.0f, 7.0f,
                5.0f,5.0f, 7.0f,
                7.0f,5.0f, 7.0f,
                7.0f, 7.0f, 7.0f,
                7.0f,5.0f,5.0f,
                7.0f, 7.0f,5.0f,
                7.0f,5.0f,5.0f,
                7.0f, 7.0f, 7.0f,
                7.0f,5.0f, 7.0f,
                7.0f, 7.0f, 7.0f,
                7.0f, 7.0f,5.0f,
                5.0f, 7.0f,5.0f,
                7.0f, 7.0f, 7.0f,
                5.0f, 7.0f,5.0f,
                5.0f, 7.0f, 7.0f,
                7.0f, 7.0f, 7.0f,
                5.0f, 7.0f, 7.0f,
                7.0f,5.0f, 7.0f
        };
        //endregion

        int vertexArrayId = glGenVertexArrays();
        glBindVertexArray(vertexArrayId);


        Obj model;
        try {
            model = OBJLoader.loadModel(new File("src/bin/suzanne.obj"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OBJBuffer objectBuffer = ObjToBuffer.objToBuffer(model);


        int vertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, objectBuffer.vertices, GL_STATIC_DRAW);

        int uvBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, uvBuffer);
        glBufferData(GL_ARRAY_BUFFER, objectBuffer.uvs, GL_STATIC_DRAW);

        int normalBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, normalBuffer);
        glBufferData(GL_ARRAY_BUFFER, objectBuffer.normals, GL_STATIC_DRAW);

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

            //region Camera Matrix
            Camera.CheckInput(window);
            glUniformMatrix4fv(matrixID, false, Camera.getMVPBuffer());
            glUniformMatrix4fv(ViewMatrixID, false, Camera.getViewMatrixBuffer());
            glUniformMatrix4fv(ModelMatrixID, false, Camera.getModelMatrix());
            //endregion

            Vector3f lightPos = new Vector3f(4,4,4);
            glUniform3f(LightID, lightPos.x(), lightPos.y(), lightPos.z());

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture);
            glUniform1i(textureID, 0);

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

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}

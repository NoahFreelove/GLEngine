import IO.Image;
import IO.OBJLoader;
import IO.Obj;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.File;
import java.io.FileNotFoundException;
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
    }

    private void loop() {

        ShaderManager.initShaders();

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        //glEnable(GL_CULL_FACE);
        //glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );

        // Set the clear color
        glClearColor(1.0f, 1.0f, 0.0f, 0.0f);

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

        //objectBuffer.put(cube1);

        int size = 0;
        int vertCount = 0;

        Obj model = null;
        try {
            model = OBJLoader.loadModel(new File("src/bin/text.obj"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        size = model.getFaces().size()*9 + cube2.length*2;

        FloatBuffer objectBuffer = BufferUtils.createFloatBuffer(size);
        float[] points = new float[model.getFaces().size()*9 + cube2.length];
        for (Obj.Face face : model.getFaces()) {
            Vector3f[] vertices = {
                    model.getVertices().get(face.getVertices()[0] - 1),
                    model.getVertices().get(face.getVertices()[1] - 1),
                    model.getVertices().get(face.getVertices()[2] - 1)
            };
            {
                points[vertCount] = vertices[0].x();
                points[vertCount+1] = vertices[0].y();
                points[vertCount+2] = vertices[0].z();
                points[vertCount+3] = vertices[1].x();
                points[vertCount+4] = vertices[1].y();
                points[vertCount+5] = vertices[1].z();
                points[vertCount+6] = vertices[2].x();
                points[vertCount+7] = vertices[2].y();
                points[vertCount+8] = vertices[2].z();
                vertCount+=9;
            }

        }
        //objectBuffer.put(points);
        objectBuffer.put(cube2);
        objectBuffer.flip();

        int vertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, objectBuffer, GL_STATIC_DRAW);


        FloatBuffer UVBuffer = BufferUtils.createFloatBuffer(UV.length);
        UVBuffer.put(UV).flip();
        int uvBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, uvBuffer);
        glBufferData(GL_ARRAY_BUFFER, UVBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        int matrixID = glGetUniformLocation(program, "MVP");
        Image img = new Image("bin/uvtemplate.tga", true);
        int texture = img.getTexID();
        int textureID = glGetUniformLocation(program, "myTextureSampler");


        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glUseProgram(program);

            //region input
            Camera.CheckInput(window);
            glUniformMatrix4fv(matrixID, false, Camera.getViewBuffer());
            //endregion
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture);
            glUniform1i(textureID,0);

            glEnableVertexAttribArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            glEnableVertexAttribArray(1);
            glBindBuffer(GL_ARRAY_BUFFER, uvBuffer);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);


            glDrawArrays(GL_TRIANGLES, 0, size*2);
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderManager {

    public static void attachShader(String source, boolean fragmentShader)
    {
        if(fragmentShader)
        {
            // Create the shader and set the source
            int fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(fragmentShaderID, source);

            // Compile the shader
            glCompileShader(fragmentShaderID);

            // Check for errors
            if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE)
                throw new RuntimeException("Error creating vertex shader\n"
                        + glGetShaderInfoLog(fragmentShaderID, glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH)));

            // Attach the shader
            glAttachShader(Main.program, fragmentShaderID);
        }
        else {
            // Create the shader and set the source
            int vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(vertexShaderID, source);

            // Compile the shader
            glCompileShader(vertexShaderID);

            // Check for errors
            if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE)
                throw new RuntimeException("Error creating vertex shader\n"
                        + glGetShaderInfoLog(vertexShaderID, glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH)));

            // Attach the shader
            glAttachShader(Main.program, vertexShaderID);
        }
    }

    private static void linkShaders()
    {
        glLinkProgram(Main.program);

        // Check for errors in the linking process
        if (glGetProgrami(Main.program, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException("Unable to link shader program:");
    }

    public static void initShaders(){
        attachShader("#version 330 core\n" +
                "\n" +
                "// Input vertex data, different for all executions of this shader.\n" +
                "layout(location = 0) in vec3 vertexPosition_modelspace;\n" +
                "layout(location = 1) in vec3 vertexColor;\n" +
                "\n" +
                "// Output data ; will be interpolated for each fragment.\n" +
                "out vec3 fragmentColor;\n" +
                "// Values that stay constant for the whole mesh.\n" +
                "uniform mat4 MVP;\n" +
                "\n" +
                "void main(){\t\n" +
                "\n" +
                "\t// Output position of the vertex, in clip space : MVP * position\n" +
                "\tgl_Position =  MVP * vec4(vertexPosition_modelspace,1);\n" +
                "\n" +
                "\t// The color of each vertex will be interpolated\n" +
                "\t// to produce the color of each fragment\n" +
                "\tfragmentColor = vertexColor;\n" +
                "}", false);

        attachShader("#version 330 core\n" +
                "\n" +
                "// Interpolated values from the vertex shaders\n" +
                "in vec3 fragmentColor;\n" +
                "\n" +
                "// Ouput data\n" +
                "out vec3 color;\n" +
                "\n" +
                "void main(){\n" +
                "\n" +
                "\t// Output color = color specified in the vertex shader, \n" +
                "\t// interpolated between all 3 surrounding vertices\n" +
                "\tcolor = fragmentColor;\n" +
                "\n" +
                "}", true);
        linkShaders();

    }


}

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderManager {

    public static void attachShader(String source, int type)
    {
        if(type == GL_FRAGMENT_SHADER)
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
        else if (type == GL_VERTEX_SHADER){

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
        // vertex shader
        attachShader("#version 330 core\n" +
                "\n" +
                "// Input vertex data, different for all executions of this shader.\n" +
                "layout(location = 0) in vec3 vertexPosition_modelspace;\n" +
                "layout(location = 1) in vec2 vertexUV;\n" +
                "\n" +
                "// Output data ; will be interpolated for each fragment.\n" +
                "out vec2 UV;\n" +
                "\n" +
                "// Values that stay constant for the whole mesh.\n" +
                "uniform mat4 MVP;\n" +
                "\n" +
                "void main(){\n" +
                "\n" +
                "\t// Output position of the vertex, in clip space : MVP * position\n" +
                "\tgl_Position =  MVP * vec4(vertexPosition_modelspace,1);\n" +
                "\t\n" +
                "\t// UV of the vertex. No special space for this one.\n" +
                "\tUV = vertexUV;\n" +
                "}", GL_VERTEX_SHADER);

        // fragment shader
        attachShader("#version 330 core\n" +
                "\n" +
                "// Interpolated values from the vertex shaders\n" +
                "in vec2 UV;\n" +
                "\n" +
                "// Ouput data\n" +
                "out vec3 color;\n" +
                "\n" +
                "// Values that stay constant for the whole mesh.\n" +
                "uniform sampler2D myTextureSampler;\n" +
                "\n" +
                "void main(){\n" +
                "\n" +
                "\t// Output color = color of the texture at the specified UV\n" +
                "\tcolor = texture( myTextureSampler, UV ).rgb;\n" +
                "}", GL_FRAGMENT_SHADER);
        linkShaders();

    }


}

package Core.Shaders;

import Core.Window;

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
            glAttachShader(Window.GetInstance().getProgram(), fragmentShaderID);
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
            glAttachShader(Window.GetInstance().getProgram(), vertexShaderID);
        }
    }

    private static void linkShaders()
    {
        glLinkProgram(Window.getInstance().getProgram());

        // Check for errors in the linking process
        if (glGetProgrami(Window.GetInstance().getProgram(), GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException("Unable to link shader program:");
    }

    public static void initShaders(){
        // vertex shader
        attachShader("#version 330 core\n" +
                "\n" +
                "// Input vertex data, different for all executions of this shader.\n" +
                "layout(location = 0) in vec3 vertexPosition_modelspace;\n" +
                "layout(location = 1) in vec2 vertexUV;\n" +
                "layout(location = 2) in vec3 vertexNormal_modelspace;\n" +
                "\n" +
                "// Output data ; will be interpolated for each fragment.\n" +
                "out vec2 UV;\n" +
                "out vec3 Position_worldspace;\n" +
                "out vec3 Normal_cameraspace;\n" +
                "out vec3 EyeDirection_cameraspace;\n" +
                "out vec3 LightDirection_cameraspace;\n" +
                "\n" +
                "// Values that stay constant for the whole mesh.\n" +
                "uniform mat4 MVP;\n" +
                "uniform mat4 V;\n" +
                "uniform mat4 M;\n" +
                "uniform vec3 LightPosition_worldspace;\n" +
                "\n" +
                "void main(){\n" +
                "\n" +
                "\t// Output position of the vertex, in clip space : MVP * position\n" +
                "\tgl_Position =  MVP * vec4(vertexPosition_modelspace,1);\n" +
                "\t\n" +
                "\t// Position of the vertex, in worldspace : M * position\n" +
                "\tPosition_worldspace = (M * vec4(vertexPosition_modelspace,1)).xyz;\n" +
                "\t\n" +
                "\t// Vector that goes from the vertex to the camera, in camera space.\n" +
                "\t// In camera space, the camera is at the origin (0,0,0).\n" +
                "\tvec3 vertexPosition_cameraspace = ( V * M * vec4(vertexPosition_modelspace,1)).xyz;\n" +
                "\tEyeDirection_cameraspace = vec3(0,0,0) - vertexPosition_cameraspace;\n" +
                "\n" +
                "\t// Vector that goes from the vertex to the light, in camera space. M is ommited because it's identity.\n" +
                "\tvec3 LightPosition_cameraspace = ( V * vec4(LightPosition_worldspace,1)).xyz;\n" +
                "\tLightDirection_cameraspace = LightPosition_cameraspace + EyeDirection_cameraspace;\n" +
                "\t\n" +
                "\t// Normal of the the vertex, in camera space\n" +
                "\tNormal_cameraspace = ( V * M * vec4(vertexNormal_modelspace,0)).xyz; // Only correct if ModelMatrix does not scale the model ! Use its inverse transpose if not.\n" +
                "\t\n" +
                "\t// UV of the vertex. No special space for this one.\n" +
                "\tUV = vertexUV;\n" +
                "}", GL_VERTEX_SHADER);

        // fragment shader
        attachShader("#version 330 core\n" +
                "\n" +
                "// Interpolated values from the vertex shaders\n" +
                "in vec2 UV;\n" +
                "in vec3 Position_worldspace;\n" +
                "in vec3 Normal_cameraspace;\n" +
                "in vec3 EyeDirection_cameraspace;\n" +
                "in vec3 LightDirection_cameraspace;\n" +
                "\n" +
                "// Ouput data\n" +
                "out vec4 color;\n" +
                "\n" +
                "// Values that stay constant for the whole mesh.\n" +
                "uniform sampler2D myTextureSampler;\n" +
                "uniform mat4 MV;\n" +
                "uniform vec3 LightPosition_worldspace;\n" +
                "\n" +
                "void main(){\n" +
                "\n" +
                "\t// Light emission properties\n" +
                "\t// You probably want to put them as uniforms\n" +
                "\tvec3 LightColor = vec3(1,1,1);\n" +
                "\tfloat LightPower = 50.0f;\n" +
                "\t\n" +
                "\t// Material properties\n" +
                "\tvec3 MaterialDiffuseColor = texture( myTextureSampler, UV ).rgb;\n" +
                "\tvec3 MaterialAmbientColor = vec3(0.1,0.1,0.1) * MaterialDiffuseColor;\n" +
                "\tvec3 MaterialSpecularColor = vec3(0.3,0.3,0.3);\n" +
                "\n" +
                "\t// Distance to the light\n" +
                "\tfloat distance = length( LightPosition_worldspace - Position_worldspace );\n" +
                "\n" +
                "\t// Normal of the computed fragment, in camera space\n" +
                "\tvec3 n = normalize( Normal_cameraspace );\n" +
                "\t// Direction of the light (from the fragment to the light)\n" +
                "\tvec3 l = normalize( LightDirection_cameraspace );\n" +
                "\t// Cosine of the angle between the normal and the light direction, \n" +
                "\t// clamped above 0\n" +
                "\t//  - light is at the vertical of the triangle -> 1\n" +
                "\t//  - light is perpendicular to the triangle -> 0\n" +
                "\t//  - light is behind the triangle -> 0\n" +
                "\tfloat cosTheta = clamp( dot( n,l ), 0,1 );\n" +
                "\t\n" +
                "\t// Eye vector (towards the camera)\n" +
                "\tvec3 E = normalize(EyeDirection_cameraspace);\n" +
                "\t// Direction in which the triangle reflects the light\n" +
                "\tvec3 R = reflect(-l,n);\n" +
                "\t// Cosine of the angle between the Eye vector and the Reflect vector,\n" +
                "\t// clamped to 0\n" +
                "\t//  - Looking into the reflection -> 1\n" +
                "\t//  - Looking elsewhere -> < 1\n" +
                "\tfloat cosAlpha = clamp( dot( E,R ), 0,1 );\n" +
                "\t\n" +
                "\tcolor.rgb = \n" +
                "\t\t// Ambient : simulates indirect lighting\n" +
                "\t\tMaterialAmbientColor +\n" +
                "\t\t// Diffuse : \"color\" of the object\n" +
                "\t\tMaterialDiffuseColor * LightColor * LightPower * cosTheta / (distance*distance) +\n" +
                "\t\t// Specular : reflective highlight, like a mirror\n" +
                "\t\tMaterialSpecularColor * LightColor * LightPower * pow(cosAlpha,5) / (distance*distance);\n" +
                "\n" +
                "\tcolor.a = 1;\n" +
                "}", GL_FRAGMENT_SHADER);
        linkShaders();

    }


}

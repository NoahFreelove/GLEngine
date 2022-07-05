package Core.Shaders;

import Core.Window;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private int program;

    // Don't add any until you call LinkShaders() !
    private ArrayList<ShaderUniformElement> uniformElements = new ArrayList<>();

    public ShaderProgram() {
        this.program = glCreateProgram();
    }

    public int getProgram() {
        return program;
    }

    public int getLocation(String name){
        return glGetUniformLocation(program, name);
    }


    public void LinkShaders(){
        glLinkProgram(program);

        // Check for errors in the linking process
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException("Unable to link shader program:");
    }


    public void attachShader(String source, int type, ShaderProgram program)
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
                throw new RuntimeException("Error creating fragment shader\n"
                        + glGetShaderInfoLog(fragmentShaderID, glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH)));

            // Attach the shader
            glAttachShader(program.getProgram(), fragmentShaderID);
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
            glAttachShader(program.getProgram(), vertexShaderID);
        }
    }
    public ArrayList<ShaderUniformElement> getUniformElements(){
        return uniformElements;
    }

    public void setElementValue(String id, FloatBuffer value){
        for (ShaderUniformElement e :
                uniformElements) {
            if(e.getUniformName().equals(id)){
                e.value = value;
            }
        }
    }
}

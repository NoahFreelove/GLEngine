package Core.Shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class ShaderUniformElement {
    String uniformName;
    int location;
    FloatBuffer value;
    UniformValueType type;

    public ShaderUniformElement(String uniformName, FloatBuffer value, UniformValueType type, ShaderProgram program) {
        this.uniformName = uniformName;
        this.location = program.getLocation(uniformName);
        this.value = value;
        this.type = type;
    }

    public FloatBuffer getValue(){
        return value;
    }

    public int getLocation() {
        return location;
    }

    public String getUniformName() {
        return uniformName;
    }

    public UniformValueType type(){ return type; }
}

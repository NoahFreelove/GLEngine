package GLEngine.IO.OBJ;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ModelBuffer {
    public FloatBuffer vertices;
    public FloatBuffer uvs;
    public FloatBuffer normals;
    public final int vertexArrayId;

    public final int vertexBuffer;
    public final int uvBuffer;
    public final int normalBuffer;

    public ModelBuffer(FloatBuffer vertices, FloatBuffer uvs, FloatBuffer normals)
    {
        this.vertices = vertices;
        this.uvs = uvs;
        this.normals = normals;
        vertexArrayId = glGenVertexArrays();
        glBindVertexArray(vertexArrayId);

        vertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        uvBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, uvBuffer);
        glBufferData(GL_ARRAY_BUFFER, uvs, GL_STATIC_DRAW);

        normalBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, normalBuffer);
        glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
    }

    public static ModelBuffer add(ModelBuffer a, ModelBuffer b){
        FloatBuffer vertBuf = BufferUtils.createFloatBuffer(a.vertices.limit() + b.vertices.limit());
        vertBuf.put(a.vertices);
        vertBuf.put(b.vertices);
        vertBuf.flip();

        FloatBuffer uvBuf = BufferUtils.createFloatBuffer(a.uvs.limit() + b.uvs.limit());
        uvBuf.put(a.uvs);
        uvBuf.put(b.uvs);
        uvBuf.flip();

        FloatBuffer normalBuf = BufferUtils.createFloatBuffer(a.normals.limit() + b.normals.limit());
        normalBuf.put(a.normals);
        normalBuf.put(b.normals);
        normalBuf.flip();

        return new ModelBuffer(vertBuf,uvBuf,normalBuf);
    }
}

package IO.OBJ;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class OBJBuffer {
    public FloatBuffer vertices;
    public FloatBuffer uvs;
    public FloatBuffer normals;

    public OBJBuffer(FloatBuffer vertices, FloatBuffer uvs, FloatBuffer normals)
    {
        this.vertices = vertices;
        this.uvs = uvs;
        this.normals = normals;
    }

    public static OBJBuffer add(OBJBuffer a, OBJBuffer b){
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

        return new OBJBuffer(vertBuf,uvBuf,normalBuf);
    }
}

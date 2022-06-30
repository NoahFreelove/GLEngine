package IO.OBJ;

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
}

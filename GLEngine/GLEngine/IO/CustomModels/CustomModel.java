package GLEngine.IO.CustomModels;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class CustomModel {
    Vector3f[] vertices = new Vector3f[0];
    Vector3f[] normals = new Vector3f[0];
    Vector2f[] uvs = new Vector2f[0];

    public CustomModel(){}

    public CustomModel(Vector3f[] vertices, Vector3f[] normals, Vector2f[] uvs) {
        this.vertices = vertices;
        this.normals = normals;
        this.uvs = uvs;
    }

    public CustomModel(CustomModel cm){
        vertices = new Vector3f[cm.getVertices().length];
        int i = 0;
        for (Vector3f v :
                cm.getVertices()) {
            vertices[i] = v;
            i++;
        }
        normals = new Vector3f[cm.getNormals().length];
        i = 0;
        for (Vector3f v :
                cm.getNormals()) {
            normals[i] = v;
            i++;
        }

        uvs = new Vector2f[cm.getUvs().length];
        i = 0;
        for (Vector2f v :
                cm.getUvs()) {
            uvs[i] = v;
            i++;
        }
    }

    public Vector3f[] getVertices() {
        return vertices;
    }

    public Vector3f[] getNormals() {
        return normals;
    }

    public Vector2f[] getUvs() {
        return uvs;
    }
}

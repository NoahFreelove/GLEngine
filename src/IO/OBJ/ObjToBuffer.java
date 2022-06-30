package IO.OBJ;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;


import java.nio.FloatBuffer;

public class ObjToBuffer {

    public static OBJBuffer objToBuffer(Obj model)
    {
        int vertCount = 0;
        float[] verts = new float[model.getFaces().size()*9];
        FloatBuffer vertBuffer = BufferUtils.createFloatBuffer(verts.length);
        for (Obj.Face face : model.getFaces()) {
            Vector3f[] vertices = {
                    model.getVertices().get(face.getVertices()[0] - 1),
                    model.getVertices().get(face.getVertices()[1] - 1),
                    model.getVertices().get(face.getVertices()[2] - 1)
            };
            {
                verts[vertCount] = vertices[0].x();
                verts[vertCount+1] = vertices[0].y();
                verts[vertCount+2] = vertices[0].z();
                verts[vertCount+3] = vertices[1].x();
                verts[vertCount+4] = vertices[1].y();
                verts[vertCount+5] = vertices[1].z();
                verts[vertCount+6] = vertices[2].x();
                verts[vertCount+7] = vertices[2].y();
                verts[vertCount+8] = vertices[2].z();
                vertCount+=9;
            }

        }
        vertBuffer.put(verts);
        vertBuffer.flip();

        int normalCount = 0;
        float[] normals = new float[model.getFaces().size()*9];
        FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(normals.length);
        for (Obj.Face face : model.getFaces()) {
            Vector3f[] vertices = {
                    model.getNormals().get(face.getNormals()[0] - 1),
                    model.getNormals().get(face.getNormals()[1] - 1),
                    model.getNormals().get(face.getNormals()[2] - 1)
            };
            {
                normals[normalCount] = vertices[0].x();
                normals[normalCount+1] = vertices[0].y();
                normals[normalCount+2] = vertices[0].z();
                normals[normalCount+3] = vertices[1].x();
                normals[normalCount+4] = vertices[1].y();
                normals[normalCount+5] = vertices[1].z();
                normals[normalCount+6] = vertices[2].x();
                normals[normalCount+7] = vertices[2].y();
                normals[normalCount+8] = vertices[2].z();
                normalCount+=9;
            }

        }
        normalBuffer.put(normals);
        normalBuffer.flip();

        int uvCount = 0;
        float[] uvs = new float[model.getFaces().size()*6];
        FloatBuffer uvBuffer = BufferUtils.createFloatBuffer(uvs.length);
        for (Obj.Face face : model.getFaces()) {
            Vector2f[] vertices = {
                    model.getTextureCoordinates().get(face.getTextureCoords()[0] - 1),
                    model.getTextureCoordinates().get(face.getTextureCoords()[1] - 1),
                    model.getTextureCoordinates().get(face.getTextureCoords()[2] - 1),
            };
            {
                uvs[uvCount] = vertices[0].x();
                uvs[uvCount+1] = vertices[0].y();
                uvs[uvCount+2] = vertices[1].x();
                uvs[uvCount+3] = vertices[1].y();
                uvs[uvCount+4] = vertices[2].x();
                uvs[uvCount+5] = vertices[2].y();
                uvCount+=6;
            }
        }
        uvBuffer.put(uvs);
        uvBuffer.flip();

        return new OBJBuffer(vertBuffer, uvBuffer, normalBuffer);
    }
}

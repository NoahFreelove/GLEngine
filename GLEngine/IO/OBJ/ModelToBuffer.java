package IO.OBJ;

import Core.Objects.Models.Model;
import IO.CustomModels.CustomModel;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class ModelToBuffer {

    public static ModelBuffer modelToBuffer(Model object)
    {
        return switch (object.type)
        {
            case OBJ -> objToBuffer(object.getObjModel());
            case CUSTOM -> customModelToBuffer(object.getCustomModel());
            case NONE -> customModelToBuffer(new CustomModel());
        };
    }

    public static ModelBuffer customModelToBuffer(CustomModel model){
        FloatBuffer vertices = BufferUtils.createFloatBuffer(model.getVertices().length * 3);
        for (Vector3f vertex : model.getVertices()){
            vertices.put(vertex.x);
            vertices.put(vertex.y);
            vertices.put(vertex.z);
        }
        vertices.flip();

        FloatBuffer normals = BufferUtils.createFloatBuffer(model.getNormals().length * 3);
        for (Vector3f normal : model.getNormals()){
            normals.put(normal.x);
            normals.put(normal.y);
            normals.put(normal.z);
        }
        normals.flip();

        FloatBuffer uvs = BufferUtils.createFloatBuffer(model.getUvs().length * 2);
        for (Vector2f uv : model.getUvs()){
            uvs.put(uv.x);
            uvs.put(uv.y);
        }
        uvs.flip();
        return new ModelBuffer(vertices, uvs, normals);
    }

    public static ModelBuffer objToBuffer(Obj object)
    {
        int vertCount = 0;
        float[] verts = new float[object.getFaces().size()*9];
        FloatBuffer vertBuffer = BufferUtils.createFloatBuffer(verts.length);
        for (Obj.Face face : object.getFaces()) {
            Vector3f[] vertices = {
                    object.getVertices().get(face.getVertices()[0] - 1),
                    object.getVertices().get(face.getVertices()[1] - 1),
                    object.getVertices().get(face.getVertices()[2] - 1)
            };
            {
                verts[vertCount+0] = (vertices[0].x());
                verts[vertCount+1] = (vertices[0].y());
                verts[vertCount+2] = (vertices[0].z());
                verts[vertCount+3] = (vertices[1].x());
                verts[vertCount+4] = (vertices[1].y());
                verts[vertCount+5] = (vertices[1].z());
                verts[vertCount+6] = (vertices[2].x());
                verts[vertCount+7] = (vertices[2].y());
                verts[vertCount+8] = (vertices[2].z());
                vertCount+=9;
            }

        }
        vertBuffer.put(verts);
        vertBuffer.flip();

        int normalCount = 0;
        float[] normals = new float[object.getFaces().size()*9];
        FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(normals.length);
        for (Obj.Face face : object.getFaces()) {
            Vector3f[] vertices = {
                    object.getNormals().get(face.getNormals()[0] - 1),
                    object.getNormals().get(face.getNormals()[1] - 1),
                    object.getNormals().get(face.getNormals()[2] - 1)
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
        float[] uvs = new float[object.getFaces().size()*6];
        FloatBuffer uvBuffer = BufferUtils.createFloatBuffer(uvs.length);
        for (Obj.Face face : object.getFaces()) {
            Vector2f[] vertices = {
                    object.getTextureCoordinates().get(face.getTextureCoords()[0] - 1),
                    object.getTextureCoordinates().get(face.getTextureCoords()[1] - 1),
                    object.getTextureCoordinates().get(face.getTextureCoords()[2] - 1),
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
        return new ModelBuffer(vertBuffer,uvBuffer,normalBuffer);
    }
}

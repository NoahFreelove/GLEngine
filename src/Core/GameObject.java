package Core;

import IO.OBJ.OBJBuffer;
import IO.OBJ.Obj;
import IO.OBJ.GameObjectToBuffer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GameObject {
    private Vector3f position;
    private Quaternionf rotation;
    private Vector3f scale;

    private Obj object;
    private OBJBuffer objectBuffer;


    public GameObject(Obj model){
        position = new Vector3f(0,0,0);
        rotation = new Quaternionf(0,0,0,0);
        scale = new Vector3f(1,1,1);
        this.object = model;
        initObject();
    }

    public GameObject(Vector3f position, Quaternionf rotation, Vector3f scale, Obj model){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.object = model;
        initObject();
    }

    private void initObject(){
        objectBuffer = GameObjectToBuffer.gameobjToBuffer(this);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Obj getObject() {
        return object;
    }

    public void setObject(Obj object) {
        this.object = object;
    }

    public OBJBuffer getObjectBuffer() {
        return objectBuffer;
    }

    public void setPosition(Vector3f newPos)
    {
        this.position = newPos;
        updatePosition();
    }

    private void updatePosition(){
        for (int i = 0; i < objectBuffer.vertices.limit(); i+=3) {
            objectBuffer.vertices.put(i,position.x());
            objectBuffer.vertices.put(i+1,position.y());
            objectBuffer.vertices.put(i+2,position.z());
        }
        //objectBuffer.vertices.flip();

        for (int i = 0; i < objectBuffer.uvs.limit(); i+=2) {
            objectBuffer.uvs.put(i,position.x());
            objectBuffer.uvs.put(i+1,position.y());
        }
        //objectBuffer.uvs.flip();

        for (int i = 0; i < objectBuffer.normals.limit(); i+=3) {
            objectBuffer.normals.put(i,position.x());
            objectBuffer.normals.put(i+1,position.y());
            objectBuffer.normals.put(i+2,position.z());
        }
        //objectBuffer.normals.flip();
    }
}

package Core;

import IO.DDS.DDSFile;
import IO.Image;
import IO.OBJ.OBJBuffer;
import IO.OBJ.Obj;
import IO.OBJ.BufferGameObject;
import org.joml.Vector3f;

public class GameObject {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;
    private String name;

    private Obj object;
    private OBJBuffer objectBuffer;

    private int texture = -1;

    public GameObject(Obj model){
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
        scale = new Vector3f(1,1,1);
        this.object = model;
        this.texture = new Image("src/bin/texture.jpg").createTexture();
        initObject();
    }

    public GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Obj model){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.object = model;
        this.texture = new Image("src/bin/texture.jpg").createTexture();
        initObject();
    }

    public GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Obj model, Image texture){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.object = model;
        this.texture = texture.createTexture();
        initObject();
    }

    public GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Obj model, DDSFile texture){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.object = model;
        this.texture = texture.createTexture();
        initObject();
    }

    private void initObject(){
        objectBuffer = BufferGameObject.bufferGameObject(this);
        name = System.identityHashCode(this) + "";
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
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
    }

    public void setTexture(int texture) {
        this.texture = texture;
    }

    public int getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }
}

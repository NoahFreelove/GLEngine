package Core.Objects;

import IO.DDS.DDSFile;
import IO.Image;
import IO.OBJ.OBJBuffer;
import IO.OBJ.Obj;
import IO.OBJ.BufferGameObject;
import org.joml.Vector3f;

import java.util.ArrayList;

public final class GameObject {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    private final ArrayList<Component> components = new ArrayList<>();

    private Identity identity;

    private Obj object;
    private OBJBuffer objectBuffer = null;

    private int texture = -1;

    public GameObject(){
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
        scale = new Vector3f(1,1,1);
        this.object = null;
        this.texture = new Image("src/bin/texture.jpg").createTexture();
        initObject();
    }

    public GameObject(Obj model){
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
        scale = new Vector3f(1,1,1);
        this.object = model;
        this.texture = new Image("src/bin/texture.jpg").createTexture();
        initObject();
    }

    public GameObject(Vector3f position, Vector3f rotation, Vector3f scale){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.object = null;
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
        if(object != null){
            objectBuffer = BufferGameObject.bufferGameObject(this);
        }
        identity = new Identity("GameObject", "gameObject");
        OnInstantiate();
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

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void setTexture(int texture) {
        this.texture = texture;
    }

    public int getTexture() {
        return texture;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void Update() {
        for (Component component : components) {
            component.Update();
        }
    }

    public void Start() {
        for (Component component : components) {
            component.Start();
        }
    }

    public void Unload() {
        for (Component component : components) {
            component.Unload();
        }
    }

    public void OnDestroy() {
        for (Component component : components) {
            component.OnDestroy();
        }
    }

    public void OnInstantiate() {

    }

    @Override
    public GameObject clone() {
        GameObject clone = new GameObject();
        try {
            clone = (GameObject) super.clone();
        }catch (CloneNotSupportedException e){
            System.out.println("Could not clone GameObject");
        }
        clone.position = new Vector3f(position);
        clone.rotation = new Vector3f(rotation);
        clone.scale = new Vector3f(scale);
        clone.object = object;
        clone.texture = texture;
        clone.identity = getIdentity();
        clone.initObject();
        return clone;
    }

    public void RemoveComponent(Component component){
        component.OnRemoved();
        components.remove(component);
    }

    public void AddComponent(Component component){
        component.OnAdded();
        component.setParent(this);
        components.add(component);
    }

    @Override
    public String toString() {
        return String.format("GameObject: %s (%s). %d Components", identity.getName(), identity.getTag(), components.size());
    }

    public Component getComponent(int index){
        return components.get(index);
    }
}

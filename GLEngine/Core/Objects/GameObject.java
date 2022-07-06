package Core.Objects;

import Core.Objects.Components.Component;
import Core.Objects.Components.Physics.Rigidbody;
import Core.Objects.Components.Rendering.MeshRenderer;
import Core.Objects.Models.Mesh;
import Core.Objects.Models.Model;
import IO.DDS.DDSFile;
import IO.Image;
import org.joml.Vector3f;

import java.io.Serializable;
import java.util.ArrayList;

public final class GameObject implements Serializable, Cloneable {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    private final ArrayList<Component> components = new ArrayList<>();
    private final ArrayList<GameObject> children = new ArrayList<>();
    private boolean lockToParent = true;

    private Identity identity = new Identity("empty", "gameObject");

    private MeshRenderer meshRenderer;

    private GameObject parent = this;

    public GameObject(){
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
        scale = new Vector3f(1,1,1);

        meshRenderer = new MeshRenderer(new Image("bin/texture.jpg"));
        addComponent(meshRenderer);
        initObject();
    }

    public GameObject(Model model){
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
        scale = new Vector3f(1,1,1);
        meshRenderer = new MeshRenderer(new Mesh(model),new Image("bin/texture.jpg"));
        addComponent(meshRenderer);
        initObject();
    }

    public GameObject(Vector3f position, Vector3f rotation, Vector3f scale){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;

        meshRenderer = new MeshRenderer(new Image("bin/texture.jpg"));
        addComponent(meshRenderer);
        initObject();
    }

    public GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Model model){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        addComponent(meshRenderer = new MeshRenderer(new Mesh(model), new Image("bin/texture.jpg")));
        initObject();
    }

    public GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Model model, Image texture){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        addComponent(meshRenderer = new MeshRenderer(new Mesh(model), texture));
        initObject();
    }

    public GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Model model, DDSFile texture){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        addComponent(meshRenderer = new MeshRenderer(new Mesh(model), texture));
        initObject();
    }

    private void initObject(){
        OnInstantiate();
    }

    public Vector3f getPosition() {
        return new Vector3f(position.x(), position.y(), position.z());
    }

    public Vector3f getRotation() {
        return new Vector3f(rotation.x(), rotation.y(), rotation.z());
    }

    public Vector3f getScale() {
        return new Vector3f(scale.x(), scale.y(), scale.z());
    }


    public void setPosition(Vector3f newPos)
    {


        Vector3f oldPos = getPosition();
        this.position = new Vector3f(newPos);

        onTransformUpdate(oldPos, getRotation(), getScale());
    }

    public void setRotation(Vector3f newRot) {
        Vector3f oldRot = getRotation();
        this.rotation = new Vector3f(newRot);

        onTransformUpdate(getPosition(), oldRot, getScale());
    }

    public void setScale(Vector3f newScale) {
        Vector3f oldScale = getScale();
        this.scale = new Vector3f(newScale);

        onTransformUpdate(getPosition(), getRotation(), oldScale);
    }

    private void onTransformUpdate(Vector3f oldPos, Vector3f oldRot, Vector3f oldScale){
        for (Component c :
                components) {
            c.ParentTransformed(getPosition(),getRotation(),getScale());
        }

        Vector3f deltaPos = new Vector3f();
        getPosition().sub(oldPos,deltaPos);

        Vector3f deltaRot = new Vector3f();
        getRotation().sub(oldRot,deltaRot);

        Vector3f deltaScale = new Vector3f();
        getScale().sub(oldScale,deltaScale);

        for (GameObject c : children){
            c.OnParentTransformed(deltaPos, deltaRot, deltaScale);
        }
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void Update(float deltaTime) {
        for (Component component : components) {
            component.Update(deltaTime);
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

    public void Added(){
        for (Component component : components) {
            component.ParentAdded();
        }
    }

    public void OnInstantiate() {}

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
        clone.meshRenderer = new MeshRenderer(meshRenderer);
        clone.identity = getIdentity();
        clone.initObject();
        return clone;
    }

    public void removeComponent(Component component){
        component.OnRemoved();
        components.remove(component);
    }

    public void addComponent(Component component){
        if(components.contains(component))
            return;

        component.setParent(this);
        component.OnAdded();
        components.add(component);
    }

    public void addChild(GameObject child){
        if(children.contains(child) || child == this)
            return;

        child.setParent(this);
        children.add(child);
    }

    public void removeChild(GameObject child){
        children.remove(child);
        child.setParent(child);
    }

    @Override
    public String toString() {
        return String.format("GameObject: %s (%s). %d Components", identity.getName(), identity.getTag(), components.size());
    }

    public Component getComponent(int index){
        return components.get(index);
    }

    public Component getComponentByType(Class type){
        for(Component component : components){
            if(component.getClass() == type){
                return component;
            }
        }
        return null;
    }

    public MeshRenderer getMeshRenderer(){
        if(meshRenderer == null)
        {
            System.out.println("MeshRenderer is null");
            return new MeshRenderer();
        }
        return meshRenderer;
    }

    public void setName (String name){
        identity.setName(name);
    }

    public void setTag(String tag){
        identity.setTag(tag);
    }

    public static boolean isValid(GameObject object){
        if(object == null)
            return false;

        return (!object.getIdentity().name.equals("empty") && object.getMeshRenderer().isActive());
    }

    public GameObject getParent() {
        return parent;
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    public boolean getLockToParent() {
        return lockToParent;
    }

    public void setLockToParent(boolean lockToParent) {
        this.lockToParent = lockToParent;
    }

    private void OnParentTransformed(Vector3f posDelta, Vector3f rotDelta, Vector3f scaleDelta){
        if(true)
            return;

        if(lockToParent){
            Rigidbody rb = (Rigidbody) getComponentByType(Rigidbody.class);
            Vector3f tmpPos = new Vector3f();
            getPosition().add(posDelta, tmpPos);

            Vector3f tmpRot = new Vector3f();
            getRotation().add(rotDelta, tmpRot);

            Vector3f tmpScale = new Vector3f();
            getPosition().add(scaleDelta, tmpScale);

            setPosition(tmpPos);
            setPosition(tmpRot);
            setPosition(tmpScale);

            if(rb !=null){
                rb.setPosition(tmpPos);
            }

        }
    }

    public ArrayList<GameObject> getChildren(){return new ArrayList<>(children);}
}

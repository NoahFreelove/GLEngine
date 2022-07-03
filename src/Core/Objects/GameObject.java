package Core.Objects;

import Core.Objects.Components.Component;
import Core.Objects.Components.Rendering.MeshRenderer;
import Core.Objects.Models.Mesh;
import Core.Objects.Models.Model;
import IO.DDS.DDSFile;
import IO.Image;
import org.joml.Vector3f;

import java.util.ArrayList;

public final class GameObject {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    private final ArrayList<Component> components = new ArrayList<>();

    private Identity identity;

    private MeshRenderer meshRenderer;

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
        addComponent(meshRenderer = new MeshRenderer(new Mesh(model), texture.createTexture()));
        initObject();
    }

    private void initObject(){
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
}

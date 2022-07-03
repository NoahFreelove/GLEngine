package Core.Objects.Components;

import Core.Objects.GameBehavior;
import Core.Objects.GameObject;
import org.joml.Vector3f;

public class Component implements GameBehavior {

    private GameObject attachedObject = null;

    private boolean isActive = true;

    public Component(){}

    @Override
    public void Update() {}

    @Override
    public void Start() {}

    @Override
    public void ParentAdded() {

    }

    @Override
    public void Unload() {}

    @Override
    public void OnDestroy() {}

    @Override
    public void OnRemoved() {}

    @Override
    public void OnAdded() {}


    public Vector3f getParentPosition(){
        return attachedObject.getPosition();
    }

    public Vector3f getParentRotation(){
        return attachedObject.getRotation();
    }

    public Vector3f getParentScale(){
        return attachedObject.getScale();
    }

    public GameObject getParent(){
        return attachedObject;
    }

    public void setParent(GameObject parent){
        this.attachedObject = parent;
    }

    public void setParentPosition(Vector3f newPos){
        attachedObject.setPosition(newPos);
    }

    public void setParentRotation(Vector3f rotation){
        attachedObject.setRotation(rotation);
    }

    public void setParentScale(Vector3f scale){
        attachedObject.setScale(scale);
    }

    public void setActive(boolean active){
        isActive = active;
    }

    public boolean isActive(){
        return isActive;
    }
}

package GLEngine.Core.Objects.Components;

import GLEngine.Core.Objects.ComponentBehavior;
import GLEngine.Core.Objects.GameObject;
import org.joml.Vector3f;

import java.io.Serializable;

public class Component implements ComponentBehavior, Serializable, Cloneable {

    private GameObject attachedObject = null;

    private boolean isActive = true;

    public Component(){}

    @Override
    public void Update(float deltaTime) {}

    @Override
    public void Start() {}

    @Override
    public void ParentAdded() {}

    @Override
    public void Unload() {}

    @Override
    public void OnDestroy() {}

    @Override
    public void OnRemoved() {}

    @Override
    public void OnAdded() {}

    @Override
    public void ParentTransformed(Vector3f newPos, Vector3f newRot, Vector3f newScale) {}


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

    public static boolean isComponentValid(Component comp){
        if(comp !=null)
            return comp.isActive;

        return false;
    }

    @Override
    public Component clone() {
        try {
            Component clone = (Component) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

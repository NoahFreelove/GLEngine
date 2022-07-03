package Core.Objects;

public interface GameBehavior {
    void Start(); // Called when scene is loaded
    void ParentAdded(); // Called when parent is added to scene
    void Update(float deltaTime); // Called every frame this object is in an active scene
    void Unload(); // Called when scene is unloaded
    void OnDestroy(); // Called when the parent object is destroyed (removed from scene)
    void OnRemoved(); // Called when the component is removed from the parent object
    void OnAdded(); // Called when the component is added to the parent object
}

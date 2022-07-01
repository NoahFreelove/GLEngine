package Core.Objects;

public interface GameBehavior {
    void Update(); // Called every frame this object is in an active scene
    void Start(); // Called when scene is started
    void Unload(); // Called when scene is unloaded
    void OnDestroy(); // Called when the object is destroyed (removed from scene)
    void OnInstantiate(); // Called when the object is created

}

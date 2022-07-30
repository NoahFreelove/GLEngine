package GLEngine.Core.Worlds;

import GLEngine.Core.Audio.Sound;
import GLEngine.Core.Objects.GameObject;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.RigidBody;

import java.util.ArrayList;
import java.util.Arrays;

public class World {
    private final PhysicsWorld physicsWorld;

    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final ArrayList<GameObject> gizmos = new ArrayList<>();
    private final ArrayList<Sound> activeSounds = new ArrayList<>();
    private final ArrayList<HashObject> colliderHashes = new ArrayList<>();

    private boolean isActive;

    private String name = "Scene";

    public World(GameObject[] initObjects, String name){
        this();
        gameObjects.addAll(Arrays.asList(initObjects));
        this.name = name;
    }

    public World(){
        physicsWorld = new PhysicsWorld();
    }

    public ArrayList<GameObject> GameObjects() {
        return gameObjects;
    }

    public String getName() {
        return name;
    }

    public void Add(GameObject object){
        if(!gameObjects.contains(object))
        {
            gameObjects.add(object);
        }
    }
    public void AddGizmo(GameObject gizmo){
        gizmos.add(gizmo);
    }

    public void AddAll(GameObject... objects){
        gameObjects.addAll(Arrays.asList(objects));
    }

    public void AddAllGizmos(GameObject... gizmo){
        gizmos.addAll(Arrays.asList(gizmo));
    }

    public void Remove(GameObject object){
        object.OnDestroy();
        gameObjects.remove(object);
    }
    public void RemoveGizmo(GameObject gizmo){
        gizmo.OnDestroy();
        gizmos.remove(gizmo);
    }
    public PhysicsWorld getPhysicsWorld() {
        return physicsWorld;
    }

    public void step(float deltaTime){
        physicsWorld.getPhysicsWorldObject().stepSimulation(deltaTime);
    }

    public ArrayList<GameObject> Gizmos() {
        return gizmos;
    }

    public void addRigidBody(RigidBody rigidBody){
        physicsWorld.getPhysicsWorldObject().addRigidBody(rigidBody);
    }

    public void addCollider(CollisionObject collider){
        physicsWorld.getPhysicsWorldObject().addCollisionObject(collider);
    }

    public void RegisterCollider(HashObject ho){
        colliderHashes.add(ho);
        //System.out.printf("registered collider %d : %s%n", ho.getColliderHash(), ho.getGameObject().getIdentity().getName());
    }
    public void RemoveCollider(CollisionObject collider){
        physicsWorld.getPhysicsWorldObject().removeCollisionObject(collider);
    }

    public GameObject getObjectByColliderHash(int hash){
        for (HashObject ho :
                colliderHashes) {
            if (ho.getColliderHash() == hash) {
                return ho.getGameObject();
            }
        }
        return new GameObject();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public GameObject getGameObjectByName(String name){
        for (GameObject go :
                gameObjects) {
            if (go.getIdentity().getName().equals(name)) {
                return go;
            }
        }
        return new GameObject();
    }

    public GameObject getGameObjectByTag(String tag){
        for (GameObject go :
                gameObjects) {
            if (go.getIdentity().getTag().equals(tag)) {
                return go;
            }
        }
        return new GameObject();
    }
}

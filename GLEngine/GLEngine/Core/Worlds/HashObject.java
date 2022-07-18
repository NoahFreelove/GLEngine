package GLEngine.Core.Worlds;

import GLEngine.Core.Objects.GameObject;

public class HashObject {
    private GameObject gameObject;
    private int colliderHash;

    public HashObject(GameObject gameObject, int colliderHash) {
        this.gameObject = gameObject;
        this.colliderHash = colliderHash;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public int getColliderHash() {
        return colliderHash;
    }
}

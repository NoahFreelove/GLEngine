package GLEngine.Core.Objects;

import org.joml.Vector3f;

public class GameObjectSaveData {

    public String name;
    public String tag;
    public String modelPath;
    public String texturePath;

    public Vector3f initialPosition;
    public Vector3f initialRotation;
    public Vector3f initialScale;

    public GameObjectSaveData(String name, String tag, String modelPath, String texturePath, Vector3f initialPos, Vector3f initialRot, Vector3f initialScale) {
        this.name = name;
        this.tag = tag;
        this.modelPath = modelPath;
        this.texturePath = texturePath;
        this.initialPosition = initialPos;
        this.initialRotation = initialRot;
        this.initialScale = initialScale;
    }

    public GameObjectSaveData(){
        this.name = "";
        this.tag = "";
        this.modelPath = "";
        this.texturePath = "";
        this.initialPosition = new Vector3f(0,0,0);
        this.initialRotation = new Vector3f(0,0,0);
        this.initialScale = new Vector3f(1,1,1);
    }
}

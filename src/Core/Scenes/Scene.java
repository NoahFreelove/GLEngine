package Core.Scenes;

import Core.Objects.GameObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Scene {
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private String name = "Scene";

    public Scene(GameObject[] initObjects, String name){
        gameObjects.addAll(Arrays.asList(initObjects));

        this.name = name;
    }

    public Scene(){

    }

    public ArrayList<GameObject> GameObjects() {
        return gameObjects;
    }

    public String getName() {
        return name;
    }

    public void Add(GameObject object){
        gameObjects.add(object);
    }
    public void Remove(GameObject object){
        gameObjects.remove(object);
    }
}

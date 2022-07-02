package Core.Scenes;

import Core.Objects.GameObject;
import Core.Window;

import java.util.ArrayList;

public class SceneManager {
    private static ArrayList<Scene> buildScenes = new ArrayList<>(0);

    private static Scene currentScene = new Scene();

    public static Scene getSceneByIndex(int i){
        if(i<buildScenes.size())
            return buildScenes.get(i);
        return new Scene();
    }
    public static Scene getSceneByName(String name){
        final Scene[] foundScene = new Scene[]{new Scene()};
        buildScenes.forEach((n) -> {
            if(n.getName().equals(name))
            {
                foundScene[0] = n;
            }
        });
        return foundScene[0];
    }

    public static void SwitchScene(int index){
        if (index < buildScenes.size()) {
            SwitchScene(buildScenes.get(index));
        }
    }

    public static void SwitchScene(Scene scene){
        if(buildScenes.contains(scene))
        {
            for (GameObject o : currentScene.GameObjects()) { o.Unload(); }

            Window.GetInstance().setRenderSource(scene);

            for (GameObject o : scene.GameObjects()) { o.Start(); }

            currentScene = scene;
        }
    }

    public static void AddSceneToBuild(Scene scene){
        buildScenes.add(scene);
    }


}

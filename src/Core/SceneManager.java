package Core;

import java.util.ArrayList;

public class SceneManager {
    private static ArrayList<Scene> buildScenes = new ArrayList<>(0);

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
            Window.GetInstance().setRenderSource(buildScenes.get(index));
            System.out.println("Switching scene");
        }
    }

    public static void SwitchScene(Scene scene){
        if(buildScenes.contains(scene))
        {
            Window.GetInstance().setRenderSource(scene);
        }
    }

    public static void AddSceneToBuild(Scene scene){
        buildScenes.add(scene);
    }


}

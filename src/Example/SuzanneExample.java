package Example;

import Core.Objects.GameObject;
import Core.Scenes.Scene;
import Core.Scenes.SceneManager;
import Core.Window;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class SuzanneExample {

    public static void main(String[] args){
        Window.CreateWindow(1920, 1080, () -> {
            Suzanne suzanne = new Suzanne(new Vector3f(-2,0,0));
            Suzanne suzanne2 = new Suzanne(new Vector3f(2,0,0));
            Skybox skybox = new Skybox(new Vector3f(0,0,0));

            Scene gameScene = new Scene(new GameObject[]{suzanne,suzanne2,skybox}, "GameScene");
            SceneManager.AddSceneToBuild(gameScene);

            SceneManager.SwitchScene(0);
            Window.GetInstance().setBackgroundColor(new Vector4f(0,0.7f,0.7f,0));
        });

    }
}

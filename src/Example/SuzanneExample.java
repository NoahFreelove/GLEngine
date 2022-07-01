package Example;

import Core.Objects.GameObject;
import Core.Scenes.Scene;
import Core.Scenes.SceneManager;
import Core.Window;
import IO.DDS.DDSFile;
import IO.OBJ.OBJLoader;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.File;

public class SuzanneExample {

    public static void main(String[] args){
        Window.CreateWindow(1920, 1080, () -> {
            Suzanne suzanne = new Suzanne(new Vector3f(-2,0,0));
            Suzanne suzanne2 = new Suzanne(new Vector3f(2,0,0));
            Skybox skybox = new Skybox(new Vector3f(0,0,0));
            GameObject text = new GameObject(new Vector3f(-3,2,0), new Vector3f(1,1,1), new Vector3f(1,1,1), OBJLoader.loadModel(new File("src/bin/text.obj")), new DDSFile("src/bin/uvmap.DDS"));
            Scene gameScene = new Scene(new GameObject[]{suzanne,suzanne2,skybox, text}, "GameScene");
            SceneManager.AddSceneToBuild(gameScene);

            SceneManager.SwitchScene(0);
            Window.GetInstance().setBackgroundColor(new Vector4f(0,0.7f,0.7f,0));
        });

    }
}

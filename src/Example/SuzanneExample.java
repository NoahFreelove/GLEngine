package Example;

import Core.Camera;
import Core.Objects.GameObject;
import Core.Scenes.Scene;
import Core.Scenes.SceneManager;
import Core.Window;
import IO.DDS.DDSFile;
import IO.Image;
import IO.OBJ.OBJLoader;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.File;

public class SuzanneExample {

    public static void main(String[] args){
        Window.CreateWindow(1920, 1080, () -> {
            GameObject suzanne = new GameObject(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1,1,1), OBJLoader.loadModel(new File("src/bin/suzanne.obj")), new DDSFile("src/bin/uvmap.DDS"));
            GameObject skybox = new GameObject(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(3,3,3), OBJLoader.loadModel(new File("src/bin/skybox.obj")), new Image("src/bin/skybox.bmp"));

            GameObject camera = new GameObject(new Vector3f(0,1,3), new Vector3f(0,0,0), new Vector3f(1,1,1));
            camera.AddComponent(new Camera());
            Window.GetInstance().ActiveCamera = (Camera)camera.getComponent(0);

            GameObject text = new GameObject(new Vector3f(-3,2,0), new Vector3f(0,0,0), new Vector3f(1,1,1), OBJLoader.loadModel(new File("src/bin/text.obj")), new DDSFile("src/bin/uvmap.DDS"));

            Scene gameScene = new Scene(new GameObject[]{suzanne, skybox, text}, "GameScene");

            SceneManager.AddSceneToBuild(gameScene);
            SceneManager.SwitchScene(0);

            Window.GetInstance().setBackgroundColor(new Vector4f(0,0.7f,0.7f,0));
        });

    }
}

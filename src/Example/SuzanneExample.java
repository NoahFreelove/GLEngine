package Example;

import Core.Camera;
import Core.Objects.GameObject;
import Core.Objects.Models.Model;
import Core.Objects.Models.RenderSettings;
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args){
        Window.CreateWindow(1920, 1080, SuzanneExample::postInit);
    }

    private static void postInit(){
        GameObject suzanne = new GameObject(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1,1,1), new Model(OBJLoader.loadModel(new File("bin/suzanne.obj"))), new DDSFile("bin/uvmap.DDS"));
        GameObject text = new GameObject(new Vector3f(-3,2,0), new Vector3f(0,0,0), new Vector3f(1,1,1), new Model(OBJLoader.loadModel(new File("bin/text.obj"))), new DDSFile("bin/uvmap.DDS"));

        GameObject skybox = new GameObject(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(2,2,2), new Model(OBJLoader.loadModel(new File("bin/skybox.obj"))), new Image("bin/skybox.bmp"));
        skybox.getMeshRenderer().setRenderSettings(new RenderSettings(false,false,true));

        GameObject camera = new GameObject(new Vector3f(0,1,3), new Vector3f(0,0,0), new Vector3f(1,1,1));
        Camera cam = new Camera();
        camera.addComponent(cam);
        Window.GetInstance().ActiveCamera = cam;


        Scene gameScene = new Scene(new GameObject[]{suzanne, skybox, text}, "GameScene");
        SceneManager.AddSceneToBuild(gameScene);
        SceneManager.SwitchScene(0);

        Window.GetInstance().setBackgroundColor(new Vector4f(0,0.7f,0.7f,0));
    }
}

import Core.*;
import IO.DDS.DDSFile;
import IO.Image;
import IO.OBJ.OBJLoader;
import IO.OBJ.Obj;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args){
        Window.CreateWindow(1920, 1080, () -> {
            Obj suzanneModel;
            Obj skyboxModel;
            try {
                suzanneModel = OBJLoader.loadModel(new File("src/bin/suzanne.obj"));
                skyboxModel = OBJLoader.loadModel(new File("src/bin/skybox.obj"));

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            DDSFile suzanneTexture = new DDSFile();
            try {
                suzanneTexture = new DDSFile("src/bin/uvmap.DDS");
            } catch (IOException e) {
                System.out.println("Error loading DDS File: " + e.getMessage());
            }

            GameObject suzanne = new GameObject(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1,1,1), suzanneModel, suzanneTexture);
            GameObject suzanne2 = new GameObject(new Vector3f(2,1,1), new Vector3f(0,0,0), new Vector3f(1,1,1), suzanneModel, suzanneTexture);
            GameObject skybox = new GameObject(new Vector3f(1,1,1), new Vector3f(0,0,0), new Vector3f(1,1,1), skyboxModel, new Image("src/bin/skybox.bmp"));

            Scene gameScene = new Scene(new GameObject[]{suzanne,suzanne2,skybox}, "GameScene");
            SceneManager.AddSceneToBuild(gameScene);

            SceneManager.SwitchScene(0);
            Window.GetInstance().setBackgroundColor(new Vector4f(0,0.7f,0.7f,0));
        });

    }
}

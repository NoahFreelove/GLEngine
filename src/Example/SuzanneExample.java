package Example;

import Core.Objects.Components.Colliders.BoxCollider;
import Core.Objects.Components.Physics.Rigidbody;
import Core.Objects.GameObject;
import Core.Objects.Models.Model;
import Core.Objects.Models.RenderSettings;
import Core.Scenes.World;
import Core.Scenes.WorldManager;
import Core.Window;
import IO.DDS.DDSFile;
import IO.Image;
import IO.OBJ.OBJLoader;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.File;

public class SuzanneExample {
    public static SuzanneController suzanneController;
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args){
        Window.CreateWindow(1920, 1080, SuzanneExample::postInit);
    }

    private static void postInit(){
        World gameWorld = new World();
        WorldManager.AddWorldToBuild(gameWorld);
        WorldManager.SwitchWorld(0);

        GameObject suzanne = new GameObject(new Vector3f(-5,0,0), new Vector3f(0,0,0), new Vector3f(1,1,1), new Model(OBJLoader.loadModel(new File("bin/suzanne.obj"))), new DDSFile("bin/uvmap.DDS"));

        GameObject floor = new GameObject(new Vector3f(0,-5,0), new Vector3f(0,0,0), new Vector3f(10,0.2f,10), new Model(OBJLoader.loadModel(new File("bin/cube.obj"))), new DDSFile("bin/uvmap.DDS"));
        BoxCollider floorCollider = new BoxCollider(new Vector3f(0,-5,0), new Vector3f(10,0.2f,10));
        floor.addComponent(floorCollider);

        GameObject sphere = new GameObject(new Vector3f(0,5,0), new Vector3f(0,0,0), new Vector3f(1,1,1), new Model(OBJLoader.loadModel(new File("bin/sphere.obj"))), new DDSFile("bin/uvmap.DDS"));
        Rigidbody sphereRb = new Rigidbody(new Vector3f(1,1,1));
        sphere.addComponent(sphereRb);

        GameObject skybox = new GameObject(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(2,2,2), new Model(OBJLoader.loadModel(new File("bin/skybox.obj"))), new Image("bin/skybox.bmp"));

        GameObject axisY = new GameObject(new Vector3f(0,0.5f,0), new Vector3f(0,0,0),   new Vector3f(0.5f,0.5f,0.5f), new Model(OBJLoader.loadModel(new File("bin/axis.obj"))), new Image("bin/green.png"));
        GameObject axisX = new GameObject(new Vector3f(0,0,0.5f), new Vector3f(90,0,0),  new Vector3f(0.5f,0.5f,0.5f), new Model(OBJLoader.loadModel(new File("bin/axis.obj"))), new Image("bin/red.png"));
        GameObject axisZ = new GameObject(new Vector3f(0.5f,0,0), new Vector3f(0,0,-90), new Vector3f(0.5f,0.5f,0.5f), new Model(OBJLoader.loadModel(new File("bin/axis.obj"))), new Image("bin/blue.png"));

        // We don't want to cull the skybox because we wouldn't be able to see it!
        skybox.getMeshRenderer().setRenderSettings(new RenderSettings(false,false,true));

        GameObject camera = new GameObject(new Vector3f(0,1,5), new Vector3f(0,0,0), new Vector3f(1,1,1));
        CustomCamera cam = new CustomCamera();
        camera.addComponent(cam);
        Window.GetInstance().ActiveCamera = cam;
        Rigidbody suzanneBody = new Rigidbody(new Vector3f(1,1,1));
        suzanne.addComponent(suzanneBody);

        gameWorld.Add(suzanne, skybox, axisY, axisX, axisZ, camera, floor, sphere);
        
        suzanneController = new SuzanneController(suzanneBody);
        suzanne.addComponent(suzanneController);
        suzanne.addComponent(new SuzanneGizmo());

        cam.setBackgroundColor(new Vector4f(0,0.7f,0.7f,0));
    }
}

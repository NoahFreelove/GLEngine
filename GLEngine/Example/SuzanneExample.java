package Example;

import Core.Objects.Components.Physics.BoundingBox;
import Core.Objects.Components.Physics.Rigidbody;
import Core.Objects.Components.Rendering.Camera;
import Core.Objects.GameObject;
import Core.Objects.Models.Model;
import Core.Objects.Models.RenderSettings;
import Core.Window;
import Core.Worlds.World;
import Core.Worlds.WorldLoader;
import Core.Worlds.WorldManager;
import IO.DDS.DDSFile;
import IO.Image;
import IO.OBJ.OBJLoader;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.File;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class SuzanneExample {
    public static SuzanneController suzanneController;

    //TODO: Implement Audio
    public static void main(String[] args){
        Window.CreateWindow(1920, 1080, SuzanneExample::SetupWorld);
    }

    private static void SetupWorld(){
        World gameWorld = new World();
        WorldManager.AddWorldToBuild(gameWorld);
        WorldManager.SwitchWorld(0);

        WorldLoader.LoadWorldToObject("bin/worlds/world1.txt", gameWorld);

        GameObject suzanne = new GameObject(new Vector3f(-5,0,0), new Vector3f(0,0,0), new Vector3f(1,1,1), new Model(OBJLoader.loadModel(new File("bin/suzanne.obj"))), new DDSFile("bin/uvmap.DDS"));
        suzanne.setName("Suzanne");
        GameObject sphere = new GameObject(new Vector3f(0,5,0), new Vector3f(0,0,0), new Vector3f(1,1,1), new Model(OBJLoader.loadModel(new File("bin/sphere.obj"))), new DDSFile("bin/uvmap.DDS"));
        sphere.setName("Sphere");
        Rigidbody sphereRb = new Rigidbody(new Vector3f(1,1,1));
        sphere.addComponent(sphereRb);
        sphere.addComponent(new BoundingBox());

        // We don't want to cull the skybox because we wouldn't be able to see it!
        GameObject skybox = new GameObject(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(2,2,2), new Model(OBJLoader.loadModel(new File("bin/skybox.obj"))), new Image("bin/skybox.bmp"));
        skybox.getMeshRenderer().setRenderSettings(new RenderSettings(false,false,true));

        GameObject axisY = new GameObject(new Vector3f(0,0.5f,0), new Vector3f(0,0,0),   new Vector3f(0.5f,0.5f,0.5f), new Model(OBJLoader.loadModel(new File("bin/axis.obj"))), new Image("bin/green.png"));
        GameObject axisX = new GameObject(new Vector3f(0,0,0.5f), new Vector3f(90,0,0),  new Vector3f(0.5f,0.5f,0.5f), new Model(OBJLoader.loadModel(new File("bin/axis.obj"))), new Image("bin/red.png"));
        GameObject axisZ = new GameObject(new Vector3f(0.5f,0,0), new Vector3f(0,0,-90), new Vector3f(0.5f,0.5f,0.5f), new Model(OBJLoader.loadModel(new File("bin/axis.obj"))), new Image("bin/blue.png"));

        GameObject camera = new GameObject(new Vector3f(0,1,5), new Vector3f(0,0,0), new Vector3f(1,1,1));
        GameObject cameraModel = new GameObject(camera.getPosition(), new Vector3f(0,90,0), new Vector3f(1,1,1), new Model(OBJLoader.loadModel(new File("bin/camera.obj"))), new DDSFile("bin/uvmap.DDS"));

        Camera cam = new Camera();

        cam.setBackgroundColor(new Vector4f(0,0.7f,0.7f,0));
        camera.addComponent(cam);
        camera.addComponent(new CameraController(cam, cameraModel));
        Window.GetInstance().setActiveCamera(cam);

        suzanne.addChild(sphere);

        gameWorld.Add(suzanne, skybox, camera, sphere);
        gameWorld.AddGizmo(axisX, axisY, axisZ);

        Rigidbody suzanneBody = new Rigidbody(suzanne.getScale(), 1);
        suzanne.addComponent(suzanneBody);

        Camera cam2 = new Camera();
        suzanne.addComponent(cam2);
        suzanneController = new SuzanneController(suzanneBody, cam, cam2, cameraModel);
        suzanne.addComponent(suzanneController);
        suzanne.addComponent(new BoundingBox());

        gameWorld.addRigidBody(suzanneBody.getRigidBody());
    }
}

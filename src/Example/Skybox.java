package Example;

import Core.Objects.GameObject;
import IO.Image;
import IO.OBJ.OBJLoader;
import org.joml.Vector3f;

import java.io.File;

public class Skybox extends GameObject {

    public Skybox(Vector3f initialPosition) {
        super(initialPosition, new Vector3f(0,0,0), new Vector3f(1,1,1), OBJLoader.loadModel(new File("src/bin/skybox.obj")), new Image("src/bin/skybox.bmp"));
    }
}

package Example;

import Core.Objects.GameObject;
import IO.DDS.DDSFile;
import IO.OBJ.OBJLoader;
import org.joml.Vector3f;

import java.io.File;

public class Suzanne extends GameObject {

    public Suzanne(Vector3f initialPosition){
        super(initialPosition, new Vector3f(0,0,0), new Vector3f(1,1,1), OBJLoader.loadModel(new File("src/bin/suzanne.obj")), new DDSFile("src/bin/uvmap.DDS"));
    }

    @Override
    public void Start() {
        System.out.println("Suzanne started");
    }

    @Override
    public void OnInstantiate() {
        System.out.println("Suzanne instantiated");
    }
}

package GLEngine.Core.Worlds;

import GLEngine.Core.Objects.Components.Colliders.BoxCollider;
import GLEngine.Core.Objects.GameObject;
import GLEngine.Core.Objects.Models.Model;
import GLEngine.IO.Image;
import org.joml.Vector3f;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Keeping the scope of the loader to pretty small for now.
public class WorldLoader {

    public static void LoadWorldToObject(String path, World world){
        String[] loadedWorld = readFile(path);
        Process(loadedWorld, world);
    }

    public static World LoadWorld(String path){
        World w = new World();
        LoadWorldToObject(path, w);
        return w;
    }

    private static String[] readFile(String path){
        Path filePath = new File(path).toPath();
        List<String> stringList = new ArrayList<>(0);
        try {
            stringList  = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        }
        catch (Exception e){
            System.err.printf("Could not load world from: %s. Error: %s%n", path, e.getMessage());
        }
        return stringList.toArray(new String[]{});
    }



    private static void Process(String[] data, World world){
        int lineNum = 0;
        boolean inGameObject = false;
        GameObject object;
        Vector3f pos = new Vector3f();
        Vector3f rot = new Vector3f();
        Vector3f sca = new Vector3f();

        Model    model = new Model();
        Image    image = new Image();
        String   name  = "GameObject";

        for (String line : data) {
            if(line.equals("///START GAMEOBJECT///")){
                inGameObject = true;
                lineNum++;
                continue;
            }

            if(line.equals("///END GAMEOBJECT///")){
                inGameObject = false;
                lineNum++;

                object = new GameObject(pos,rot,sca,model,image);
                object.setName(name);
                object.addComponent(BoxCollider.GenerateBoxColliderForObject(object, true));
                world.Add(object);

                pos = new Vector3f();
                rot = new Vector3f();
                sca = new Vector3f();
                model = new Model();
                image = new Image();

                continue;
            }

            if(inGameObject){

                if(line.startsWith("NAME")){
                    line = cleanLine(line);
                    name = line;
                }

                if(line.startsWith("POS")){
                    line = cleanLine(line);

                    String[] subStr = line.split(",");
                    if(subStr.length == 3){
                        pos.x = Float.parseFloat(subStr[0]);
                        pos.y = Float.parseFloat(subStr[1]);
                        pos.z = Float.parseFloat(subStr[2]);
                    }
                    else System.err.println("Level Loader: Error parsing gameobject");
                    lineNum++;
                    continue;
                }
                if(line.startsWith("ROT")){
                    line = cleanLine(line);

                    String[] subStr = line.split(",");
                    if(subStr.length == 3){
                        rot.x = Float.parseFloat(subStr[0]);
                        rot.y = Float.parseFloat(subStr[1]);
                        rot.z = Float.parseFloat(subStr[2]);
                    }
                    else System.err.println("Level Loader: Error parsing gameobject");
                    lineNum++;
                    continue;
                }
                if(line.startsWith("SCA")){
                    line = cleanLine(line);

                    String[] subStr = line.split(",");
                    if(subStr.length == 3){
                        sca.x = Float.parseFloat(subStr[0]);
                        sca.y = Float.parseFloat(subStr[1]);
                        sca.z = Float.parseFloat(subStr[2]);
                    }
                    else System.err.println("Level Loader: Error parsing gameobject");
                    lineNum++;
                    continue;
                }

                if(line.startsWith("MOD")){
                    line = cleanLine(line);
                    model = new Model(line);
                    lineNum++;
                    continue;
                }
                if(line.startsWith("TEX")){
                    line = cleanLine(line);
                    image = new Image(line);
                    lineNum++;
                    continue;
                }

            }

        }
    }

    private static String cleanLine(String input)
    {
        input = input.replace("NAME ", "");
        input = input.replace("POS ", "");
        input = input.replace("ROT ", "");
        input = input.replace("SCA ", "");
        input = input.replace("MOD ", "");
        input = input.replace("TEX ", "");

        input = input.replace(" ", "");

        input = input.replace("CLASS ", "");
        input = input.replace("DIM ", "");

        input = input.replace("(", "");
        input = input.replace(")", "");
        input = input.replace("\"", "");
        return input;
    }
}

package GLEngine.Core.Worlds;

import GLEngine.Core.Objects.Components.Colliders.BoxCollider;
import GLEngine.Core.Objects.Components.Component;
import GLEngine.Core.Objects.GameObject;
import GLEngine.Core.Objects.Models.Model;
import GLEngine.IO.Image;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Keeping the scope of the loader to pretty small for now.
public class WorldLoader {

    public static void LoadWorldToObject(String path, World world, boolean loadComponents){
        String[] loadedWorld = readFile(path);
        Process(loadedWorld, world, loadComponents);
    }

    public static World LoadWorldObject(String path){
        World w = new World();
        LoadWorldToObject(path, w, true);
        return w;
    }

    public static void LoadWorld(String path){
        World w = new World();
        LoadWorldToObject(path, w, true);
        WorldManager.SwitchWorld(w);
    }

    public static World PreviewWorld(String path){
        World w = new World();
        LoadWorldToObject(path, w, false);
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



    private static void Process(String[] data, World world, boolean loadComponents){
        int lineNum = 0;
        boolean inGameObject = false;
        boolean inComponent = false;
        GameObject object;
        ArrayList<Component> components = new ArrayList<>(0);
        int componentCount = 0;
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

            if(line.equals("///COMP///")){
                inComponent = true;
                lineNum++;
                continue;
            }

            if(line.equals("///END COMP///")){
                inComponent = false;

                lineNum++;
                continue;
            }

            if(line.equals("///END GAMEOBJECT///")){
                inGameObject = false;
                lineNum++;

                object = new GameObject(pos,rot,sca,model,image);
                object.setName(name);
                object.addComponent(BoxCollider.GenerateBoxColliderForObject(object, true));

                for (Component c :
                        components) {
                    object.addComponent(c);
                    c.OnCreated();
                }

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

            if(inComponent && loadComponents){
                if(line.startsWith("CLASS")){
                    line = cleanLine(line);
                    try {
                        // Convert class name to class object
                        Class<?> clazz = Class.forName(line);
                        // Create constructor for class
                        Constructor<?> constructor = clazz.getConstructor();
                        // Create instance of class without casting
                        if(constructor.newInstance() instanceof Component){
                            components.add((Component) constructor.newInstance());
                            componentCount++;
                        }
                        else System.err.println("Level Loader: Error parsing component");
                        // Set
                    } catch (Exception e) {
                        System.err.println("Level Loader: Error parsing component: " + e.getMessage());
                    }
                }
                if(line.startsWith("FIELD")){
                    line = cleanLine(line);
                    String[] subStr = line.split(":");
                    if(subStr.length == 3){
                        String fieldName = subStr[0];
                        String fieldValue = subStr[1];
                        String fieldType = subStr[2];
                        try {
                            Field field = components.get(componentCount-1).getClass().getField(fieldName);
                            switch (fieldType){
                                case "int"->{
                                    field.setInt(components.get(componentCount-1), Integer.parseInt(fieldValue));
                                }
                                case "float"->{
                                    field.setFloat(components.get(componentCount-1), Float.parseFloat(fieldValue));
                                }
                                case "boolean"->{
                                    field.setBoolean(components.get(componentCount-1), Boolean.parseBoolean(fieldValue));
                                }
                                case "Vector3f"->{
                                    String[] subStr2 = fieldValue.split(",");
                                    if(subStr2.length == 3){
                                        Vector3f vec = new Vector3f();
                                        vec.x = Float.parseFloat(subStr2[0]);
                                        vec.y = Float.parseFloat(subStr2[1]);
                                        vec.z = Float.parseFloat(subStr2[2]);
                                        field.set(components.get(componentCount-1), vec);
                                    }
                                    else System.err.println("Level Loader: Error parsing Vector3f");
                                }
                                case "Vector2f" ->{
                                    String[] subStr2 = fieldValue.split(",");
                                    if(subStr2.length == 2){
                                        Vector2f vec = new Vector2f();
                                        vec.x = Float.parseFloat(subStr2[0]);
                                        vec.y = Float.parseFloat(subStr2[1]);
                                        field.set(components.get(componentCount-1), vec);
                                    }
                                    else System.err.println("Level Loader: Error parsing Vector2f");
                                }
                                default -> {
                                    field.set(components.get(componentCount-1), fieldValue);
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Level Loader: Error parsing component: " + e.getMessage());
                        }
                    }
                    else System.err.println("Level Loader: Error parsing component");
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
        input = input.replace("CLASS ", "");
        input = input.replace("FIELD ", "");

        input = input.replace(" ", "");

        input = input.replace("CLASS ", "");
        input = input.replace("DIM ", "");

        input = input.replace("(", "");
        input = input.replace(")", "");
        input = input.replace("\"", "");
        return input;
    }
}

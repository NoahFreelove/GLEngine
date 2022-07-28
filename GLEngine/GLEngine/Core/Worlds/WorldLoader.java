package GLEngine.Core.Worlds;

import GLEngine.Core.Interfaces.EditorVisible;
import GLEngine.Core.Objects.Components.Colliders.BoxCollider;
import GLEngine.Core.Objects.Components.Component;
import GLEngine.Core.Objects.GameObject;
import GLEngine.Core.Objects.GameObjectSaveData;
import GLEngine.Core.Objects.Models.Model;
import GLEngine.Core.Objects.Transform;
import GLEngine.IO.Image;
import GLEngine.Logging.LogType;
import GLEngine.Logging.Logger;
import org.joml.Quaternionf;
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

    public static String binPathPrefix = "";

    public static void LoadWorldToObject(String path, World world, boolean loadComponents, boolean dummyLoad){
        String[] loadedWorld = readFile(path);
        Process(loadedWorld, world, loadComponents, dummyLoad);
    }

    public static World LoadWorldObject(String path){
        World w = new World();
        LoadWorldToObject(path, w, true, false);
        return w;
    }

    public static void LoadWorld(String path){
        World w = new World();
        LoadWorldToObject(path, w, true, false);
        WorldManager.SwitchWorld(w);
    }

    public static World PreviewWorld(String path, String binPath){
        World w = new World();
        binPathPrefix = binPath;
        LoadWorldToObject(path, w, false, false);
        return w;
    }

    public static World DummyWorld(String path){
        World w = new World();
        LoadWorldToObject(path, w, true, true);
        return w;
    }

    private static String[] readFile(String path)
    {
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



    private static void Process(String[] data, World world, boolean loadComponents, boolean dummyLoad){
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
        String   tag   = "Tag";
        GameObjectSaveData saveData = new GameObjectSaveData();

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
                if(!dummyLoad){
                    object = new GameObject(pos,rot,sca,model,image);
                    object.addComponent(BoxCollider.GenerateBoxColliderForObject(object, true));
                }
                else {
                    object = new GameObject(pos,rot,sca);
                    object.setSaveData(saveData);
                }

                object.setName(name);
                object.setTag(tag);
                saveData.name = name;
                saveData.tag = tag;

                if(dummyLoad){
                    object.addComponent(new Transform(pos,rot,sca));
                }

                for (Component c :
                        components) {
                    object.addComponent(c);
                    c.setParent(object);
                    c.OnCreated();
                }

                world.Add(object);

                pos = new Vector3f();
                rot = new Vector3f();
                sca = new Vector3f();
                model = new Model();
                image = new Image();
                components = new ArrayList<>(0);
                componentCount = 0;
                name = "GameObject";
                tag = "Tag";
                saveData = new GameObjectSaveData();
                continue;
            }

            if(inGameObject){

                if(line.startsWith("NAME")){
                    line = cleanLine(line);
                    name = line;
                    saveData.name = name;
                }

                if(line.startsWith("TAG")){
                    line = cleanLine(line);
                    tag = line;
                    saveData.tag = tag;
                }

                if(line.startsWith("POS")){
                    line = cleanLine(line);

                    String[] subStr = line.split(",");
                    if(subStr.length == 3){
                        pos.x = Float.parseFloat(subStr[0]);
                        pos.y = Float.parseFloat(subStr[1]);
                        pos.z = Float.parseFloat(subStr[2]);
                        saveData.initialPosition = pos;
                    }
                    else System.err.println("Level Loader: Error parsing GameObject");
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
                        saveData.initialRotation = rot;
                    }
                    else System.err.println("Level Loader: Error parsing GameObject");
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
                        saveData.initialScale = sca;
                    }
                    else System.err.println("Level Loader: Error parsing GameObject");
                    lineNum++;
                    continue;
                }

                if(line.startsWith("MOD")){
                    line = cleanLine(line);

                    if(dummyLoad){
                        saveData.modelPath = line;
                    }
                    else if (!loadComponents){
                        model = new Model(binPathPrefix + "/"+line);
                    }
                    else{
                        model = new Model(line);
                    }
                    lineNum++;
                    continue;
                }
                if(line.startsWith("TEX")){
                    line = cleanLine(line);
                    if(dummyLoad){
                        saveData.texturePath = line;
                    }
                    else if (!loadComponents){
                        saveData.texturePath = binPathPrefix  + "/"+line;
                        image = new Image(saveData.texturePath);
                    }
                    else {
                        image = new Image(line);
                    }
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
                        if(clazz == Transform.class){
                            throw new Exception("Not allowed to attach the Transform component to GameObjects. Please remove it from the file.");
                        }
                        // Create constructor for class
                        Constructor<?> constructor = clazz.getConstructor();
                        // Create instance of class without casting
                        if(constructor.newInstance() instanceof Component){
                            components.add((Component) constructor.newInstance());
                            componentCount++;
                        }
                        else Logger.log("Level Loader: Error parsing component", LogType.Error);
                    } catch (Exception e) {
                        Logger.log("Level Loader: Error parsing component", LogType.Error);
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
                            Field field;
                            if(fieldName.equals("enabled")){
                                field = Component.class.getDeclaredField("enabled");
                                field.setAccessible(true);
                                field.set(components.get(componentCount-1), Boolean.parseBoolean(fieldValue));
                                continue;
                            }

                            field = components.get(componentCount-1).getClass().getDeclaredField(fieldName);

                            field.setAccessible(true);
                            if(!field.isAnnotationPresent(EditorVisible.class))
                                continue;
                            switch (fieldType){
                                case "int", "Integer"->{
                                    field.set(components.get(componentCount-1), Integer.parseInt(fieldValue));
                                }
                                case "float", "Float"->{
                                    field.set(components.get(componentCount-1), Float.parseFloat(fieldValue));
                                }
                                case "boolean", "Boolean"->{
                                    field.set(components.get(componentCount-1), Boolean.parseBoolean(fieldValue));
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
                                case "Quaternionf"->{
                                    String[] subStr2 = fieldValue.split(",");
                                    if(subStr2.length == 4){
                                        Quaternionf quat = new Quaternionf();
                                        quat.x = Float.parseFloat(subStr2[0]);
                                        quat.y = Float.parseFloat(subStr2[1]);
                                        quat.z = Float.parseFloat(subStr2[1]);
                                        quat.w = Float.parseFloat(subStr2[1]);
                                        field.set(components.get(componentCount-1), quat);
                                    }
                                    else System.err.println("Level Loader: Error parsing Quaternionf");
                                }
                                case "String" -> field.set(components.get(componentCount-1), fieldValue);
                                default -> {
                                    field.set(components.get(componentCount-1), fieldValue);
                                }
                            }
                        } catch (Exception e) {
                            //System.err.println("Level Loader: Error parsing component: " + e);
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
        input = input.replace("TAG ", "");
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

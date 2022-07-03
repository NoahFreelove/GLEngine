package Core.Scenes;

import Core.Objects.GameObject;
import Core.Window;

import java.util.ArrayList;

public class WorldManager {
    private static ArrayList<World> buildWorlds = new ArrayList<>(0);

    private static World currentWorld = new World();

    public static World getSceneByIndex(int i){
        if(i< buildWorlds.size())
            return buildWorlds.get(i);
        return new World();
    }
    public static World getWorldByName(String name){
        final World[] foundWorld = new World[]{new World()};
        buildWorlds.forEach((n) -> {
            if(n.getName().equals(name))
            {
                foundWorld[0] = n;
            }
        });
        return foundWorld[0];
    }

    public static void SwitchWorld(int index){
        if (index < buildWorlds.size()) {
            SwitchWorld(buildWorlds.get(index));
        }
    }

    public static void SwitchWorld(World world){
        if(buildWorlds.contains(world))
        {
            for (GameObject o : currentWorld.GameObjects()) { o.Unload(); }

            Window.GetInstance().setRenderSource(world);

            for (GameObject o : world.GameObjects()) { o.Start(); }

            currentWorld = world;
        }
    }

    public static void AddWorldToBuild(World world){
        buildWorlds.add(world);
    }

    public static World getCurrentWorld() {
        return currentWorld;
    }

}

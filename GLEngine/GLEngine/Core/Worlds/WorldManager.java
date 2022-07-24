package GLEngine.Core.Worlds;

import GLEngine.Core.Objects.GameObject;
import GLEngine.Core.Window;

import java.util.ArrayList;

public class WorldManager {
    private static final ArrayList<World> buildWorlds = new ArrayList<>(0);

    private static World currentWorld = new World();
    private static World loadingWorld = new World();

    private static boolean enableGizmos = true;

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
            currentWorld.setActive(false);
            Window.GetInstance().setRenderSource(world);

            for (GameObject o : world.GameObjects()) { o.Start(); }
            world.setActive(true);
            currentWorld = world;
            loadingWorld = world;
        }
    }

    public static void AddWorldToBuild(World world){
        buildWorlds.add(world);
    }

    public static World getCurrentWorld() {
        return currentWorld;
    }

    public static World getLoadingWorld(){
        return loadingWorld;
    }

    public static boolean areGizmosEnabled() {
        return enableGizmos;
    }

    public static void setEnableGizmos(boolean enableGizmos) {
        WorldManager.enableGizmos = enableGizmos;
    }

    public static void setLoadingWorld(World loadingWorld) {
        WorldManager.loadingWorld = loadingWorld;
    }
}

package Core.Input;

import Core.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    static long window;
    public static boolean isKeyPressed(int code){
        window = Window.GetInstance().getWindowHandle();

        return glfwGetKey(window, code) == GLFW_PRESS;
    }

    public static boolean isKeyReleased(int code){
        window = Window.GetInstance().getWindowHandle();

        return glfwGetKey(window, code) == GLFW_RELEASE;
    }
}

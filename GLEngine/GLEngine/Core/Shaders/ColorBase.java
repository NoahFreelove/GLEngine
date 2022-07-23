package GLEngine.Core.Shaders;

public record ColorBase() {
    //Basic
    public static final Color RED = new Color(255,0,0);
    public static final Color BLUE = new Color(0,0,255);
    public static final Color GREEN = new Color(0,255,0);
    public static final Color WHITE = new Color(255,255,255);
    public static final Color BLACK = new Color(0,0,0);
    public static final Color GRAY = new Color(128,128,128);

    // Other
    public static final Color ORANGE = new Color(255,165,0);
    public static final Color PURPLE = new Color(128,0,128);
}

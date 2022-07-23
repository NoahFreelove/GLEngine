package GLEngine.Core.Shaders;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Color {
    private float r;
    private float g;
    private float b;
    private float a;

    public Color(){
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        clampOne(this);
    }

    public Color(float r, float g, float b){
        this.r = r/255;
        this.g = g/255;
        this.b = b/255;
        this.a = 1;
        clampOne(this);
    }

    private void clampOne(Color c){
        c.setColor(clamp(0,1,c.r),clamp(0,1,c.g),clamp(0,1,c.b),clamp(0,1,c.a));
    }

    private float clamp (float min, float max, float v){
        if(v<min)
            return min;
        return Math.min(v, max);
    }

    public float R(){
        return r;
    }

    public float G(){
        return g;
    }

    public float B(){
        return b;
    }

    public float A(){
        return a;
    }

    public void setColorRGB(float R, float G, float B){
        setColor(clamp(0,1,R/255),clamp(0,1,G/255),clamp(0,1,B/255),1);
    }

    public void setColor(float R, float G, float B, float A){
        r = R;
        g = G;
        b = B;
        a = A;
    }

    public Vector3f getRGB(){
        return new Vector3f((float) Math.floor(r*255),(float) Math.floor(g*255), (float) Math.floor(b*255));
    }
    public Vector4f getColor(){
        return new Vector4f(r,g,b,a);
    }

}

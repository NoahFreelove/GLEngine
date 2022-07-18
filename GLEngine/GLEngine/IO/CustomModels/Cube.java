package GLEngine.IO.CustomModels;

import org.joml.Vector3f;

public class Cube extends CustomModel{

    public Cube(Vector3f dimensions){

        float[] points = new float[]{
                -dimensions.x(),-dimensions.y(),-dimensions.z(),
                -dimensions.x(),-dimensions.y(), dimensions.z(),
                -dimensions.x(), dimensions.y(), dimensions.z(),
                 dimensions.x(), dimensions.y(),-dimensions.z(),
                -dimensions.x(),-dimensions.y(),-dimensions.z(),
                -dimensions.x(), dimensions.y(),-dimensions.z(),
                 dimensions.x(),-dimensions.y(), dimensions.z(),
                -dimensions.x(),-dimensions.y(),-dimensions.z(),
                 dimensions.x(),-dimensions.y(),-dimensions.z(),
                 dimensions.x(), dimensions.y(),-dimensions.z(),
                 dimensions.x(),-dimensions.y(),-dimensions.z(),
                -dimensions.x(),-dimensions.y(),-dimensions.z(),
                -dimensions.x(),-dimensions.y(),-dimensions.z(),
                -dimensions.x(), dimensions.y(), dimensions.z(),
                -dimensions.x(), dimensions.y(),-dimensions.z(),
                 dimensions.x(),-dimensions.y(), dimensions.z(),
                -dimensions.x(),-dimensions.y(), dimensions.z(),
                -dimensions.x(),-dimensions.y(),-dimensions.z(),
                -dimensions.x(), dimensions.y(), dimensions.z(),
                -dimensions.x(),-dimensions.y(), dimensions.z(),
                 dimensions.x(),-dimensions.y(), dimensions.z(),
                 dimensions.x(), dimensions.y(), dimensions.z(),
                 dimensions.x(),-dimensions.y(),-dimensions.z(),
                 dimensions.x(), dimensions.y(),-dimensions.z(),
                 dimensions.x(),-dimensions.y(),-dimensions.z(),
                 dimensions.x(), dimensions.y(), dimensions.z(),
                 dimensions.x(),-dimensions.y(), dimensions.z(),
                 dimensions.x(), dimensions.y(), dimensions.z(),
                 dimensions.x(), dimensions.y(),-dimensions.z(),
                -dimensions.x(), dimensions.y(),-dimensions.z(),
                 dimensions.x(), dimensions.y(), dimensions.z(),
                -dimensions.x(), dimensions.y(),-dimensions.z(),
                -dimensions.x(), dimensions.y(), dimensions.z(),
                 dimensions.x(), dimensions.y(), dimensions.z(),
                -dimensions.x(), dimensions.y(), dimensions.z(),
                 dimensions.x(),-dimensions.y(), dimensions.z()
        };

        Vector3f[] vertices = new Vector3f[points.length/3];

        for (int i = 0; i < points.length; i+=3){
            vertices[i/3] = new Vector3f(points[i], points[i+1], points[i+2]);
        }

        super.vertices = vertices;
    }
}

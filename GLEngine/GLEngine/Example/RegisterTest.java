package GLEngine.Example;

import GLEngine.Core.Interfaces.EditorVisible;
import GLEngine.Core.Objects.Components.Component;
import org.joml.Vector3f;

public class RegisterTest extends Component {

    @EditorVisible
    public Vector3f value = new Vector3f(0,0,0);
    @EditorVisible
    private int hidden = 5;

    int delay = 60;

    @Override
    public void Update(float dt){
        delay--;
        if(delay == 0){
            delay = 60;
            System.out.println("value: " + value);
            System.out.println("shhh: " + hidden);
        }
    }
}
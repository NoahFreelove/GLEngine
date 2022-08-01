package GLEngine.Core.Audio;

import GLEngine.Core.Interfaces.EditorVisible;
import GLEngine.Core.Objects.Components.Component;

public class SoundPlayer extends Component {
    private Sound sound;

    @EditorVisible
    private String filePath;

    @EditorVisible
    private boolean loops;

    public SoundPlayer(String filepath, boolean doesLoop) {
        sound = new Sound(filepath, doesLoop);
    }

    @Override
    public void OnCreated(){
        sound = new Sound(filePath, loops);
    }

    public void play(){
        sound.play();
    }

    public void stop(){
        sound.stop();
    }

    public void restart(){
        sound.stop();
        sound.play();
    }
}

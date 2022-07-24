package GLEngine.Core.Audio;

import java.io.File;


public class PlaySound {
    public static void play(){
        Sound s = new Sound(new File("bin/sound.ogg").getAbsolutePath(),false);
        s.play();
    }
}

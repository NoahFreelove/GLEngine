package GLEngine.Core.Audio;

import GLEngine.Core.Worlds.World;
import GLEngine.Core.Worlds.WorldManager;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.EXTEfx;
import org.lwjgl.stb.STBVorbis;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound {
    private int bufferID;
    private int sourceID;

    private String fp;

    private boolean isPlaying = false;

    private World activeWorld;

    public Sound(String fp, boolean loops){
        this.fp = fp;

        this.activeWorld = WorldManager.getLoadingWorld();
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(fp, channelsBuffer, sampleRateBuffer);

        if(rawAudioBuffer == null){
            System.out.println("Error loading sound file: " + fp);
            stackPop();
            stackPop();
            return;
        }


        int channels = channelsBuffer.get(0);
        int sampleRate = sampleRateBuffer.get(0);

        stackPop();
        stackPop();

        //Find correct format
        int format = -1;
        if(channels == 1){
            format = AL10.AL_FORMAT_MONO16;
        }else if(channels == 2){
            format = AL10.AL_FORMAT_STEREO16;
        }

        bufferID = AL10.alGenBuffers();

        alBufferData(bufferID, format, rawAudioBuffer, sampleRate);

        // Generate source
        sourceID = alGenSources();
        alSourcei(sourceID, AL10.AL_BUFFER, bufferID);
        alSourcei(sourceID, AL10.AL_LOOPING, loops?1:0);

        alSourcei(sourceID, AL_POSITION, 0);

        final int filter = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(filter, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
        EXTEfx.alFilterf(filter, EXTEfx.AL_LOWPASS_GAIN, 25f);
        EXTEfx.alFilterf(filter, EXTEfx.AL_LOWPASS_GAINHF, 0f);
        AL10.alSourcei(sourceID, EXTEfx.AL_DIRECT_FILTER, filter);
        alSourcef(sourceID, AL_MAX_GAIN, 100f);
        alSourcef(sourceID, AL_GAIN, 10f);

        free(rawAudioBuffer);
    }

    public void delete(){
        alDeleteSources(sourceID);
        alDeleteBuffers(bufferID);
    }

    public void play(){
        // Dont play if scene isn't active
        if(WorldManager.getCurrentWorld() != activeWorld)
            return;

        int state = alGetSourcei(sourceID, AL_SOURCE_STATE);

        if(state == AL_STOPPED)
        {
            isPlaying = false;
            alSourcei(sourceID, AL_POSITION, 0);
        }

        if(!isPlaying){
            alSourcePlay(sourceID);
            isPlaying = true;
        }
    }

    public void stop(){
        if(isPlaying){
            alSourceStop(sourceID);
            isPlaying = false;
        }
    }

    public String getFp() {
        return fp;
    }

    public boolean isPlaying() {
        int state = alGetSourcei(sourceID, AL_SOURCE_STATE);
        return state == AL_PLAYING;
    }


}

package io.github.some_example_name.lwjgl3.outputs;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class OutputManager implements iOutputManager {
    private final AudioManager audio = new AudioManager();

    private Map<String, Music> musicTracks = new HashMap<>();
    private Music currentMusic = null;
    private float globalMusicVolume = 1.0f; // 1.0f is 100%
    private float globalSfxVolume = 1.0f;
    
    public void loadAudio(String id, String path) {
        audio.load(id, path);
    }

    public void playSound(String id) {
        audio.play(id);
    }
    
    public void loadMusic(String action, String filePath) {
        musicTracks.put(action, Gdx.audio.newMusic(Gdx.files.internal(filePath)));
    }

    public void playMusic(String action, boolean loop) {
        Music nextMusic = musicTracks.get(action);
        if (nextMusic == null) {
            System.err.println("Music track not found: " + action);
            return;
        }

        // MAGIC TRICK: If the exact same song is already playing, do nothing!
        if (currentMusic == nextMusic && currentMusic.isPlaying()) {
            return; 
        }

        // Stop the old song if one is playing
        if (currentMusic != null) {
            currentMusic.stop();
        }

        // Start the new song
        currentMusic = nextMusic;
        currentMusic.setLooping(loop);
        currentMusic.setVolume(globalMusicVolume);
        currentMusic.play();
    }

    public void setMusicVolume(float volume) {
        this.globalMusicVolume = volume;
        if (currentMusic != null) {
            currentMusic.setVolume(this.globalMusicVolume);
        }
    }

    public void setSfxVolume(float volume) {
        this.globalSfxVolume = volume;
        // You can multiply your Sound effects by this variable in your playAudio/playSound method!
    }

    public void dispose() {
        audio.dispose();
    }
}
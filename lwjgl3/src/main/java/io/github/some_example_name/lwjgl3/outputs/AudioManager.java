package io.github.some_example_name.lwjgl3.outputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private final Map<String, Sound> soundMap = new HashMap<>();

    public void load(String id, String path) {
        if (Gdx.files.internal(path).exists()) {
            soundMap.put(id, Gdx.audio.newSound(Gdx.files.internal(path)));
        } else {
            System.err.println("AUDIO ERROR: Could not find file: " + path);
        }
    }

    public void play(String id) {
        if (soundMap.containsKey(id)) {
            soundMap.get(id).play();
        }
    }

    public void dispose() {
        for (Sound s : soundMap.values()) {
            s.dispose();
        }
        soundMap.clear();
    }
}
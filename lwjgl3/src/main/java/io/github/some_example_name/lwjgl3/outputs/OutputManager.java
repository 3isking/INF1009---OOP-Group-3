package io.github.some_example_name.lwjgl3.outputs;

public class OutputManager {
    private final AudioManager audio = new AudioManager();

    public void loadAudio(String id, String path) {
        audio.load(id, path);
    }

    public void playSound(String id) {
        audio.play(id);
    }

    public void dispose() {
        audio.dispose();
    }
}
package io.github.some_example_name.lwjgl3.outputs;

public interface iOutputManager {
    void loadAudio(String id, String path);
    void playSound(String id);
    void dispose();
}
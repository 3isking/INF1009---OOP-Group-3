package io.github.some_example_name.lwjgl3.outputs;

public interface iOutputManager {
    void loadAudio(String id, String path);
    void playSound(String id);
    void dispose();
    void loadMusic(String action, String filePath);
    void playMusic(String action, boolean loop);
    void setMusicVolume(float volume);
    void setSfxVolume(float volume);
}
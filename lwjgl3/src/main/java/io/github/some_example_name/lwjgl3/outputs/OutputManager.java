package io.github.some_example_name.lwjgl3.outputs;

public class OutputManager {
    private final AudioManager audio = new AudioManager();
    private final HapticManager haptics = new HapticManager();

    public void loadAudio(String id, String path) {
        audio.load(id, path);
    }

    public void playSound(String id) {
        audio.play(id);
    }

    public void triggerVibration(int ms) {
        haptics.vibrate(ms);
    }

    public void dispose() {
        audio.dispose();
    }
}
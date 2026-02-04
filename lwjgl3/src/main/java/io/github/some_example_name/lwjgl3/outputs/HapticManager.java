package io.github.some_example_name.lwjgl3.outputs;

import com.badlogic.gdx.Gdx;

public class HapticManager {
    public void vibrate(int milliseconds) {
        // Prevents crashes on desktop where input might not support vibration
        if (Gdx.input != null) {
            try {
                Gdx.input.vibrate(milliseconds);
            } catch (Exception e) {
                // Ignore vibration errors on unsupported devices
            }
        }
    }
}
package io.github.some_example_name.lwjgl3.inputs;

import com.badlogic.gdx.math.Vector2;

public interface iInputManager {
    boolean inputPressed(String action);
    boolean inputHeld(String action);
    Vector2 getMousePosition();
    void setKeyBind(String action);
    boolean keyDown(int keycode);
    boolean touchDown(int x, int y, int pointer, int button);
    Camera getCamera();
    void usePlayerCamera();
    void useFreeCamera();
    void useDefaultCamera();
    int getMappedKey(String action);
    String getWaitingForBind();
}
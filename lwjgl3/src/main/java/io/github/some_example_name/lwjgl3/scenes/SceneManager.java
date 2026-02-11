package io.github.some_example_name.lwjgl3.scenes;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SceneManager {
    private Map<String, Scene> scenes;
    private Scene currentScene;

    public SceneManager() {
        this.scenes = new HashMap<>();
    }

    public void addScene(Scene scene) {
        scenes.put(scene.getName(), scene);
    }

    public Scene getCurrentScene() {
        return this.currentScene;
    }

    public void setCurrentScene(Scene scene) {
        if (this.currentScene != null) {
            this.currentScene.onExit();
        }
        this.currentScene = scene;
        if (this.currentScene != null) {
            this.currentScene.onEnter();
        }
    }

    public void update(float deltaTime) {
        if (currentScene != null) {
            currentScene.update(deltaTime);
        }
    }

    public void render(SpriteBatch batch) {
        if (currentScene != null) {
            currentScene.render(batch);
    }
}
}
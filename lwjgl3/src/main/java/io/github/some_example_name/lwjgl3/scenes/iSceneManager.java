package io.github.some_example_name.lwjgl3.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface iSceneManager {
    void addScene(Scene scene);
    Scene getCurrentScene();
    void setCurrentScene(String sceneName);
    void setCurrentScene(Scene scene);
    void initializeClassroomScene();
    void initializeScene1();
    void initializeScene2();
    void initializeScene3();
    void update(float deltaTime);
    void setProjectionMatrix(com.badlogic.gdx.math.Matrix4 matrix);
    void render(SpriteBatch batch);
}
package io.github.some_example_name.lwjgl3.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.some_example_name.lwjgl3.outputs.iOutputManager; // Added this missing import!

public interface iSceneManager {
    void addScene(Scene scene);
    Scene getCurrentScene();
    void setCurrentScene(String sceneName);
    void setCurrentScene(Scene scene);
    
    void openOverlay(String sceneName);
    void closeOverlay();
    boolean isOverlayActive();
    
    void initializeClassroomScene();
    void initializeMainMenu();
    void initializeSettingsScene();
    void initializeGameOverScene();
    
    void update(float deltaTime);
    void setProjectionMatrix(com.badlogic.gdx.math.Matrix4 matrix);
    void render(SpriteBatch batch);
    void renderUI(SpriteBatch batch);
    Scene getScene(String sceneName);
    
    iOutputManager getOutputManager();
}
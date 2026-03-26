package io.github.some_example_name.lwjgl3.scenes;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.iEntityManager;
import io.github.some_example_name.lwjgl3.inputs.iInputManager;
import io.github.some_example_name.lwjgl3.movement.iMovementManager;
import io.github.some_example_name.lwjgl3.outputs.iOutputManager;

public class SceneManager implements iSceneManager {
    private Map<String, Scene> scenes;
    private Scene currentScene;
    private Scene overlayScene;


    
    // Manager references - SceneManager handles all scenes and their managers
    private iEntityManager entityManager;
    private iInputManager inputManager;
    private iOutputManager outputManager;
    

    public SceneManager(iEntityManager entityManager, iInputManager inputManager,
                        iOutputManager outputManager) {
        this.scenes = new HashMap<>();
        this.entityManager = entityManager;
        this.inputManager = inputManager;
        this.outputManager = outputManager;
    }

    public void addScene(Scene scene) {
        Scene existing = scenes.get(scene.getName());
        if (existing != null) {
            existing.onUnload(); // dispose textures/fonts of the old scene first
        }
        scenes.put(scene.getName(), scene);
    }

    public Scene getCurrentScene() {
        return this.currentScene;
    }
    
    public Scene getScene(String sceneName) {
        return scenes.get(sceneName);
    }

    public void setCurrentScene(String sceneName) {
        Scene scene = scenes.get(sceneName);
        if (scene == null) {
            System.err.println("Scene not found: " + sceneName);
            return;
        }
        
        if (this.currentScene != null) {
            this.currentScene.onExit();
        }
        
        this.currentScene = scene;
        
        if (this.currentScene != null) {
            this.currentScene.onEnter();
        }

        // --- PLAY THE MUSIC BASED ON THE SCENE ---
        if (outputManager != null) {
            if (sceneName.equals("MainMenu") || sceneName.equals("SettingsScene")) {
                outputManager.playMusic("MAIN_BGM", true);
            } else if (sceneName.equals("ClassroomScene") || sceneName.equals("Scene1")) {
                outputManager.playMusic("PLAY_BGM", true);
            }
        }
    }
    
    //initialize ClassroomScene - SceneManager is responsible for scene initialization
    public void initializeClassroomScene() {
        ClassroomScene classroomScene = new ClassroomScene(entityManager, inputManager);
        classroomScene.setSceneManager(this); 
        addScene(classroomScene);
        classroomScene.onLoad();
    }
    
    // Initialize MainMenu - SceneManager is responsible for scene initialization
    public void initializeMainMenu() {
        MainMenu mainMenu = new MainMenu(entityManager, inputManager);
        mainMenu.setSceneManager(this); 
        addScene(mainMenu);
        mainMenu.onLoad();
    }
    
    // Initialize SettingsScene - SceneManager is responsible for scene initialization
    public void initializeSettingsScene() {
        SettingsScene settingsScene = new SettingsScene(entityManager, inputManager, this);
        addScene(settingsScene);
        settingsScene.onLoad();
    }
    
    public void initializeGameOverScene() {
        GameOverScene gameOverScene = new GameOverScene(inputManager);
        gameOverScene.setSceneManager(this); 
        addScene(gameOverScene);
        gameOverScene.onLoad();
    }
    

    public void update(float deltaTime) {
        if (overlayScene != null) {
            overlayScene.update(deltaTime);
        } else if (currentScene != null) {
            currentScene.update(deltaTime);
        }
    }

    public void render(SpriteBatch batch) {
        if (currentScene != null) {
            currentScene.render(batch);
        }
        // 2. Draw the overlay ON TOP of the game
        if (overlayScene!= null) {
            // You MUST ensure the batch is projecting correctly for the UI
            overlayScene.render(batch);
        }
    }
    
    public void renderUI(SpriteBatch batch) {
        // Don't draw the game's HUD while an overlay (e.g. SettingsScene) is covering it
        if (overlayScene == null && currentScene != null) {
            currentScene.renderUI(batch);
        }
        if (overlayScene != null) {
            overlayScene.renderUI(batch);
        }
    }
    public iOutputManager getOutputManager() {
        return this.outputManager;
    }
    
    public void openOverlay(String sceneName) {
        overlayScene = scenes.get(sceneName);
        if (overlayScene != null) overlayScene.onEnter();
    }

    public void closeOverlay() {
        if (overlayScene != null) {
            overlayScene.onExit();
            overlayScene = null;
        }
    }

    public boolean isOverlayActive() {
        return overlayScene != null;
    }
}
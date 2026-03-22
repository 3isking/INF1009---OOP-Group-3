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
    private iMovementManager movementManager;
    private iCollisionManager collisionManager;
    private iOutputManager outputManager;
    

    public SceneManager(iEntityManager entityManager, iInputManager inputManager, 
                        iMovementManager movementManager, iCollisionManager collisionManager,
                        iOutputManager outputManager) {
        this.scenes = new HashMap<>();
        this.entityManager = entityManager;
        this.inputManager = inputManager;
        this.movementManager = movementManager;
        this.collisionManager = collisionManager;
        this.outputManager = outputManager;
    }

    public void addScene(Scene scene) {
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
    
    public void setCurrentScene(Scene scene) {
        if (this.currentScene != null) {
            this.currentScene.onExit();
        }
        this.currentScene = scene;
        if (this.currentScene != null) {
            this.currentScene.onEnter();
        }
    }
    
    //initialize ClassroomScene - SceneManager is responsible for scene initialization
    public void initializeClassroomScene() {
        ClassroomScene classroomScene = new ClassroomScene(entityManager, inputManager, movementManager, collisionManager);
        classroomScene.setSceneManager(this); 
        addScene(classroomScene);
        classroomScene.onLoad();
    }
    
    // Initialize MainMenu - SceneManager is responsible for scene initialization
    public void initializeMainMenu() {
        MainMenu mainMenu = new MainMenu(entityManager, inputManager, movementManager, collisionManager);
        mainMenu.setSceneManager(this); 
        addScene(mainMenu);
        mainMenu.onLoad();
    }
    
    // Initialize SettingsScene - SceneManager is responsible for scene initialization
    public void initializeSettingsScene() {
        SettingsScene settingsScene = new SettingsScene(entityManager, inputManager, movementManager, collisionManager);
        settingsScene.setSceneManager(this); 
        addScene(settingsScene);
        settingsScene.onLoad();
    }
    
    public void initializeGameOverScene() {
        GameOverScene gameOverScene = new GameOverScene(entityManager, inputManager, movementManager, collisionManager);
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
    
    public void setProjectionMatrix(com.badlogic.gdx.math.Matrix4 matrix) {
        if (currentScene != null) {
            currentScene.setProjectionMatrix(matrix);
        }
    }

    public void render(SpriteBatch batch) {
        if (currentScene != null) {
            currentScene.render(batch);
        }
    }
    
    public void renderUI(SpriteBatch batch) {
        if (currentScene != null) currentScene.renderUI(batch);
        if (overlayScene != null) overlayScene.renderUI(batch);
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
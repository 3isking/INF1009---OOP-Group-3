package io.github.some_example_name.lwjgl3.scenes;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.some_example_name.lwjgl3.collision.CollisionManager;
import io.github.some_example_name.lwjgl3.entities.EntityManager;
import io.github.some_example_name.lwjgl3.inputs.InputManager;
import io.github.some_example_name.lwjgl3.movement.MovementManager;

public class SceneManager {
    private Map<String, Scene> scenes;
    private Scene currentScene;
    
    // Manager references - SceneManager handles all scenes and their managers
    private EntityManager entityManager;
    private InputManager inputManager;
    private MovementManager movementManager;
    private CollisionManager collisionManager;

    public SceneManager(EntityManager entityManager, InputManager inputManager, 
                        MovementManager movementManager, CollisionManager collisionManager) {
        this.scenes = new HashMap<>();
        this.entityManager = entityManager;
        this.inputManager = inputManager;
        this.movementManager = movementManager;
        this.collisionManager = collisionManager;
    }

    public void addScene(Scene scene) {
        scenes.put(scene.getName(), scene);
    }

    public Scene getCurrentScene() {
        return this.currentScene;
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
    
    // Initialize Scene1 - SceneManager is responsible for scene initialization
    public void initializeScene1() {
        Scene1 scene1 = new Scene1(entityManager, inputManager, movementManager, collisionManager);
        addScene(scene1);
        scene1.onLoad();
    }
    
    // Initialize Scene2 - SceneManager is responsible for scene initialization
    public void initializeScene2() {
        Scene2 scene2 = new Scene2(entityManager, inputManager, movementManager, collisionManager);
        scene2.setSceneManager(this); // Set reference for scene transitions
        addScene(scene2);
        scene2.onLoad();
    }

    public void update(float deltaTime) {
        if (currentScene != null) {
            currentScene.update(deltaTime);
        }
    }
    
    public void setProjectionMatrix(com.badlogic.gdx.math.Matrix4 matrix) {
        if (currentScene != null && currentScene instanceof Scene2) {
            ((Scene2) currentScene).setProjectionMatrix(matrix);
        }
    }

    public void render(SpriteBatch batch) {
        if (currentScene != null) {
            currentScene.render(batch);
    }
}
}
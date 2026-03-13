package io.github.some_example_name.lwjgl3.scenes;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.iEntityManager;
import io.github.some_example_name.lwjgl3.inputs.iInputManager;
import io.github.some_example_name.lwjgl3.movement.iMovementManager;

public class SceneManager implements iSceneManager {
    private Map<String, Scene> scenes;
    private Scene currentScene;
    
    // Manager references - SceneManager handles all scenes and their managers
    private iEntityManager entityManager;
    private iInputManager inputManager;
    private iMovementManager movementManager;
    private iCollisionManager collisionManager;
    

    public SceneManager(iEntityManager entityManager, iInputManager inputManager, 
                        iMovementManager movementManager, iCollisionManager collisionManager) {
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
    
    //initialize ClassroomScene - SceneManager is responsible for scene initialization
    public void initializeClassroomScene() {
        ClassroomScene classroomScene = new ClassroomScene(entityManager, inputManager, movementManager, collisionManager);
        addScene(classroomScene);
        classroomScene.onLoad();
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

    // Initialize Scene3 - SceneManager is responsible for scene initialization
    public void initializeScene3() {
        Scene3 scene3 = new Scene3(entityManager, inputManager, movementManager, collisionManager);
        scene3.setSceneManager(this); // Set reference for scene transitions
        addScene(scene3);
        scene3.onLoad();
    }

    public void update(float deltaTime) {
        if (currentScene != null) {
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
        if (currentScene != null) {
             currentScene.renderUI(batch);
        }
    }
}
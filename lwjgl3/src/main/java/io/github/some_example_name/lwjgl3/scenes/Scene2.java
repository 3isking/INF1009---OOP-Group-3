package io.github.some_example_name.lwjgl3.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.some_example_name.lwjgl3.collision.CollisionManager;
import io.github.some_example_name.lwjgl3.entities.EntityManager;
import io.github.some_example_name.lwjgl3.inputs.InputManager;
import io.github.some_example_name.lwjgl3.movement.MovementManager;

public class Scene2 extends Scene {
    private EntityManager entityManager;
    private InputManager inputManager;
    private MovementManager movementManager;
    private CollisionManager collisionManager;
    private SceneManager sceneManager;
    
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private String titleText = "MAIN MENU";
    private String instructionText = "Click anywhere to start";
    private boolean clickToStart = false;

    public Scene2(EntityManager entityManager, InputManager inputManager, MovementManager movementManager, CollisionManager collisionManager) {
        super("Scene2");
        this.entityManager = entityManager;
        this.inputManager = inputManager;
        this.movementManager = movementManager;
        this.collisionManager = collisionManager;
    }
    
    // Method to set SceneManager reference for scene transitions
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void onLoad() {
        System.out.println("[Scene2 - Main Menu] Loading resources...");
        font = new BitmapFont();
        font.getData().setScale(2.0f);
        shapeRenderer = new ShapeRenderer();
        System.out.println("[Scene2 - Main Menu] Resources loaded successfully!");
    }
    
    // Method to set projection matrix for rendering
    public void setProjectionMatrix(com.badlogic.gdx.math.Matrix4 matrix) {
        if (shapeRenderer != null) {
            shapeRenderer.setProjectionMatrix(matrix);
        }
    }

    @Override
    public void onEnter() {
        System.out.println("[Scene2 - Main Menu] Entering scene...");
        clickToStart = false;
        
        // Main menu doesn't need entities, just UI
        System.out.println("[Scene2 - Main Menu] Main menu ready.");
    }

    @Override
    public void onExit() {
        System.out.println("[Scene2 - Main Menu] Exiting scene...");
        
        // Clear any entities if present
        for (int i = getEntityList().size() - 1; i >= 0; i--) {
            entityManager.removeEntity(getEntityList().get(i).getId());
        }
        getEntityList().clear();
        
        System.out.println("[Scene2 - Main Menu] Scene cleanup complete.");
    }

    @Override
    public void onUnload() {
        System.out.println("[Scene2 - Main Menu] Unloading resources...");
        if (font != null) {
            font.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        System.out.println("[Scene2 - Main Menu] Resources unloaded successfully!");
    }

    @Override
    public void update(float deltaTime) {
        // Check for mouse click or key press to start game
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) || 
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            clickToStart = true;
            System.out.println("[Scene2 - Main Menu] Starting game, transitioning to Scene1...");
            
            // Transition to Scene1
            if (sceneManager != null) {
                sceneManager.setCurrentScene("Scene1");
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // End the current batch to draw shapes
        batch.end();
        
        // Draw menu background
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.1f, 0.1f, 0.15f, 1);
        shapeRenderer.rect(0, 0, 640, 480);
        shapeRenderer.end();
        
        // Restart batch for text rendering
        batch.begin();
        
        // Draw text
        font.setColor(Color.WHITE);

        
        // Instruction
        font.getData().setScale(1.5f);
        font.draw(batch, instructionText, -100, 0);
        font.getData().setScale(2.0f); // Reset scale
    }
}
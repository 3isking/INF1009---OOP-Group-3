package io.github.some_example_name.lwjgl3.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.some_example_name.lwjgl3.collision.CollisionManager;
import io.github.some_example_name.lwjgl3.entities.EntityManager;
import io.github.some_example_name.lwjgl3.inputs.InputManager;
import io.github.some_example_name.lwjgl3.movement.MovementManager;

public class Scene3 extends Scene {
    private EntityManager entityManager;
    private InputManager inputManager;
    private MovementManager movementManager;
    private CollisionManager collisionManager;
    private SceneManager sceneManager;
    
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private Texture backgroundTexture;
    private String titleText = "GAME OVER";
    private String instructionText = "Game Over! Click to restart or press ESC to exit.";
    private boolean clickToStart = false;

    public Scene3(EntityManager entityManager, InputManager inputManager, MovementManager movementManager, CollisionManager collisionManager) {
        super("Scene3");
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
        System.out.println("[Scene3 - Game Over] Loading resources...");
        font = new BitmapFont();
        font.getData().setScale(2.0f);
        shapeRenderer = new ShapeRenderer();
        backgroundTexture = new Texture(Gdx.files.internal("background_scene2.png"));
        System.out.println("[Scene3 - Game Over] Resources loaded successfully!");

    }
    
    // Method to set projection matrix for rendering
    public void setProjectionMatrix(com.badlogic.gdx.math.Matrix4 matrix) {
        if (shapeRenderer != null) {
            shapeRenderer.setProjectionMatrix(matrix);
        }
    }

    @Override
    public void onEnter() {
        System.out.println("[Scene3 - Game Over] Entering scene...");
        clickToStart = false;
        
        // Main menu doesn't need entities, just UI
        System.out.println("[Scene3 - Game Over] Main menu ready.");
    }

    @Override
    public void onExit() {
        System.out.println("[Scene3 - Game Over] Exiting scene...");
        
        // Clear any entities if present
        entityManager.clear();
        getEntityList().clear();
        
        System.out.println("[Scene3 - Game Over] Scene cleanup complete.");
    }

    @Override
    public void onUnload() {
        System.out.println("[Scene3 - Game Over] Unloading resources...");
        if (font != null) {
            font.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        System.out.println("[Scene3 - Game Over] Resources unloaded successfully!");
    }

    @Override
    public void update(float deltaTime) {
        // Check for mouse click or key press to start game
        if (inputManager.inputPressed("action")) {
            clickToStart = true;
            System.out.println("[Scene3 - Game Over] Starting game, transitioning to Scene1...");
            
            // Transition to Scene1
            if (sceneManager != null) {
                sceneManager.setCurrentScene("Scene1");
            }
        }
        // Exit Program
        if (inputManager.inputPressed("exit")){
            Gdx.app.exit();
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        // End the current batch to draw shapes
        batch.draw(backgroundTexture, -350, -300, 1000, 1000); 

        // Draw text     
        font.setColor(Color.WHITE);
        // Instruction
        font.getData().setScale(1.5f);
        GlyphLayout layout = new GlyphLayout(font, instructionText);
        float layoutX = 0 - layout.width/2f;
        font.draw(batch, instructionText, layoutX, 0);

        //title text
        GlyphLayout layout2 = new GlyphLayout(font, titleText);
        float layout2X = 0 - layout2.width/2f;
        font.draw(batch, titleText, layout2X, 100);
        font.getData().setScale(2.0f); // Reset scale
    }
}
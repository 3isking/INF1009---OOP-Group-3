package io.github.some_example_name.lwjgl3.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.iEntityManager;
import io.github.some_example_name.lwjgl3.inputs.iInputManager;
import io.github.some_example_name.lwjgl3.movement.iMovementManager;

public class GameOverScene extends Scene {
    private iEntityManager entityManager;
    private iInputManager inputManager;
    private iMovementManager movementManager;
    private iCollisionManager collisionManager;
    private iSceneManager sceneManager;
    
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private GlyphLayout layout;
    private int finalScore = 0;

    public GameOverScene(iEntityManager entityManager, iInputManager inputManager, iMovementManager movementManager, iCollisionManager collisionManager) {
        super("GameOverScene");
        this.entityManager = entityManager;
        this.inputManager = inputManager;
        this.movementManager = movementManager;
        this.collisionManager = collisionManager;
    }

    public void setSceneManager(iSceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void onLoad() {
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        layout = new GlyphLayout();
    }
    
    public void setFinalScore(int score) {
        this.finalScore = score;
    }

    @Override
    public void onEnter() {
    	if (sceneManager != null && sceneManager.getOutputManager() != null) {
            sceneManager.getOutputManager().playMusic("GAMEOVER_BGM", false);
        }
    }

    @Override
    public void onExit() { }

    @Override
    public void onUnload() {
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (font != null) font.dispose();
    }

    @Override
    public void update(float deltaTime) {
        // Wait for user to restart
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            sceneManager.closeOverlay();
            
            // Re-generate the level from scratch and switch to it
            sceneManager.initializeClassroomScene(); 
            sceneManager.setCurrentScene("ClassroomScene"); 
        }
        
        // Wait for user to quit to Main Menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.closeOverlay();
            sceneManager.setCurrentScene("MainMenu");
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // Handled in renderUI
    }

    @Override
    public void renderUI(SpriteBatch batch) {
        batch.end(); // Briefly stop the SpriteBatch to draw shapes

        // Enable transparency
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f); // 70% transparent black
        
        // Draw a massive rectangle that covers everything regardless of camera position
        Vector3 camPos = inputManager.getCamera().getCamera().position;
        shapeRenderer.rect(camPos.x - 2000, camPos.y - 2000, 4000, 4000); 
        shapeRenderer.end();
        
        Gdx.gl.glDisable(GL20.GL_BLEND); // Turn transparency back off

        batch.begin(); // Resume SpriteBatch to draw text

        // --- GAME OVER TEXT ---
        // Center the text based on where the camera died
        font.getData().setScale(3.0f);
        font.setColor(Color.RED);
        layout.setText(font, "GAME OVER");
        font.draw(batch, "GAME OVER", camPos.x - (layout.width / 2), camPos.y + 100);
        
        
        // --- SCORE TEXT ---
        font.getData().setScale(2.0f);
        font.setColor(Color.YELLOW);
        String scoreText = "Final Score: " + finalScore;
        layout.setText(font, scoreText);
        font.draw(batch, scoreText, camPos.x - (layout.width / 2), camPos.y + 30);
        
        // --- INSTRUCTIONS TEXT ---
        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
        
        String restartText = "Press ENTER to Restart";
        layout.setText(font, restartText);
        font.draw(batch, restartText, camPos.x - (layout.width / 2), camPos.y - 40);
        
        String menuText = "Press ESC to Main Menu";
        layout.setText(font, menuText);
        font.draw(batch, menuText, camPos.x - (layout.width / 2), camPos.y - 80);
        
        // Reset scale 
        font.getData().setScale(1.0f);
    }
    
    public void setProjectionMatrix(com.badlogic.gdx.math.Matrix4 matrix) { }
}
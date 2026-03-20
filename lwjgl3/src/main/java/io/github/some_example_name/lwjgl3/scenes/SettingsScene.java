package io.github.some_example_name.lwjgl3.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.iEntityManager;
import io.github.some_example_name.lwjgl3.inputs.iInputManager;
import io.github.some_example_name.lwjgl3.movement.iMovementManager;

public class SettingsScene extends Scene {
    private iEntityManager entityManager;
    private iInputManager inputManager;
    private iMovementManager movementManager;
    private iCollisionManager collisionManager;
    private iSceneManager sceneManager;

    private BitmapFont font;
    private Texture backgroundTexture;
    private Texture backBtnTexture; 

    // --- Settings State ---
    private int musicVolume = 100;
    private int sfxVolume = 100;
    private int upKey = Input.Keys.W;   // Default Up key
    private int downKey = Input.Keys.S; // Default Down key

    private boolean isRebindingUp = false;
    private boolean isRebindingDown = false;

    // --- UI Click Bounds (X, Y, Width, Height) ---
    private Rectangle musicMinusRect = new Rectangle(-100, 120, 40, 40);
    private Rectangle musicPlusRect = new Rectangle(60, 120, 40, 40);
    
    private Rectangle sfxMinusRect = new Rectangle(-100, 60, 40, 40);
    private Rectangle sfxPlusRect = new Rectangle(60, 60, 40, 40);
    
    private Rectangle upRebindRect = new Rectangle(-50, -20, 100, 40);
    private Rectangle downRebindRect = new Rectangle(-50, -80, 100, 40);
    
    private Rectangle backBtnRect = new Rectangle(-100, -180, 200, 50);

    public SettingsScene(iEntityManager entityManager, iInputManager inputManager, iMovementManager movementManager, iCollisionManager collisionManager) {
        super("SettingsScene");
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
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        // Reuse main menu background or use a new one
        backgroundTexture = new Texture(Gdx.files.internal("mainmenu_bg.jpg")); 
        backBtnTexture = new Texture(Gdx.files.internal("back_button.png")); // Add this asset!
    }

    @Override
    public void onEnter() {
        isRebindingUp = false;
        isRebindingDown = false;
    }

    @Override
    public void onExit() {
    }

    @Override
    public void onUnload() {
        if (font != null) font.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (backBtnTexture != null) backBtnTexture.dispose();
    }

    @Override
    public void update(float deltaTime) {
        // fetch the latest keys every frame
        upKey = inputManager.getMappedKey("up");
        downKey = inputManager.getMappedKey("down");

        // If we are currently waiting for a key press, block UI clicking.
        if (inputManager.getWaitingForBind() != null) {
            return; 
        }

        if (inputManager.inputPressed("action")) {
            Vector2 screenMousePos = inputManager.getMousePosition();
            com.badlogic.gdx.math.Vector3 worldMousePos = new com.badlogic.gdx.math.Vector3(screenMousePos.x, screenMousePos.y, 0);
            inputManager.getCamera().getCamera().unproject(worldMousePos);

            float mx = worldMousePos.x;
            float my = worldMousePos.y;

         // Volume Controls
            if (musicMinusRect.contains(mx, my)) {
                musicVolume = Math.max(0, musicVolume - 10);
                sceneManager.getOutputManager().setMusicVolume(musicVolume / 100f);
            }
            if (musicPlusRect.contains(mx, my)) {
                musicVolume = Math.min(100, musicVolume + 10);
                sceneManager.getOutputManager().setMusicVolume(musicVolume / 100f);
            }
            if (sfxMinusRect.contains(mx, my)) {
                sfxVolume = Math.max(0, sfxVolume - 10);
                sceneManager.getOutputManager().setSfxVolume(sfxVolume / 100f);
            }
            if (sfxPlusRect.contains(mx, my)) {
                sfxVolume = Math.min(100, sfxVolume + 10);
                sceneManager.getOutputManager().setSfxVolume(sfxVolume / 100f);
            }
            // Rebind Buttons - Tells InputManager to start listening for the next key press!
            if (upRebindRect.contains(mx, my)) inputManager.setKeyBind("up");
            if (downRebindRect.contains(mx, my)) inputManager.setKeyBind("down");

            // Back Button
            if (backBtnRect.contains(mx, my)) {
                if (sceneManager != null) {
                    sceneManager.setCurrentScene("MainMenu");
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(backgroundTexture, -400, -300, 800, 600);
        font.draw(batch, "SETTINGS", -60, 220);

        // Music
        font.draw(batch, "Music Vol:", -200, 150);
        font.draw(batch, "<", musicMinusRect.x + 10, musicMinusRect.y + 30);
        font.draw(batch, String.valueOf(musicVolume), -15, 150);
        font.draw(batch, ">", musicPlusRect.x + 10, musicPlusRect.y + 30);

        // SFX
        font.draw(batch, "SFX Vol:", -200, 90);
        font.draw(batch, "<", sfxMinusRect.x + 10, sfxMinusRect.y + 30);
        font.draw(batch, String.valueOf(sfxVolume), -15, 90);
        font.draw(batch, ">", sfxPlusRect.x + 10, sfxPlusRect.y + 30);

        // --- Keybinds Rendering ---
        font.draw(batch, "Up Key:", -200, 10);
        // If waitingForBind equals "up", show "Press any key...", otherwise show the key name
        String upText = "up".equals(inputManager.getWaitingForBind()) ? "Press any key..." : Input.Keys.toString(upKey);
        font.draw(batch, "[" + upText + "]", upRebindRect.x, upRebindRect.y + 30);

        font.draw(batch, "Down Key:", -200, -50);
        // Same logic for "down"
        String downText = "down".equals(inputManager.getWaitingForBind()) ? "Press any key..." : Input.Keys.toString(downKey);
        font.draw(batch, "[" + downText + "]", downRebindRect.x, downRebindRect.y + 30);

        // Back Button
        batch.draw(backBtnTexture, backBtnRect.x, backBtnRect.y, backBtnRect.width, backBtnRect.height);
    }

    @Override
    public void renderUI(SpriteBatch batch) {
    }

    public void setProjectionMatrix(com.badlogic.gdx.math.Matrix4 matrix) {
    }
}
package io.github.some_example_name.lwjgl3.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.iEntityManager;
import io.github.some_example_name.lwjgl3.inputs.iInputManager;
import io.github.some_example_name.lwjgl3.movement.iMovementManager;

public class MainMenu extends Scene {
    private iEntityManager entityManager;
    private iInputManager inputManager;
    private iMovementManager movementManager;
    private iCollisionManager collisionManager;
    private iSceneManager sceneManager;
    
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private Texture backgroundTexture;
    
    // --- Button Textures & Dimensions ---
    private Texture startBtnTexture;
    private Texture settingsBtnTexture;
    private Texture quitBtnTexture;
    private Texture logoTexture;
    
    private float btnWidth = 200;
    private float btnHeight = 50;
    private float btnX = -100;      
    private float startBtnY = -50;  
    private float settingsBtnY = -120; 
    private float quitBtnY = -190;  
    
    private boolean clickToStart = false;

    public MainMenu(iEntityManager entityManager, iInputManager inputManager, iMovementManager movementManager, iCollisionManager collisionManager) {
        super("MainMenu");
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
        System.out.println("[MainMenu] Loading resources...");
        font = new BitmapFont();
        font.getData().setScale(2.0f);
        shapeRenderer = new ShapeRenderer();
        backgroundTexture = new Texture(Gdx.files.internal("mainmenu_bg.jpg"));
        
        logoTexture = new Texture(Gdx.files.internal("LTF_logo.png"));
        startBtnTexture = new Texture(Gdx.files.internal("start_button.png"));
        settingsBtnTexture = new Texture(Gdx.files.internal("settings_button.png")); 
        quitBtnTexture = new Texture(Gdx.files.internal("quit_button.png"));
        
        System.out.println("[MainMenu] Resources loaded successfully!");
    }
    
    public void setProjectionMatrix(com.badlogic.gdx.math.Matrix4 matrix) {
        if (shapeRenderer != null) {
            shapeRenderer.setProjectionMatrix(matrix);
        }
    }

    @Override
    public void onEnter() {
        System.out.println("[MainMenu] Entering scene...");
        clickToStart = false;
        System.out.println("[MainMenu] Ready.");
    }

    @Override
    public void onExit() {
        System.out.println("[MainMenu] Exiting scene...");
        for (int i = getEntityList().size() - 1; i >= 0; i--) {
            entityManager.removeEntity(getEntityList().get(i).getId());
        }
        clearEntityList();
        System.out.println("[MainMenu] Scene cleanup complete.");
    }

    @Override
    public void onUnload() {
        System.out.println("[MainMenu] Unloading resources...");
        if (font != null) font.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        
        if (startBtnTexture != null) startBtnTexture.dispose();
        if (settingsBtnTexture != null) settingsBtnTexture.dispose();
        if (quitBtnTexture != null) quitBtnTexture.dispose();
        
        System.out.println("[MainMenu] Resources unloaded successfully!");
    }

    @Override
    public void update(float deltaTime) {
        if (inputManager.inputPressed("action")) {
            Vector2 screenMousePos = inputManager.getMousePosition();
            
            com.badlogic.gdx.math.Vector3 worldMousePos = new com.badlogic.gdx.math.Vector3(screenMousePos.x, screenMousePos.y, 0);
            inputManager.getCamera().getCamera().unproject(worldMousePos);
            
            // 1. Start Button
            if (worldMousePos.x >= btnX && worldMousePos.x <= btnX + btnWidth &&
                worldMousePos.y >= startBtnY && worldMousePos.y <= startBtnY + btnHeight) {
                
                System.out.println("[MainMenu] Start Button Clicked!");
                if (sceneManager != null) {
                    sceneManager.setCurrentScene("ClassroomScene"); 
                }
            }
            
            // 2. Settings Button
            else if (worldMousePos.x >= btnX && worldMousePos.x <= btnX + btnWidth &&
                     worldMousePos.y >= settingsBtnY && worldMousePos.y <= settingsBtnY + btnHeight) {
                
                System.out.println("[MainMenu] Settings Button Clicked!");
                 if (sceneManager != null) {
                     sceneManager.setCurrentScene("SettingsScene"); 
                 }
            }
            
            // 3. Quit Button
            else if (worldMousePos.x >= btnX && worldMousePos.x <= btnX + btnWidth &&
                     worldMousePos.y >= quitBtnY && worldMousePos.y <= quitBtnY + btnHeight) {
                
                System.out.println("[MainMenu] Quit Button Clicked!");
                Gdx.app.exit();
            }
        }
        
//        if (inputManager.inputPressed("exit")){
//            Gdx.app.exit();
//        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(backgroundTexture, -400, -300, 800, 600); 
        batch.draw(logoTexture, -150, 10, 300, 200);
        batch.draw(startBtnTexture, btnX, startBtnY, btnWidth, btnHeight);
        batch.draw(settingsBtnTexture, btnX, settingsBtnY, btnWidth, btnHeight);
        batch.draw(quitBtnTexture, btnX, quitBtnY, btnWidth, btnHeight);
    }
    
    @Override
    public void renderUI(SpriteBatch batch) {
    }
}
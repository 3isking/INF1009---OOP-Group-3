package io.github.some_example_name.lwjgl3.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.collision.CollisionManager;
import io.github.some_example_name.lwjgl3.entities.AiEntity;
import io.github.some_example_name.lwjgl3.entities.EntityManager;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.Sprite;
import io.github.some_example_name.lwjgl3.entities.Obstacle;
import io.github.some_example_name.lwjgl3.inputs.InputManager;
import io.github.some_example_name.lwjgl3.movement.MovementManager;


public class Scene1 extends Scene {

    private EntityManager entityManager;
    private InputManager inputManager;
    private MovementManager movementManager;
    private CollisionManager collisionManager;
    private Texture backgroundTexture;
    private Music backgroundMusic;

    private static final int LEVEL_WIDTH  = 2000;
    private static final int LEVEL_HEIGHT = 1200;
    private static final int TILE_SIZE = 200;

    public Scene1(EntityManager entityManager, InputManager inputManager, MovementManager movementManager, CollisionManager collisionManager) {
        super("Scene1");
        this.entityManager = entityManager;
        this.inputManager = inputManager;
        this.movementManager = movementManager;
        this.collisionManager = collisionManager;
    }

    @Override
    public void onLoad() {
        System.out.println("[Scene1 - Level] Loading resources...");
        System.out.println("[Scene1 - Level] Resources loaded successfully!");
        backgroundTexture = new Texture(Gdx.files.internal("background_scene1.png"));
    }

    @Override
    public void onEnter() {
        System.out.println("[Scene1 - Level] Entering scene...");

        // Level Size
        int levelWidth = LEVEL_WIDTH;
        int levelHeight = LEVEL_HEIGHT;

        // player Spawn
        PlayableEntity player = new PlayableEntity(movementManager, collisionManager);
        player.setId("player_1");
        player.setSprite(new Sprite(new Texture(Gdx.files.internal("Player.png")), 50, 50));
        player.setPosition(new Vector2(200, 200));

        addEntity(player);
        entityManager.addEntity(player);

        // AI Entities
        AiEntity ai1 = new AiEntity(movementManager, collisionManager);
        ai1.setId("ai_1");
        ai1.setSprite(new Sprite(new Texture(Gdx.files.internal("owl.png")), 50, 50));
        ai1.setPosition(new Vector2(1600, 900));

        AiEntity ai2 = new AiEntity(movementManager, collisionManager);
        ai2.setId("ai_2");
        ai2.setSprite(new Sprite(new Texture(Gdx.files.internal("owl.png")), 50, 50));
        ai2.setPosition(new Vector2(1800, 250));

        AiEntity ai3 = new AiEntity(movementManager, collisionManager);
        ai3.setId("ai_3");
        ai3.setSprite(new Sprite(new Texture(Gdx.files.internal("owl.png")), 50, 50));
        ai3.setPosition(new Vector2(500, 950));

        AiEntity ai4 = new AiEntity(movementManager, collisionManager);
        ai4.setId("ai_4");
        ai4.setSprite(new Sprite(new Texture(Gdx.files.internal("owl.png")), 50, 50));
        ai4.setPosition(new Vector2(1400, 500));

        addEntity(ai1);
        entityManager.addEntity(ai1);
        addEntity(ai2);
        entityManager.addEntity(ai2);
        addEntity(ai3);
        entityManager.addEntity(ai3);
        addEntity(ai4);
        entityManager.addEntity(ai4);

        // Border Obstacles
        Obstacle topObstacle = new Obstacle(0, levelHeight - 20, levelWidth, 20, collisionManager);
        topObstacle.setId("obstacle_top");

        Obstacle bottomObstacle = new Obstacle(0, 0, levelWidth, 20, collisionManager);
        bottomObstacle.setId("obstacle_bottom");

        Obstacle leftObstacle = new Obstacle(0, 0, 20, levelHeight, collisionManager);
        leftObstacle.setId("obstacle_left");

        Obstacle rightObstacle = new Obstacle(levelWidth - 20, 0, 20, levelHeight, collisionManager);
        rightObstacle.setId("obstacle_right");

        addEntity(topObstacle);
        entityManager.addEntity(topObstacle);
        addEntity(bottomObstacle);
        entityManager.addEntity(bottomObstacle);
        addEntity(leftObstacle);
        entityManager.addEntity(leftObstacle);
        addEntity(rightObstacle);
        entityManager.addEntity(rightObstacle);

        // Obstacles
        Obstacle w1 = new Obstacle(400, 600, 1200, 30, collisionManager);
        w1.setId("obstacle_1");

        Obstacle w2 = new Obstacle(900, 250, 30, 250, collisionManager);
        w2.setId("obstacle_2");

        Obstacle w3 = new Obstacle(250, 850, 400, 30, collisionManager);
        w3.setId("obstacle_3");

        Obstacle w4 = new Obstacle(250, 850, 30, 250, collisionManager);
        w4.setId("obstacle_4");

        Obstacle w5 = new Obstacle(900, 250, 250, 30, collisionManager);
        w5.setId("obstacle_5");

        addEntity(w1);
        entityManager.addEntity(w1);
        addEntity(w2);
        entityManager.addEntity(w2);
        addEntity(w3);
        entityManager.addEntity(w3);
        addEntity(w4);
        entityManager.addEntity(w4);
        addEntity(w5);
        entityManager.addEntity(w5);
        
        // Background Music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        backgroundMusic.setLooping(true); // Makes the music loop 
        backgroundMusic.setVolume(0.05f);  // Adjust volume
        backgroundMusic.play();           // Start the music

        //Set camera type to follow player
        inputManager.usePlayerCamera();

        System.out.println("[Scene1 - Level] Scene setup complete. Entities created: " + getEntityList().size());
    }

    @Override
    public void onExit() {
        System.out.println("[Scene1 - Level] Exiting scene...");
        entityManager.clear();
        
        // Clear background music
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }

        // Clear the scene entity list
        getEntityList().clear();
        inputManager.useDefaultCamera();

        System.out.println("[Scene1 - Level] Scene cleanup complete.");
    }

    @Override
    public void onUnload() {
        System.out.println("[Scene1 - Level] Unloading resources...");

        System.out.println("[Scene1 - Level] Resources unloaded successfully!");
    }

    @Override
    public void update(float deltaTime) {
        // Camera Control for Scene1
        if (inputManager.inputPressed("freeCam")) {
            inputManager.useFreeCamera();
        }
        if (inputManager.inputPressed("playerCam")) {
            inputManager.usePlayerCamera();
        }
        if (inputManager.inputPressed("defaultCam")) {
            inputManager.useDefaultCamera();
        }

        // Movement Rebind Test
        String rebindAction = "right";
        if (inputManager.inputPressed("rebind")) {
            inputManager.setKeyBind(rebindAction);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        for (int x = 0; x < LEVEL_WIDTH; x += TILE_SIZE) {
            for (int y = 0; y < LEVEL_HEIGHT; y += TILE_SIZE) {
                batch.draw(backgroundTexture, x, y, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}

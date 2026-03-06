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

public class ClassroomScene extends Scene {

    private EntityManager entityManager;
    private InputManager inputManager;
    private MovementManager movementManager;
    private CollisionManager collisionManager;

    private Texture backgroundTexture;
    private float bg1X;
    private float bg2X;

    private static final float SCROLL_SPEED = 3f;
    private static final float BG_WIDTH = 1280f; // match your image width
    private static final float BG_HEIGHT = 720f;

    public ClassroomScene(EntityManager entityManager, InputManager inputManager,
                          MovementManager movementManager, CollisionManager collisionManager) {
        super("ClassroomScene");
        this.entityManager = entityManager;
        this.inputManager = inputManager;
        this.movementManager = movementManager;
        this.collisionManager = collisionManager;
    }

    @Override
    public void onLoad() {
        backgroundTexture = new Texture(Gdx.files.internal("classroom.png"));
        // Start bg1 at position 0, bg2 directly behind it
        bg1X = 0;
        bg2X = BG_WIDTH;
    }

    @Override
    public void onEnter() {
        bg1X = 0;
        bg2X = BG_WIDTH;

        // Spawn player fixed on left side
        PlayableEntity player = new PlayableEntity(100, 200, movementManager, collisionManager);
        player.setId("player_1");
        addEntity(player);
        entityManager.addEntity(player);
    }

    @Override
    public void update(float deltaTime) {
        // Scroll both backgrounds left
        bg1X -= SCROLL_SPEED;
        bg2X -= SCROLL_SPEED;

        // Leapfrog: if bg1 goes fully off screen, jump it behind bg2
        if (bg1X + BG_WIDTH <= 0) {
            bg1X = bg2X + BG_WIDTH;
        }

        // Leapfrog: if bg2 goes fully off screen, jump it behind bg1
        if (bg2X + BG_WIDTH <= 0) {
            bg2X = bg1X + BG_WIDTH;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // Draw both background copies
        batch.draw(backgroundTexture, bg1X, 0, BG_WIDTH, BG_HEIGHT);
        batch.draw(backgroundTexture, bg2X, 0, BG_WIDTH, BG_HEIGHT);

        // Entities render on top (handled by EntityManager in GameMaster)
    }

    @Override
    public void onExit() {
        entityManager.clear();
        clearEntityList();
    }

    @Override
    public void onUnload() {
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}
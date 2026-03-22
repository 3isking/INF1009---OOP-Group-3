package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.some_example_name.lwjgl3.collision.CollisionManager;
import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.EntityManager;
import io.github.some_example_name.lwjgl3.entities.iCollidable;
import io.github.some_example_name.lwjgl3.inputs.Camera;
import io.github.some_example_name.lwjgl3.inputs.DefaultCamera;
import io.github.some_example_name.lwjgl3.inputs.InputManager;
import io.github.some_example_name.lwjgl3.movement.MovementManager;
import io.github.some_example_name.lwjgl3.outputs.OutputManager;
import io.github.some_example_name.lwjgl3.scenes.SceneManager;

public class GameMaster extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private boolean debugMode;
    private SceneManager sceneManager;
    private EntityManager entityManager;
    private MovementManager movementManager;
    private CollisionManager collisionManager;
    private InputManager inputManager;
    private OutputManager outputManager;
    private Camera camera;
    private Viewport viewport;

    // Fixed virtual resolution — everything is designed around this
    private static final float VIRTUAL_W = 640f;
    private static final float VIRTUAL_H = 480f;

    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        // Camera locked to virtual resolution — never changes size
        OrthographicCamera orthoCam = new OrthographicCamera(VIRTUAL_W, VIRTUAL_H);

        // ExtendViewport locks the height to VIRTUAL_H and extends the visible
        // width to fill whatever screen width is available — no black bars.
        // Perfect for a horizontal scroller.
        viewport = new ExtendViewport(VIRTUAL_W, VIRTUAL_H, orthoCam);
        viewport.apply(true); // center camera immediately

        camera = new DefaultCamera(orthoCam);

        inputManager = new InputManager(camera);
        Gdx.input.setInputProcessor(inputManager);
        outputManager = new OutputManager();
        movementManager = new MovementManager(inputManager);
        collisionManager = new CollisionManager();
        entityManager = new EntityManager(movementManager, collisionManager);
        sceneManager = new SceneManager(entityManager, inputManager, movementManager, collisionManager, outputManager);
        
        collisionManager.setCollisionManager(entityManager, sceneManager, outputManager);

        outputManager.loadMusic("MAIN_BGM", "main_bgmusic.mp3"); 
        outputManager.loadMusic("PLAY_BGM", "play_bgmusic.mp3");
        outputManager.loadMusic("GAMEOVER_BGM", "gameover.mp3");
        outputManager.loadAudio("CORRECT_EVENT", "correct_bgsound.mp3");
        
        sceneManager.initializeMainMenu();
        sceneManager.initializeClassroomScene();
        sceneManager.initializeSettingsScene();
        sceneManager.initializeGameOverScene();
        sceneManager.initializeScene1();
        sceneManager.initializeScene3();

        sceneManager.setCurrentScene("MainMenu");
//        sceneManager.setCurrentScene("ClassroomScene");

        outputManager.loadAudio("COLLISION_EVENT", "collide.wav");
        outputManager.loadAudio("HIT_EVENT", "hit.mp3");
        
        debugMode = false;
    }

    @Override
    public void resize(int width, int height) {
        // This is the critical method — called when going fullscreen or resizing window.
        // Tell the viewport the new screen size so it recalculates the letterbox and
        // keeps the camera projection stable. Without this, the camera fights the
        // new resolution every frame causing the lag/catchup jitter.
        viewport.update(width, height, true);
    }

    public void render() {
        float dt = Math.min(Gdx.graphics.getDeltaTime(), 0.16f);
        ScreenUtils.clear(0, 0, 0.2f, 1);
        
     // --- FREEZE BACKGROUND GAMEPLAY IF OVERLAY IS ACTIVE ---
        if (!sceneManager.isOverlayActive()) {
            movementManager.moveEntities(entityManager.getAllEntities());
            inputManager.updateCamera(entityManager.getEntity("player_1"));
            collisionManager.checkCollisions(entityManager.getAllEntities());
        }

//        Matrix4 camMatrix = inputManager.getCamera().getCombinedMatrix();
//        batch.setProjectionMatrix(camMatrix);
//        shape.setProjectionMatrix(camMatrix);

        // 1. MOVE FIRST
        movementManager.moveEntities(entityManager.getAllEntities());

        // 2. CAMERA IMMEDIATELY AFTER MOVEMENT (before collision)
        inputManager.updateCamera(entityManager.getEntity("player_1"));

        // 3. COLLISION
        collisionManager.checkCollisions(entityManager.getAllEntities());

        // 4. UPDATE MATRICES with FINAL positions
        Matrix4 camMatrix = inputManager.getCamera().getCombinedMatrix();
        batch.setProjectionMatrix(camMatrix);
        shape.setProjectionMatrix(camMatrix);

        // 5. Scene logic
        sceneManager.update(dt);

        // 6. RENDER
        batch.begin();
        sceneManager.render(batch);
        entityManager.render(batch);
        sceneManager.renderUI(batch);
        batch.end();

        debugMode();
    }

    public void dispose() {
        batch.dispose();
        shape.dispose();
        sceneManager.getCurrentScene().onUnload();
    }

    public void debugMode() {
        if (debugMode) {
            for (Entity entity : entityManager.getAllEntities()) {
                shape.begin(ShapeRenderer.ShapeType.Line);
                if (entity instanceof iCollidable) {
                    iCollidable collisionEntity = (iCollidable) entity;
                    shape.setColor(Color.RED);
                    shape.rect(
                        collisionEntity.getCollisionBounds().x,
                        collisionEntity.getCollisionBounds().y,
                        collisionEntity.getCollisionBounds().width,
                        collisionEntity.getCollisionBounds().height
                    );
                }
                shape.end();
            }
        }
    }
}
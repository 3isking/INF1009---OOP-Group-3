package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.some_example_name.lwjgl3.collision.CollisionManager;
import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.EntityManager;
import io.github.some_example_name.lwjgl3.entities.iCollidable;
import io.github.some_example_name.lwjgl3.inputs.Camera;
import io.github.some_example_name.lwjgl3.inputs.InputManager;
import io.github.some_example_name.lwjgl3.inputs.PlayerCamera;
import io.github.some_example_name.lwjgl3.movement.MovementManager;
import io.github.some_example_name.lwjgl3.outputs.OutputManager;
import io.github.some_example_name.lwjgl3.scenes.SceneManager;

public class GameMaster extends ApplicationAdapter{
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

    public void create(){

        // Setup Batch & Shape Renderer
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        
        //Setup camera, default as player camera
        OrthographicCamera orthoCam = new OrthographicCamera(640, 480);
        camera = new PlayerCamera(orthoCam);
        
        // Setup Managers
        inputManager = new InputManager(camera);
        Gdx.input.setInputProcessor(inputManager);
        outputManager = new OutputManager();
        movementManager = new MovementManager(inputManager);
        collisionManager = new CollisionManager();
        entityManager = new EntityManager(movementManager, collisionManager);
        sceneManager = new SceneManager(entityManager, inputManager, movementManager, collisionManager);

        // Initialize CollisionManager with SceneManager reference for scene transitions
        collisionManager.setCollisionManager(sceneManager, outputManager);
        
        // Initialize both scenes
        sceneManager.initializeScene1();
        sceneManager.initializeScene2();
        sceneManager.initializeScene3();
        
        // Start with Scene2 (Main Menu)
        sceneManager.setCurrentScene("Scene2");
        
        // Setup audio
        outputManager.loadAudio("COLLISION_EVENT", "collide.wav"); // Obstacle
        outputManager.loadAudio("HIT_EVENT", "hit.mp3"); // AI Entity

        // Setup Debug
        debugMode = true;
    }

    public void render(){
        float dt = Math.min(Gdx.graphics.getDeltaTime(), 0.16f);
        ScreenUtils.clear(0, 0, 0.2f, 1);
        
        // 1. MOVE FIRST
        movementManager.moveEntities(entityManager.getAllEntities());
        
        // 2. CAMERA IMMEDIATELY AFTER MOVEMENT (before collision)
        inputManager.updateCamera(entityManager.getEntity("player_1"));
        
        // 3. COLLISION (won't fight camera)
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
        batch.end();

        debugMode();
        
        // Input handling stays after render
        // Testing camera change
        if (inputManager.inputPressed("freeCam")) {
            inputManager.useFreeCamera();
        }
        if (inputManager.inputPressed("playerCam")) {
            inputManager.usePlayerCamera();
        }
        if (inputManager.inputPressed("defaultCam")) {
            inputManager.useDefaultCamera();
        }
        String rebindAction = "right";
        if (inputManager.inputPressed("rebind")) {
            inputManager.setKeyBind(rebindAction);
        }
        
    }


    public void dispose(){
        batch.dispose();
        shape.dispose();
        
        sceneManager.getCurrentScene().onUnload();
    }

    public void debugMode(){
        if (debugMode){
            for (Entity entity : entityManager.getAllEntities()) {
                shape.begin(ShapeRenderer.ShapeType.Line);
        
                if (entity instanceof iCollidable){
                    iCollidable collisionEntity = (iCollidable) entity;

                    shape.setColor(Color.RED);
                    shape.rect(collisionEntity.getCollisionBounds().x, collisionEntity.getCollisionBounds().y, collisionEntity.getCollisionBounds().width, collisionEntity.getCollisionBounds().height);
                }
                shape.end();
            }
        }
    }
}
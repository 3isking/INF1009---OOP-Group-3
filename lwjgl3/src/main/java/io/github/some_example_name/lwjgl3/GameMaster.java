package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.Player;
import io.github.some_example_name.lwjgl3.entities.iCollidable;
import io.github.some_example_name.lwjgl3.managers.CollisionManager;
import io.github.some_example_name.lwjgl3.managers.EntityManager;
import io.github.some_example_name.lwjgl3.managers.InputManager;
import io.github.some_example_name.lwjgl3.managers.MovementManager;
import io.github.some_example_name.lwjgl3.managers.OutputManager;
import io.github.some_example_name.lwjgl3.managers.SceneManager;
import io.github.some_example_name.lwjgl3.scenes.Scene1;

public class GameMaster extends ApplicationAdapter{
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private boolean debugMode;
    private SceneManager sceneManager;
    private EntityManager entityManager;
    private MovementManager movementManager;
    private CollisionManager collisionManager;
    private InputManager inputManager;
    private OutputManager outputManager;

    public void create(){

        // Setup Camera
		camera = new OrthographicCamera(640, 480);
		camera.position.set(320, 240, 0);
		camera.update();
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        
        // Setup Managers
        entityManager = new EntityManager();
        movementManager = new MovementManager();
        inputManager = new InputManager();
        Gdx.input.setInputProcessor(inputManager);
        collisionManager = new CollisionManager();
        sceneManager = new SceneManager();
        outputManager = new OutputManager();

        // Setup Player
        // Player player = new Player(inputManager);
        // entityManager.addEntity(player);

        // // Setup Wall
        // Wall wall1 = new Wall();
        // entityManager.addEntity(wall1);
        Scene1 scene1 = new Scene1(entityManager, inputManager);
        sceneManager.addScene(scene1);
        scene1.onLoad();
        sceneManager.setCurrentScene(scene1);

        // Setup Debug
        debugMode = true;
        
        // Setup audio
        outputManager.loadAudio("COLLISION_EVENT", "collide.wav");
    }

    public void render(){
        float dt = Gdx.graphics.getDeltaTime();
        cameraControl();
        camera.update();
		batch.setProjectionMatrix(camera.combined);
		shape.setProjectionMatrix(camera.combined);
        
		ScreenUtils.clear(0, 0, 0.2f, 1);

        sceneManager.update(dt);

        movementManager.moveEntities(entityManager.getAllEntities());
        collisionManager.checkCollisions(entityManager.getAllEntities());
        
        batch.begin();
        sceneManager.render(batch);
        entityManager.render(batch);
        batch.end();
        
        debugMode();
        //Rebind to be determined from a menu for keyjustpressed and rebind action
        String rebindAction = "right";
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSLASH)) {
            inputManager.setKeyBind(rebindAction);
        }
        
        
        // Play collision.wav if hit
        for (Entity e : entityManager.getAllEntities()) {
            if (e instanceof Player) {
                Player p = (Player) e;
                
                if (p.wasHit()) {
                    outputManager.playSound("COLLISION_EVENT");
                    outputManager.triggerVibration(100);
                    System.out.println("Abstract Engine: Collision Output Triggered!");
                    
                    // Reset the flag so it plays only once per hit
                    p.resetHitFlag();
                }
            }
        }

    }

    public void dispose(){
        batch.dispose();
        shape.dispose();
        
        // if (sceneManager.getCurrentScene() != null) {
            sceneManager.getCurrentScene().onUnload();
        // }
    }

    public void cameraControl(){
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0, 3, 0);
		}
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

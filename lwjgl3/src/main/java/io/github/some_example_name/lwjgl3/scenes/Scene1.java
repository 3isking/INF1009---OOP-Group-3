package io.github.some_example_name.lwjgl3.scenes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.collision.CollisionManager;
import io.github.some_example_name.lwjgl3.entities.AiEntity;
import io.github.some_example_name.lwjgl3.entities.EntityManager;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.Sprite;
import io.github.some_example_name.lwjgl3.entities.Wall;
import io.github.some_example_name.lwjgl3.inputs.InputManager;
import io.github.some_example_name.lwjgl3.movement.MovementManager;

public class Scene1 extends Scene{
    private EntityManager entityManager;
    private InputManager inputManager;
    private MovementManager movementManager;
    private CollisionManager collisionManager;
    private Texture backgroundTexture;

    public Scene1(EntityManager entityManager, InputManager inputManager, MovementManager movementManager, CollisionManager collisionManager) {
        super("Scene1");
        this.entityManager = entityManager;
        this.inputManager = inputManager;
        this.movementManager = movementManager;
        this.collisionManager = collisionManager;
    }

    @Override
    public void onLoad() {
        System.out.println("[Scene1] Loading resources...");
        System.out.println("[Scene1] Resources loaded successfully!");
        backgroundTexture = new Texture(Gdx.files.internal("background_scene1.png"));
    }

    @Override
    public void onEnter() {
        System.out.println("[Scene1] Entering scene...");
        
        // add player entity in the center
        PlayableEntity player = new PlayableEntity(movementManager, collisionManager);
        player.setSprite(new Sprite(new Texture(Gdx.files.internal("Player.png")), 50, 50));

        // add AI entity
        AiEntity ai = new AiEntity(movementManager, collisionManager);
        ai.setSprite(new Sprite(new Texture(Gdx.files.internal("owl.png")), 50, 50));


        ai.setPosition(new Vector2(320,340));
        player.setPosition(new Vector2(320, 240));
        
        // Add to scene entity list 
        addEntity(player);
        addEntity(ai);
        
        // Add to global entity manager (for rendering and updates)
        entityManager.addEntity(player);
        entityManager.addEntity(ai);
        
        // Top wall
        Wall topWall = new Wall(0, 460, 640, 20, collisionManager);
        topWall.setId("wall_top");
        addEntity(topWall);
        entityManager.addEntity(topWall);
        
        // Bottom wall
        Wall bottomWall = new Wall(0, 0, 640, 20, collisionManager);
        bottomWall.setId("wall_bottom");
        addEntity(bottomWall);
        entityManager.addEntity(bottomWall);
        
        // Left wall
        Wall leftWall = new Wall(0, 0, 20, 480, collisionManager);
        leftWall.setId("wall_left");
        addEntity(leftWall);
        entityManager.addEntity(leftWall);
        
        // Right wall
        Wall rightWall = new Wall(620, 0, 20, 480, collisionManager);
        rightWall.setId("wall_right");
        addEntity(rightWall);
        entityManager.addEntity(rightWall);
        
        System.out.println("[Scene1] Scene setup complete. Entities created: " + getEntityList().size());
    }

    @Override
    public void onExit() {
        System.out.println("[Scene1] Exiting scene...");

        entityManager.clear();
        // movementManager.emptyMovableEntities();
        // collisionManager.emptyCollidableEntities();
        
        // Clear the scene entity list
        getEntityList().clear();
        
        System.out.println("[Scene1] Scene cleanup complete.");
    }

    @Override
    public void onUnload() {
        System.out.println("[Scene1] Unloading resources...");

        
        System.out.println("[Scene1] Resources unloaded successfully!");
    }

    @Override
    public void update(float deltaTime) {
        // Update logic for Scene1 if needed
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.end();
        batch.begin();
        batch.draw(backgroundTexture, -350, -300, 1000, 1000); 
        
    }
}
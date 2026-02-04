package io.github.some_example_name.lwjgl3.scenes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.Player;
import io.github.some_example_name.lwjgl3.entities.Wall;
import io.github.some_example_name.lwjgl3.managers.EntityManager;
import io.github.some_example_name.lwjgl3.managers.InputManager;

public class Scene1 extends Scene{
    private EntityManager entityManager;
    private InputManager inputManager;

    public Scene1(EntityManager entityManager, InputManager inputManager) {
        super("Scene1");
        this.entityManager = entityManager;
        this.inputManager = inputManager;
    }

    @Override
    public void onLoad() {
        System.out.println("[Scene1] Loading resources...");
        System.out.println("[Scene1] Resources loaded successfully!");
    }

    @Override
    public void onEnter() {
        System.out.println("[Scene1] Entering scene...");
        
        // add player entity in the center
        Player player = new Player(inputManager);
        player.setId("player_scene1");
        player.setPosition(new Vector2(320, 240));
        
        // Add to scene entity list 
        addEntity(player);
        
        // Add to global entity manager (for rendering and updates)
        entityManager.addEntity(player);
        
        // Top wall
        Wall topWall = new Wall(0, 460, 640, 20);
        topWall.setId("wall_top");
        addEntity(topWall);
        entityManager.addEntity(topWall);
        
        // Bottom wall
        Wall bottomWall = new Wall(0, 0, 640, 20);
        bottomWall.setId("wall_bottom");
        addEntity(bottomWall);
        entityManager.addEntity(bottomWall);
        
        // Left wall
        Wall leftWall = new Wall(0, 0, 20, 480);
        leftWall.setId("wall_left");
        addEntity(leftWall);
        entityManager.addEntity(leftWall);
        
        // Right wall
        Wall rightWall = new Wall(620, 0, 20, 480);
        rightWall.setId("wall_right");
        addEntity(rightWall);
        entityManager.addEntity(rightWall);
        
        System.out.println("[Scene1] Scene setup complete. Entities created: " + getEntityList().size());
    }

    @Override
    public void onExit() {
        System.out.println("[Scene1] Exiting scene...");
        
        // Remove all entities from the entity manager
        for (Entity entity : getEntityList()) {
            entityManager.removeEntity(entity.getId());
        }
        
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
        // Scene-specific rendering if needed
        // Entities are rendered by the entity manager
    }
}
package io.github.some_example_name.lwjgl3.collision;

import com.badlogic.gdx.math.Rectangle;

import io.github.some_example_name.lwjgl3.entities.AiEntity;
import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.Wall;
import io.github.some_example_name.lwjgl3.entities.iCollidable;
import io.github.some_example_name.lwjgl3.scenes.SceneManager;
import io.github.some_example_name.lwjgl3.outputs.OutputManager;

public final class CollisionResolver {
    private SceneManager sceneManager;
    private OutputManager outputManager;

    public CollisionResolver(SceneManager sceneManager, OutputManager outputManager) {
        // Constructor can be used to set up references if needed
        this.sceneManager = sceneManager; 
        this.outputManager = outputManager;
    }

    // Generic Collisions
    public void resolveCollisions(iCollidable a, iCollidable b) {
        a.collide(b); 
    }

    // PLAYER vs WALL
    public void resolveCollisions(PlayableEntity player, Wall wall) {
        resolveEntityWall(player, wall);
    }

    // AI vs WALL
    public void resolveCollisions(AiEntity ai, Wall wall) {
        resolveEntityWall(ai, wall);
    }

    // PLAYER vs AI
    public void resolveCollisions(PlayableEntity player, AiEntity ai) {
        // Transition to Game Over scene
        System.out.println("Player collided with AI!");
        outputManager.playSound("HIT_EVENT");
        sceneManager.setCurrentScene("Scene3"); 
    }

    // Shared Entity-Wall Collision Logic
    public void resolveEntityWall(Entity entity, Wall wall) 
    {
        System.out.println(entity.getId() + " collided with " + wall.getId());
        
        // Play sound 
        if (entity instanceof PlayableEntity) {
            PlayableEntity player = (PlayableEntity) entity;
            
            if (player.isNewWallCollision()) {
                outputManager.playSound("COLLISION_EVENT");
            }
        }

        Rectangle entityBounds = ((iCollidable) entity).getCollisionBounds();
        Rectangle wallBounds   = wall.getCollisionBounds();

        float overlapLeft   = (entityBounds.x + entityBounds.width) - wallBounds.x;
        float overlapRight  = (wallBounds.x + wallBounds.width) - entityBounds.x;
        float overlapBottom = (entityBounds.y + entityBounds.height) - wallBounds.y;
        float overlapTop    = (wallBounds.y + wallBounds.height) - entityBounds.y;

        float minX = Math.min(overlapLeft, overlapRight);
        float minY = Math.min(overlapBottom, overlapTop);
        
        float pushBuffer = 0.5f;

        // Resolve along the smallest overlap axis
        if (minX < minY) {
            // Horizontal collision
            if (overlapLeft < overlapRight)
                // Player hit wall from left
                entity.getPosition().x -= (overlapLeft + pushBuffer);
            else
                // Player hit wall from right
                entity.getPosition().x += (overlapRight + pushBuffer);
        }
        else {
            // Veritcal collision
            if (overlapBottom < overlapTop)
                // Player hit wall from below
                entity.getPosition().y -= (overlapBottom + pushBuffer);
            else
                // Player hit wall from above
                entity.getPosition().y += (overlapTop + pushBuffer);
        }
    }
}

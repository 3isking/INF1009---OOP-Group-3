package io.github.some_example_name.lwjgl3.collision;

import com.badlogic.gdx.math.Rectangle;

import io.github.some_example_name.lwjgl3.entities.AiEntity;
import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.Obstacle;
import io.github.some_example_name.lwjgl3.entities.iCollidable;
import io.github.some_example_name.lwjgl3.outputs.OutputManager;
import io.github.some_example_name.lwjgl3.scenes.SceneManager;

public final class CollisionResolver {
    private SceneManager sceneManager;
    private OutputManager outputManager;
    private Boolean criticalCollisionOccurred = false;

    public CollisionResolver(SceneManager sceneManager, OutputManager outputManager) {
        // Constructor can be used to set up references if needed
        this.sceneManager = sceneManager; 
        this.outputManager = outputManager;
    }

    public boolean hasCriticalCollisionOccurred() {
        if (criticalCollisionOccurred) {
            criticalCollisionOccurred = false;
            return true;
        }
        return false;
    }

    // Generic Collisions
    public void resolveCollisions(iCollidable a, iCollidable b) {
        a.collide(b); 
    }

    // PLAYER vs WALL
    public void resolveCollisions(PlayableEntity player, Obstacle obstacle) {
        resolveEntityObstacle(player, obstacle);
    }

    // AI vs WALL
    public void resolveCollisions(AiEntity ai, Obstacle obstacle) {
        resolveEntityObstacle(ai, obstacle);
    }

    // PLAYER vs AI
    public void resolveCollisions(PlayableEntity player, AiEntity ai) {
        // Transition to Game Over scene
        System.out.println("[Collision] Player collided with AI!");
        outputManager.playSound("HIT_EVENT");
        sceneManager.setCurrentScene("Scene3"); 
        criticalCollisionOccurred = true;
    }

    // Shared Entity-Obstacle Collision Logic
    public void resolveEntityObstacle(Entity entity, Obstacle obstacle) 
    {
        System.out.println("[Collision] " + entity.getId() + " collided with " + obstacle.getId());
        
        // Play sound 
        if (entity instanceof PlayableEntity) {
            PlayableEntity player = (PlayableEntity) entity;
            
            if (player.isNewObstacleCollision()) {
                outputManager.playSound("COLLISION_EVENT");
            }
        }

        Rectangle entityBounds = ((iCollidable) entity).getCollisionBounds();
        Rectangle obstacleBounds   = obstacle.getCollisionBounds();

        float overlapLeft   = (entityBounds.x + entityBounds.width) - obstacleBounds.x;
        float overlapRight  = (obstacleBounds.x + obstacleBounds.width) - entityBounds.x;
        float overlapBottom = (entityBounds.y + entityBounds.height) - obstacleBounds.y;
        float overlapTop    = (obstacleBounds.y + obstacleBounds.height) - entityBounds.y;

        float minX = Math.min(overlapLeft, overlapRight);
        float minY = Math.min(overlapBottom, overlapTop);
        
        float pushBuffer = 0.5f;

        // Resolve along the smallest overlap axis
        if (minX < minY) {
            // Horizontal collision
            if (overlapLeft < overlapRight)
                // Player hit obstacle from left
                entity.getPosition().x -= (overlapLeft + pushBuffer);
            else
                // Player hit obstacle from right
                entity.getPosition().x += (overlapRight + pushBuffer);
        }
        else {
            // Veritcal collision
            if (overlapBottom < overlapTop)
                // Player hit obstacle from below
                entity.getPosition().y -= (overlapBottom + pushBuffer);
            else
                // Player hit obstacle from above
                entity.getPosition().y += (overlapTop + pushBuffer);
        }
    }
}

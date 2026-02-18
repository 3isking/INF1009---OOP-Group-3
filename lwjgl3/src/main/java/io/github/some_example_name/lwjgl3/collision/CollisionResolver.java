package io.github.some_example_name.lwjgl3.collision;

import com.badlogic.gdx.math.Rectangle;

import io.github.some_example_name.lwjgl3.entities.AiEntity;
import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.Obstacle;
import io.github.some_example_name.lwjgl3.entities.iCollidable;

public final class CollisionResolver {
    // Generic Collisions
    public void resolveCollisions(iCollidable a, iCollidable b) {
        a.collide(b); 
    }

    // PLAYER vs obstacle
    public void resolveCollisions(PlayableEntity player, Obstacle obstacle) {
        resolveEntityObstacle(player, obstacle);
    }

    // AI vs obstacle
    public void resolveCollisions(AiEntity ai, Obstacle obstacle) {
        resolveEntityObstacle(ai, obstacle);
    }

    // PLAYER vs AI
    public void resolveCollisions(PlayableEntity player, AiEntity ai) {
        System.out.println("Player collided with AI!");

    }

    // Shared Entity-obstacle Collision Logic
    public void resolveEntityObstacle(Entity entity, Obstacle obstacle) 
    {
        System.out.println(entity.getId() + " collided with " + obstacle.getId());

        Rectangle entityBounds = ((iCollidable) entity).getCollisionBounds();
        Rectangle obstacleBounds   = obstacle.getCollisionBounds();

        float overlapLeft   = (entityBounds.x + entityBounds.width) - obstacleBounds.x;
        float overlapRight  = (obstacleBounds.x + obstacleBounds.width) - entityBounds.x;
        float overlapBottom = (entityBounds.y + entityBounds.height) - obstacleBounds.y;
        float overlapTop    = (obstacleBounds.y + obstacleBounds.height) - entityBounds.y;

        float minX = Math.min(overlapLeft, overlapRight);
        float minY = Math.min(overlapBottom, overlapTop);
        
        float pushBuffer = 5.0f;

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

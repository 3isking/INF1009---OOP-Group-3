package io.github.some_example_name.lwjgl3.collision;

import com.badlogic.gdx.math.Rectangle;

import io.github.some_example_name.lwjgl3.entities.AiEntity;
import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.Wall;
import io.github.some_example_name.lwjgl3.entities.iCollidable;

public final class CollisionResolver {
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
        System.out.println("Player collided with AI!");

    }

    // Shared Entity-Wall Collision Logic
    public void resolveEntityWall(Entity entity, Wall wall) 
    {
        System.out.println(entity.getId() + " collided with " + wall.getId());

        Rectangle entityBounds = ((iCollidable) entity).getCollisionBounds();
        Rectangle wallBounds   = wall.getCollisionBounds();

        float overlapLeft   = (entityBounds.x + entityBounds.width) - wallBounds.x;
        float overlapRight  = (wallBounds.x + wallBounds.width) - entityBounds.x;
        float overlapBottom = (entityBounds.y + entityBounds.height) - wallBounds.y;
        float overlapTop    = (wallBounds.y + wallBounds.height) - entityBounds.y;

        float minX = Math.min(overlapLeft, overlapRight);
        float minY = Math.min(overlapBottom, overlapTop);
        
        float pushBuffer = 5.0f;

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

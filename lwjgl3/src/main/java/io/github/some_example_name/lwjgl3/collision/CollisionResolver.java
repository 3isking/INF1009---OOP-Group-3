package io.github.some_example_name.lwjgl3.collision;

import com.badlogic.gdx.math.Rectangle;

import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.Player;
import io.github.some_example_name.lwjgl3.entities.Wall;
import io.github.some_example_name.lwjgl3.entities.iCollidable;

public class CollisionResolver {
    public void resolveCollisions(iCollidable entity1, iCollidable entity2)
    {

        // Collision Between Player & Wall
        if (entity1 instanceof Player && entity2 instanceof Wall || entity2 instanceof Player && entity1 instanceof Wall)
        {
            iCollidable playerEntity;
            iCollidable wallEntity;

            if (entity1 instanceof Player){
                playerEntity = entity1;
                wallEntity = entity2;
            } else {
                playerEntity = entity2;
                wallEntity = entity1;
            }

            Rectangle player = playerEntity.getCollisionBounds();
            Rectangle wall = wallEntity.getCollisionBounds();
            
            float overlapLeft = (player.x + player.width) - wall.x;
            float overlapRight = (wall.x + wall.width) - player.x;
            float overlapBottom = (player.y + player.height) - wall.y;
            float overlapTop = (wall.y + wall.height) - player.y;
            float minX = Math.min(overlapLeft, overlapRight);
            float minY = Math.min(overlapBottom, overlapTop);
            
            float pushBuffer = 5.0f;

            // Resolve along the smallest overlap axis
            if (minX < minY) {
                // Horizontal collision
                if (overlapLeft < overlapRight) {
                    // Player hit wall from left
                    ((Entity) playerEntity).getPosition().x -= (overlapLeft + pushBuffer);
                } else {
                    // Player hit wall from right
                    ((Entity) playerEntity).getPosition().x += (overlapRight + pushBuffer);
                }
            } else {
                // Vertical collision
                if (overlapBottom < overlapTop) {
                    // Player hit wall from below
                    ((Entity) playerEntity).getPosition().y -= (overlapBottom + pushBuffer);
                } else {
                    // Player hit wall from above
                    ((Entity) playerEntity).getPosition().y += (overlapTop + pushBuffer);
                }
            }
            
        }
    }
}
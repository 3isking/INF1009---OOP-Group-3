package io.github.some_example_name.lwjgl3.managers;

import java.util.List;

import io.github.some_example_name.lwjgl3.entities.iCollidable;
import io.github.some_example_name.lwjgl3.entities.Entity;
import com.badlogic.gdx.math.Rectangle;

public class CollisionManager {
    // check collisions between all entities in the given list
    public void checkCollisions(List<Entity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            // pick the first entity
            Entity e1 = entities.get(i);
            // skip entities that cannot collide
            if (!(e1 instanceof iCollidable)) 
                continue;
            // if can collide, then store in e1
            iCollidable c1 = (iCollidable) e1;

            // compare with remaining entities
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e2 = entities.get(j);
                // skip entities that cannot collide
                if (!(e2 instanceof iCollidable)) 
                    continue;
                // if can collide, then store in e2
                iCollidable c2 = (iCollidable) e2;

                // check if the collision bounds overlap
                if (c1.getCollisionBounds().overlaps(c2.getCollisionBounds())) {
                    // notify both entities of the collision
                    c1.onCollision(c2);
                    c2.onCollision(c1);
                }
            }
        }
    }

    // check whether a given rectangle collides with any collidable entity in the list
    public boolean checkCollision(Rectangle bounds, List<Entity> entities) {
        // loop through all entities
        for (Entity e : entities) {
            // only check entities that support collision
            if (e instanceof iCollidable) {
                Rectangle entityBounds = ((iCollidable) e).getCollisionBounds();
                // if the rectangle overlaps any collision bounds, collision exists
                 if (bounds.overlaps(entityBounds)) {
                    System.out.println("Collision detected with entity: " + e.getId());
                    return true;
                }
            }
        }
        return false;
    }
}
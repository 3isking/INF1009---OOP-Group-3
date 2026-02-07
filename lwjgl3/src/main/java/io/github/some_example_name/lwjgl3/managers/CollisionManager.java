package io.github.some_example_name.lwjgl3.managers;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

import io.github.some_example_name.lwjgl3.collision.CollisionDetector;
import io.github.some_example_name.lwjgl3.collision.CollisionResolver;
import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.iCollidable;

public class CollisionManager {
    private CollisionDetector collisionDetector;
    private CollisionResolver collisionResolver;

    public CollisionManager() 
    {
        collisionDetector = new CollisionDetector();
        collisionResolver = new CollisionResolver();
    }
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

                // used for collision detector
                // check if the collision bounds overlap
                if (collisionDetector.checkCollisions(c1, c2)) {
                    // used for collision resolver
                    // notify both entities of the collision
                    //  CollisionResolver.onCollision(c1, c2);
                    collisionResolver.resolveCollisions(c1, c2);
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
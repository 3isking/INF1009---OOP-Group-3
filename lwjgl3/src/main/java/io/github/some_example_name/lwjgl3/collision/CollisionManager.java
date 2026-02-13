package io.github.some_example_name.lwjgl3.collision;

import java.util.ArrayList;
import java.util.List;

import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.iCollidable;

public class CollisionManager {
    private CollisionDetector collisionDetector;
    private CollisionResolver collisionResolver;
    private List<iCollidable> collidableEntities;


    public CollisionManager() 
    {
        this.collisionDetector = new CollisionDetector();
        this.collisionResolver = new CollisionResolver();
        this.collidableEntities = new ArrayList<>();
    }
    // check collisions between all entities in the given list
    public void checkCollisions(List<Entity> entities) {
        // pick the first entity
        for (iCollidable collidable : collidableEntities) {
            if (collidable instanceof PlayableEntity) {
                ((PlayableEntity) collidable).resetCollisionState();
            }

            // pick the first entity
            for (iCollidable otherCollidable : collidableEntities) {
                // used for collision detector
                // check if the collision bounds overlap
                if (collidable != otherCollidable) {
                    // used for collision resolver
                    // notify both entities of the collision
                    //  CollisionResolver.onCollision(collidable, otherCollidable);
                    if (collisionDetector.checkCollisions(collidable, otherCollidable)) {
                        collisionResolver.resolveCollisions(collidable, otherCollidable);
                    }
                }
            }
        }
    }

    public void addCollidableEntity(iCollidable collidable){
        collidableEntities.add(collidable);
    }
}
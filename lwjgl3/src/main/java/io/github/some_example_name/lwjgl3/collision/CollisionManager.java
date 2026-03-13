package io.github.some_example_name.lwjgl3.collision;

import java.util.ArrayList;
import java.util.List;

import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.iCollidable;
import io.github.some_example_name.lwjgl3.scenes.SceneManager;
import io.github.some_example_name.lwjgl3.outputs.OutputManager;

public class CollisionManager implements iCollisionManager {
    private SceneManager sceneManager;
    private CollisionDetector collisionDetector;
    private CollisionResolver collisionResolver;
    private List<iCollidable> collidableEntities;

    public void setCollisionManager(SceneManager sceneManager, OutputManager outputManager){
        this.sceneManager = sceneManager;
        this.collisionDetector = new CollisionDetector();
        this.collisionResolver = new CollisionResolver(this.sceneManager, outputManager);
        this.collidableEntities = new ArrayList<>();
    }
    // check collisions between all entities in the given list
    
    public void checkCollisions(List<Entity> entities) {
        // Create a copy of the list
        // Allows the real list to be cleared safely if a collision triggers a scene change
        List<iCollidable> snapshot = new ArrayList<>(collidableEntities);

        // Iterate over the snapshot instead of the live list
        for (iCollidable collidable : snapshot) {
            if (collidable instanceof PlayableEntity) {
                ((PlayableEntity) collidable).resetCollisionState();
            }

            for (iCollidable otherCollidable : snapshot) {
                if (collidable != otherCollidable) {
                    if (collisionDetector.checkCollisions(collidable, otherCollidable)) {
                        collisionResolver.resolveCollisions(collidable, otherCollidable);

                        // Exit if Critical Collision Occurs
                        if (collisionResolver.hasCriticalCollisionOccurred()) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public CollisionResolver getResolver() {
        return this.collisionResolver;
    }  

    public void addCollidableEntity(iCollidable collidable){
        collidableEntities.add(collidable);
    }

    public void emptyCollidableEntities() {
        collidableEntities.clear();
    }
}
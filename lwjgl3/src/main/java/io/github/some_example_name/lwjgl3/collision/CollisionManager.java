package io.github.some_example_name.lwjgl3.collision;

import java.util.ArrayList;
import java.util.List;

import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.iCollidable;
import io.github.some_example_name.lwjgl3.entities.iEntityManager;
import io.github.some_example_name.lwjgl3.scenes.iSceneManager;
import io.github.some_example_name.lwjgl3.outputs.iOutputManager;

public class CollisionManager implements iCollisionManager {
    private iSceneManager sceneManager;
    private CollisionDetector collisionDetector;
    private CollisionResolver collisionResolver;
    private List<iCollidable> collidableEntities;

    public void setCollisionManager(iEntityManager entityManager, iSceneManager sceneManager, iOutputManager outputManager) {
        this.sceneManager = sceneManager;
        this.collisionDetector = new CollisionDetector();
        this.collisionResolver = new CollisionResolver(sceneManager, outputManager, this);
        this.collidableEntities = new ArrayList<>();
    }

    public void checkCollisions(List<Entity> entities) {
        List<iCollidable> snapshot = new ArrayList<>(collidableEntities);

        for (iCollidable collidable : snapshot) {
            if (collidable instanceof PlayableEntity) {
                ((PlayableEntity) collidable).resetCollisionState();
            }

            for (iCollidable otherCollidable : snapshot) {
                if (collidable != otherCollidable) {
                    if (collisionDetector.checkCollisions(collidable, otherCollidable)) {
                        collisionResolver.resolveCollisions(collidable, otherCollidable);

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

    public void addCollidableEntity(iCollidable collidable) {
        collidableEntities.add(collidable);
    }

    public List<iCollidable> getCollidableEntities() {
        return new ArrayList<>(collidableEntities);
    }

    public void removeCollidableEntity(iCollidable collidable) {
        collidableEntities.remove(collidable);
    }

    public void emptyCollidableEntities() {
        collidableEntities.clear();
    }
}
package io.github.some_example_name.lwjgl3.collision;

import java.util.List;

import io.github.some_example_name.lwjgl3.entities.iCollidable;

public interface iCollisionManager {
    void checkCollisions();
    void addCollidableEntity(iCollidable collidable);
    void emptyCollidableEntities();
    CollisionResolver getResolver();
    List<iCollidable> getCollidableEntities();
    void removeCollidableEntity(iCollidable collidable);
}
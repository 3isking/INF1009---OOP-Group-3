package io.github.some_example_name.lwjgl3.movement;
import io.github.some_example_name.lwjgl3.entities.iMovable;



public interface iMovementManager {
    void moveEntities();
    void addMovableEntity(iMovable movable);
    void emptyMovableEntities();
    PlayerMovement getPlayerMovement();
}

package io.github.some_example_name.lwjgl3.movement;
import java.util.List;

import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.iMovable;
import io.github.some_example_name.lwjgl3.inputs.iInputManager;

public interface iMovementManager {
    void moveEntities(List<Entity> entityList, iInputManager inputManager);
    void addMovableEntity(iMovable movable);
    void emptyMovableEntities();
    PlayerMovement getPlayerMovement();
}

package io.github.some_example_name.lwjgl3.movement;
import io.github.some_example_name.lwjgl3.entities.Entity;
import java.util.List;
import io.github.some_example_name.lwjgl3.entities.iMovable;



public interface iMovementManager {
    void moveEntities(List<Entity> entityList);
    void addMovableEntity(iMovable movable);
    void emptyMovableEntities();
    PlayerMovement getPlayerMovement();
    AIMovement getAiMovement();
}

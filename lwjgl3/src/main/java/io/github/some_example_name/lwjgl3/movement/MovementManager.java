package io.github.some_example_name.lwjgl3.movement;

import java.util.List;

import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.iMovable;
import io.github.some_example_name.lwjgl3.inputs.InputManager;

public class MovementManager {

    private final InputManager inputManager;
    private final MovementStrategy playerMovement;
    private final MovementStrategy aiMovement;

    public MovementManager(InputManager inputManager) {
        this.inputManager = inputManager;
        this.playerMovement = new PlayerMovement();
        this.aiMovement = new AIMovement();
    }

    public void moveEntities(List<Entity> entityList){
        for (Entity entity: entityList){
            if (entity instanceof iMovable){
                iMovable movable = (iMovable) entity;

                if (entity instanceof PlayableEntity){
                    playerMovement.move(movable, inputManager);
                } else {
                    aiMovement.move(movable, null);
                }
            }

            
        }
    }
}

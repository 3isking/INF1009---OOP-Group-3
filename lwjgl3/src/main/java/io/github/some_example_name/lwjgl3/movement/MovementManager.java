package io.github.some_example_name.lwjgl3.movement;

import java.util.ArrayList;
import java.util.List;

import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.iMovable;
import io.github.some_example_name.lwjgl3.inputs.iInputManager;

public class MovementManager implements iMovementManager{
    private final iInputManager inputManager;
    private final PlayerMovement playerMovement;
    private List<iMovable> movableEntities;

    public MovementManager(iInputManager inputManager) {
        this.inputManager = inputManager;
        this.playerMovement = new PlayerMovement();
        this.movableEntities = new ArrayList<>();
    }

    public void moveEntities() {
        for (iMovable movable : movableEntities) {
            MovementStrategy strategy = movable.getMovementStrategy();
            if (strategy != null) {
                strategy.move(movable, inputManager);
            }
        }
    }

    public void addMovableEntity(iMovable movable){
        movableEntities.add(movable);
    }

    public void emptyMovableEntities() {
        movableEntities.clear();
    }

    public PlayerMovement getPlayerMovement() {
        return playerMovement;
    }
}

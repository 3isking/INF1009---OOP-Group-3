package io.github.some_example_name.lwjgl3.movement;

import java.util.ArrayList;
import java.util.List;

import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.iMovable;
import io.github.some_example_name.lwjgl3.inputs.iInputManager;

public class MovementManager implements iMovementManager{
    private final iInputManager inputManager;
    private final PlayerMovement playerMovement;
    private final AIMovement aiMovement;
    private List<iMovable> movableEntitys;

    public MovementManager(iInputManager inputManager) {
        this.inputManager = inputManager;
        this.playerMovement = new PlayerMovement();
        this.aiMovement = new AIMovement();
        this.movableEntitys = new ArrayList<>();
    }

    public void moveEntities(List<Entity> entityList) {
        for (iMovable movable : movableEntitys) {
            MovementStrategy strategy = movable.getMovementStrategy();
            if (strategy != null) {
                strategy.move(movable, inputManager);
            }
        }
    }

    public void addMovableEntity(iMovable movable){
        movableEntitys.add(movable);
    }

    public void emptyMovableEntities() {
        movableEntitys.clear();
    }

    public PlayerMovement getPlayerMovement() {
        return playerMovement;
    }

    public AIMovement getAiMovement() {
        return aiMovement;
    }
}

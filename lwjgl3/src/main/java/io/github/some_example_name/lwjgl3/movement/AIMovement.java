package io.github.some_example_name.lwjgl3.movement;

import io.github.some_example_name.lwjgl3.entities.iMovable;
import io.github.some_example_name.lwjgl3.inputs.InputManager;

import com.badlogic.gdx.math.Vector2;

public class AIMovement implements MovementStrategy {

    @Override
    public void move(iMovable entity, InputManager inputManager) {
        if (entity == null) return;

        // Basic AI: move according to current velocity (no input)
        Vector2 vel = entity.getVelocity();
        if (vel == null) return;

        entity.setPosition(entity.getPosition().add(vel));
    }
}
package io.github.some_example_name.lwjgl3.movement;

import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.entities.iMovable;
import io.github.some_example_name.lwjgl3.inputs.InputManager;

public class PlayerMovement implements MovementStrategy {

    private final float speed = 3f;

    @Override
    public void move(iMovable entity, InputManager inputManager) {
        if (entity == null || inputManager == null) {
            return;
        }

        Vector2 newVel = new Vector2(0, 0);
        if (inputManager.inputHeld("left")) {
            newVel = new Vector2(-speed, 0);
        }
        if (inputManager.inputHeld("right")) {
            newVel = new Vector2(speed, 0);
        }
        if (inputManager.inputHeld("down")) {
            newVel = new Vector2(0, -speed);
        }
        if (inputManager.inputHeld("up")) {
            newVel = new Vector2(0, speed);
        }

        entity.setVelocity(newVel);

        // apply movement immediately
        entity.setPosition(entity.getPosition().add(entity.getVelocity()));

        // reset velocity for next frame
        entity.setVelocity(new Vector2(0, 0));
    }
}
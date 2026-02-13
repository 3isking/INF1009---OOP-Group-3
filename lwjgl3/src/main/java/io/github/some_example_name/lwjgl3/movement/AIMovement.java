package io.github.some_example_name.lwjgl3.movement;

import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.entities.iMovable;
import io.github.some_example_name.lwjgl3.inputs.InputManager;

public class AIMovement implements MovementStrategy {
    private float timer = 0f;
    private boolean goingLeft = true;
    private final float interval = 1f; // seconds
    private final float speed = 0.5f;    // adjust as needed

    @Override
    public void move(iMovable entity, InputManager inputManager) {
        if (entity == null) return;

        // Use Gdx.graphics.getDeltaTime() to get frame time
        float deltaTime = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        timer += deltaTime;

        if (timer >= interval) {
            goingLeft = !goingLeft;
            timer = 0f;
        }

        // Set velocity based on direction
        Vector2 velocity = entity.getVelocity();
        if (goingLeft) {
            velocity.x = -speed;
        } else {
            velocity.x = speed;
        }
        entity.setVelocity(velocity);

        // Move entity
        Vector2 position = entity.getPosition();
        if (position != null) {
            entity.setPosition(position.add(velocity));
        }
    }
}
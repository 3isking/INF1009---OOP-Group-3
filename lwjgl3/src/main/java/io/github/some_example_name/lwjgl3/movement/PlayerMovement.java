package io.github.some_example_name.lwjgl3.movement;

import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.entities.iMovable;
import io.github.some_example_name.lwjgl3.inputs.InputManager;

public class PlayerMovement implements MovementStrategy {

    private final float horizontalSpeed = 3f;
    private final float verticalSpeed = 3000f;
    private final float[] lanes = {-120f, 0f, 120f};
    private int currentLane = 1;
    private float laneSwapCooldown = 0.2f;
    private float laneSwapTimer = 0f;

    @Override
    public void move(iMovable entity, InputManager inputManager) {
        
        if (entity == null || inputManager == null) {
            return;
        }

        // Use Gdx.graphics.getDeltaTime() to get frame time
        float deltaTime = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        laneSwapTimer += deltaTime;

        // Side Movements
        Vector2 newVel = new Vector2(0, 0);
        if (inputManager.inputHeld("left")) {
            newVel = new Vector2(-horizontalSpeed, 0);
        }
        if (inputManager.inputHeld("right")) {
            newVel = new Vector2(horizontalSpeed, 0);
        }
        entity.setVelocity(newVel);

        // apply movement immediately
        entity.setPosition(entity.getPosition().add(entity.getVelocity()));

        // Lane Swaps
        if (inputManager.inputHeld("down") && laneSwapTimer > laneSwapCooldown) {
            currentLane--;
            if (currentLane < 0){
                currentLane = 0;
            }
            laneSwapTimer = 0;
        }
        if (inputManager.inputHeld("up") && laneSwapTimer > laneSwapCooldown) {
            currentLane++;
            if (currentLane > 2){
                currentLane = 2;
            }
            laneSwapTimer = 0;
        }

        // Slowly Increment Movement over Time by Calculating Distance between Target and Current
        float targetY = lanes[currentLane];
        float currentY = entity.getPosition().y;
        float diff = targetY - currentY;

        if (Math.abs(diff) > 1f) {
            float direction = Math.signum(diff);
            float moveAmount = verticalSpeed * deltaTime * direction;

            // Prevent overshooting the lane
            if (Math.abs(moveAmount) > Math.abs(diff)) {
                moveAmount = diff;
            }

            entity.setPosition(new Vector2(entity.getPosition().x,currentY + moveAmount));
        }

        // reset velocity for next frame
        entity.setVelocity(new Vector2(0, 0));
    }
}
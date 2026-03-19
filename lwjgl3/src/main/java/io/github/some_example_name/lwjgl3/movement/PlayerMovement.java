package io.github.some_example_name.lwjgl3.movement;

import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.entities.iMovable;
import io.github.some_example_name.lwjgl3.inputs.iInputManager;

public class PlayerMovement implements MovementStrategy {

    private static final float SCREEN_W = 640f;
    private static final float SCREEN_H = 480f;

    private final float verticalSpeed = 3000f;
    float topY = -SCREEN_H / 2f + SCREEN_H * 0.85f - 30 / 2f;
    float middleY = -SCREEN_H / 2f + SCREEN_H * 0.5f - 30 / 2f;
    float bottomY = -SCREEN_H / 2f + SCREEN_H * 0.15f - 30 / 2f;
    private final float[] lanes = {bottomY, middleY, topY};
    private int currentLane = 1;
    private float laneSwapCooldown = 0.2f;
    private float laneSwapTimer = 0f;

    @Override
    public void move(iMovable entity, iInputManager inputManager) {
        
        if (entity == null || inputManager == null) {
            return;
        }

        // Use Gdx.graphics.getDeltaTime() to get frame time
        float deltaTime = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        laneSwapTimer += deltaTime;
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
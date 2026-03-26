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

   @Override
    public void move(iMovable entity, iInputManager inputManager) {
        
        if (entity == null || inputManager == null) {
            return;
        }

        float deltaTime = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        // inputPressed fires ONCE per keypress, no cooldown needed
        if (inputManager.inputPressed("down")) {
            currentLane--;
            if (currentLane < 0) currentLane = 0;
        }
        if (inputManager.inputPressed("up")) {
            currentLane++;
            if (currentLane > 2) currentLane = 2;
        }

        // Smooth movement toward target lane
        float targetY  = lanes[currentLane];
        float currentY = entity.getPosition().y;
        float diff     = targetY - currentY;

        if (Math.abs(diff) > 1f) {
            float direction  = Math.signum(diff);
            float moveAmount = verticalSpeed * deltaTime * direction;

            if (Math.abs(moveAmount) > Math.abs(diff)) {
                moveAmount = diff;
            }

            entity.setPosition(new Vector2(entity.getPosition().x, currentY + moveAmount));
        }

        entity.setVelocity(new Vector2(0, 0));
    }
}

package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.movement.MovementStrategy;

public interface iMovable {
    Vector2 getPosition();
    void setPosition(Vector2 position);

    public Vector2 getVelocity();
    public void setVelocity(Vector2 velocity);
    MovementStrategy getMovementStrategy();
}

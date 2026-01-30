package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.math.Vector2;

public interface iMovable {
    public Vector2 getVelocity();
    public void setVelocity(Vector2 velocity);
    public void move();
}

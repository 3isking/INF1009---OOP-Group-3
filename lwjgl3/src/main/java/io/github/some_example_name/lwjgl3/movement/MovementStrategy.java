package io.github.some_example_name.lwjgl3.movement;

import io.github.some_example_name.lwjgl3.entities.iMovable;
import io.github.some_example_name.lwjgl3.inputs.iInputManager;

public interface MovementStrategy {
    void move(iMovable entity, iInputManager inputManager);
}

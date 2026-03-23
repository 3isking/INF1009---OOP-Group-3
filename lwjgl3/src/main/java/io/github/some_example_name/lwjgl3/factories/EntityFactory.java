package io.github.some_example_name.lwjgl3.factories;
import io.github.some_example_name.lwjgl3.entities.Entity;

public interface EntityFactory<T extends Entity> {
    T createEntity(Class<T> type, float x, float y, Object extra);
}
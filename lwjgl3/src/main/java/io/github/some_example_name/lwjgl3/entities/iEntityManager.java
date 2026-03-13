package io.github.some_example_name.lwjgl3.entities;

import java.util.List;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface iEntityManager {
    void addEntity(Entity entity);
    Entity getEntity(String id);
    List<Entity> getAllEntities();
    void removeEntity(String id);
    void updateAllEntities(float deltaTime);
    void render(SpriteBatch batch);
    void clear();
}
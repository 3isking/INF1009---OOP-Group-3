package io.github.some_example_name.lwjgl3.managers;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.some_example_name.lwjgl3.entities.Entity;

public class EntityManager {
    private List<Entity> entities;

    public EntityManager() {
        this.entities = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        if (entity != null && !entities.contains(entity)) {
            entities.add(entity);
        }
    }

    public Entity getEntity(String id) {
        for (Entity entity : entities) {
            if (entity.getId() != null && entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    public List<Entity> getAllEntities() {
        return new ArrayList<>(entities);
    }

    public void removeEntity(String id) {
        entities.removeIf(entity -> entity.getId() != null && entity.getId().equals(id));
    }

    public void updateAllEntities(float deltaTime) {
        for (Entity entity : entities) {
            entity.update(deltaTime);
        }
    }

    public void render(SpriteBatch batch) {
        for (Entity entity : entities) {
            if (entity.isVisible()) {
                entity.render(batch);
            }
        }
    }

    public void clear() {
        entities.clear();
    }
}
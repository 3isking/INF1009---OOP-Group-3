package io.github.some_example_name.lwjgl3.entities;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.some_example_name.lwjgl3.collision.CollisionManager;
import io.github.some_example_name.lwjgl3.movement.MovementManager;

public class EntityManager {
    private List<Entity> entities;
    private MovementManager movementManager;
    private CollisionManager collisionManager;

    public EntityManager(MovementManager movementManager, CollisionManager collisionManager) {
        this.entities = new ArrayList<>();
        this.movementManager = movementManager;
        this.collisionManager = collisionManager;
    }

    public void addEntity(Entity entity) {
        if (entity != null && !entities.contains(entity)) {
            entities.add(entity);

            if (entity instanceof iMovable) {
                movementManager.addMovableEntity((iMovable) entity);
            }

            if (entity instanceof iCollidable) {
                collisionManager.addCollidableEntity((iCollidable) entity);
            }
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
        movementManager.emptyMovableEntities();
        collisionManager.emptyCollidableEntities();
    }
}
package io.github.some_example_name.lwjgl3.entities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.factories.EntityFactory;
import io.github.some_example_name.lwjgl3.movement.iMovementManager;

public class EntityManager implements iEntityManager {
    private List<Entity> entities;
    private iMovementManager movementManager;
    private iCollisionManager collisionManager;
    private final Map<Class<? extends Entity>, EntityFactory<? extends Entity>> factories = new HashMap<>();

    public EntityManager(iMovementManager movementManager, iCollisionManager collisionManager) {
        this.entities = new ArrayList<>();
        this.movementManager = movementManager;
        this.collisionManager = collisionManager;
    }

    public <T extends Entity> void registerFactory(Class<T> type, EntityFactory<T> factory) {
        factories.put(type, factory);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T createEntity(Class<T> type, float x, float y, Object extra) {
        EntityFactory<? extends Entity> factory = factories.get(type);
        if (factory != null) {
            return type.cast(((EntityFactory<T>) factory).createEntity(type, x, y, extra));
        } else {
            throw new IllegalArgumentException("Unknown entity type: " + type.getSimpleName());
        }
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
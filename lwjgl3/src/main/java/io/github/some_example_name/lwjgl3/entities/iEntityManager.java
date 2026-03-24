package io.github.some_example_name.lwjgl3.entities;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.factories.EntityFactory;
import io.github.some_example_name.lwjgl3.movement.iMovementManager;

public interface iEntityManager {
    void addEntity(Entity entity, iMovementManager movementManager, iCollisionManager collisionManager);
    Entity getEntity(String id);
    List<Entity> getAllEntities();
    void removeEntity(String id);
    void updateAllEntities(float deltaTime);
    void render(SpriteBatch batch);
    void clear(iMovementManager movementManager, iCollisionManager collisionManager);
    <T extends Entity> void registerFactory(Class<T> type, EntityFactory<T> factory);
    <T extends Entity> T createEntity(Class<T> type, float x, float y, Object extra);

}
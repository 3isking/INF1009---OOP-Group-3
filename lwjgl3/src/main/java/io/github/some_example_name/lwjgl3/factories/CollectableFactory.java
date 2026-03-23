package io.github.some_example_name.lwjgl3.factories;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.Collectable;
import io.github.some_example_name.lwjgl3.entities.Sprite;


public class CollectableFactory implements EntityFactory<Collectable>{
    public enum CollectableType {
        POWERUP
    }
    
    private iCollisionManager collisionManager;
    
    private final Map<CollectableType, BiFunction<Float, Float, Collectable>> registry = new HashMap<>();

    public CollectableFactory(iCollisionManager collisionManager) {
        this.collisionManager = collisionManager;
        // Register types here or via a public method
        registry.put(CollectableType.POWERUP, this::createPower);
    }

    @Override
    public Collectable createEntity(Class<Collectable> type, float x, float y, Object extra) {
        BiFunction<Float, Float, Collectable> constructor = registry.get(extra);
        if (constructor == null) {
            throw new IllegalArgumentException("No constructor registered for: " + extra);
        }
        return constructor.apply(x, y);
    }
    
    private Collectable createPower(float x, float y) {
        Collectable collectable = new Collectable(x, y, 0f, 0f, collisionManager);
        collectable.setSprite(new Sprite(new Texture(Gdx.files.internal("power.jpg")), 40f));
        return collectable;
    }

}
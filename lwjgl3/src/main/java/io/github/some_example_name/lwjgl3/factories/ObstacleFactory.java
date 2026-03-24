package io.github.some_example_name.lwjgl3.factories;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.Obstacle;
import io.github.some_example_name.lwjgl3.entities.Sprite;

public class ObstacleFactory implements EntityFactory<Obstacle> {

    public enum ObstacleType {
        ERASER,
        BOOKS
    }

    private iCollisionManager collisionManager;

    private final Map<ObstacleType, BiFunction<Float, Float, Obstacle>> registry = new HashMap<>();

    public ObstacleFactory(iCollisionManager collisionManager) {
        this.collisionManager = collisionManager;
        // Register types here or via a public method
        registry.put(ObstacleType.ERASER, this::createEraser);
        registry.put(ObstacleType.BOOKS, this::createBooks);
    }

    @Override
    public Obstacle createEntity(Class<Obstacle> type, float x, float y, Object extra) {
        BiFunction<Float, Float, Obstacle> constructor = registry.get(extra);
        if (constructor == null) {
            throw new IllegalArgumentException("No constructor registered for: " + extra);
        }
        return constructor.apply(x, y);
    }

    private Obstacle createEraser(float x, float y) {
        Obstacle obstacle = new Obstacle(x, y, 0, 0, collisionManager);
        obstacle.setSprite(new Sprite(new Texture(Gdx.files.internal("eraser.png")), 80f));
        obstacle.setId("eraser");
        return obstacle;
    }

    private Obstacle createBooks(float x, float y) {
        Obstacle obstacle = new Obstacle(x, y, 0, 0, collisionManager);
        obstacle.setSprite(new Sprite(new Texture(Gdx.files.internal("books.png")), 80f));
        obstacle.setId("books");
        return obstacle;
    }

    
}
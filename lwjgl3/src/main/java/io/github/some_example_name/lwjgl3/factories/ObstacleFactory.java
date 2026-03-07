package io.github.some_example_name.lwjgl3.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.lwjgl3.collision.CollisionManager;
import io.github.some_example_name.lwjgl3.entities.Obstacle;
import io.github.some_example_name.lwjgl3.entities.Sprite;

public class ObstacleFactory {

    public enum ObstacleType {
        ERASER,
        BOOKS
    }

    private CollisionManager collisionManager;

    public ObstacleFactory(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    public Obstacle create(ObstacleType type, float x, float y) {
        switch (type) {
            case ERASER:
                return createEraser(x, y);
            case BOOKS:
                return createBooks(x, y);
            default:
                throw new IllegalArgumentException("Unknown obstacle type: " + type);
        }
    }

    private Obstacle createEraser(float x, float y) {
        Obstacle obstacle = new Obstacle(x, y, 0, 0, collisionManager);
        obstacle.setSprite(new Sprite(new Texture(Gdx.files.internal("eraser.png")), 60f));
        return obstacle;
    }

    private Obstacle createBooks(float x, float y) {
        Obstacle obstacle = new Obstacle(x, y, 0, 0, collisionManager);
        obstacle.setSprite(new Sprite(new Texture(Gdx.files.internal("books.png")), 80f));
        return obstacle;
    }
}
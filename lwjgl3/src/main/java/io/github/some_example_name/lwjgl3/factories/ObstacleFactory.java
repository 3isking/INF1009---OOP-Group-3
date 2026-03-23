package io.github.some_example_name.lwjgl3.factories;

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

    public ObstacleFactory(iCollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    @Override
    public Obstacle createEntity(Class<Obstacle> type, float x, float y, Object extra) {
        ObstacleType obstacleType = (ObstacleType) extra;

        switch (obstacleType) {
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
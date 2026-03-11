package io.github.some_example_name.lwjgl3.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.lwjgl3.collision.CollisionManager;
import io.github.some_example_name.lwjgl3.entities.Collectable;
import io.github.some_example_name.lwjgl3.entities.Sprite;

public class CollectableFactory {
    public enum CollectableType {
        COIN,
        MAGNET,
        POWERUP
    }
    
    private CollisionManager collisionManager;
    
    public CollectableFactory(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }
    
    public Collectable create(CollectableType type, float x, float y) {
        switch (type) {
            case COIN:
                return createCoin(x, y);
            
            case MAGNET:
                return createMagnet(x, y);
            
            case POWERUP:
                return createPower(x, y);
            
            default:
                throw new IllegalArgumentException("Unknown collectable type: " + type);
        }
    }
    
    private Collectable createCoin(float x, float y) {
        Collectable collectable = new Collectable(x, y, 0, 0, collisionManager);
        collectable.setSprite(new Sprite(new Texture(Gdx.files.internal("coin.png")), 30f));
        return collectable;
    }
    
    private Collectable createMagnet(float x, float y) {
        Collectable collectable = new Collectable(x, y, 0, 0, collisionManager);
        collectable.setSprite(new Sprite(new Texture(Gdx.files.internal("magnet.png")), 40f));
        return collectable;
    }

    private Collectable createPower(float x, float y) {
        Collectable collectable = new Collectable(x, y, 0, 0, collisionManager);
        collectable.setSprite(new Sprite(new Texture(Gdx.files.internal("power.png")), 40f));
        return collectable;
    }
}
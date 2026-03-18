package io.github.some_example_name.lwjgl3.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.lwjgl3.collision.CollisionManager;
import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.Collectable;
import io.github.some_example_name.lwjgl3.entities.Sprite;

public class CollectableFactory {
    public enum CollectableType {
        POWERUP
    }
    
    private iCollisionManager collisionManager;
    
    public CollectableFactory(iCollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }
    
    public Collectable create(CollectableType type, float x, float y) {
        switch (type) {        
            case POWERUP:
                return createPower(x, y);
            
            default:
                throw new IllegalArgumentException("Unknown collectable type: " + type);
        }
    }

    private Collectable createPower(float x, float y) {
        Collectable collectable = new Collectable(x, y, 0f, 0f, (CollisionManager) collisionManager);
        collectable.setSprite(new Sprite(new Texture(Gdx.files.internal("power.jpg")), 40f));
        return collectable;
    }
}
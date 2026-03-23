package io.github.some_example_name.lwjgl3.factories;

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
    
    public CollectableFactory(iCollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    @Override
    public Collectable createEntity(Class<Collectable> type, float x, float y, Object extra) {
        CollectableType collectableType = (CollectableType) extra;
        switch (collectableType) {        
            case POWERUP:
                return createPower(x, y);
            
            default:
                throw new IllegalArgumentException("Unknown collectable type: " + type);
        }
    }
    
    private Collectable createPower(float x, float y) {
        Collectable collectable = new Collectable(x, y, 0f, 0f, collisionManager);
        collectable.setSprite(new Sprite(new Texture(Gdx.files.internal("power.jpg")), 40f));
        return collectable;
    }

}
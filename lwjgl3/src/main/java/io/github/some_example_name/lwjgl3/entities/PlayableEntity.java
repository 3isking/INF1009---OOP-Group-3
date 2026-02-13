package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.movement.MovementManager;
import io.github.some_example_name.lwjgl3.movement.MovementStrategy;

public class PlayableEntity
 extends Entity implements iMovable, iCollidable {
    
    private MovementStrategy movementStrategy;
    private boolean hasCollided = false;
    private long lastCollisionTime = 0;

    public PlayableEntity(MovementManager movementManager){
        super();
    	this.id = "player_1";
        this.movementStrategy = movementManager.getPlayerMovement();
        
    }

    @Override
    public void render(SpriteBatch batch){
        // batch.begin();
        batch.draw(this.getSprite().getTexture(), this.getPosition().x, this.getPosition().y, this.getSprite().getWidth(), this.getSprite().getHeight());
        // batch.end();
    }

    @Override
    public void update(float deltaTime){
        
    }

    @Override
    public Vector2 getVelocity() {
        return this.velocity;
    }

    @Override
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    @Override
    public MovementStrategy getMovementStrategy() {
        return this.movementStrategy;
    }

    @Override
    public Rectangle getCollisionBounds() {
        return new Rectangle(
            this.getPosition().x,
            this.getPosition().y,
            this.getSprite().getWidth(),
            this.getSprite().getHeight()
        );
    }    
    
    // Output Manager
    public boolean wasHit() {
        return hasCollided;
    }

    public void resetHitFlag() {
        hasCollided = false;
    }
}

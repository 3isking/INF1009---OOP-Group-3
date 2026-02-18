package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.collision.CollisionManager;
import io.github.some_example_name.lwjgl3.collision.CollisionResolver;
import io.github.some_example_name.lwjgl3.movement.MovementManager;
import io.github.some_example_name.lwjgl3.movement.MovementStrategy;

public class PlayableEntity extends Entity implements iMovable, iCollidable {
    
    private MovementStrategy movementStrategy;
    private CollisionResolver resolver;
    
    private boolean wasTouchingWallLastFrame = false;
    private boolean isTouchingWallThisFrame = false;
    private boolean playSoundSignal = false;

    public PlayableEntity(MovementManager movementManager, CollisionManager collisionManager){
        super();
    	this.id = "player_1";
        this.movementStrategy = movementManager.getPlayerMovement();
        this.resolver = collisionManager.getResolver();
    }

    @Override
    public void render(SpriteBatch batch){
        // batch.begin();
        batch.draw(this.getSprite().getTexture(), this.getPosition().x, this.getPosition().y, this.getSprite().getWidth(), this.getSprite().getHeight());
        // batch.end();
    }

    @Override
    public void update(float deltaTime){
        wasTouchingWallLastFrame = isTouchingWallThisFrame;
        isTouchingWallThisFrame = false;
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
    
    
    public void resetCollisionState() {
        // "Archive" the current state to history
        wasTouchingWallLastFrame = isTouchingWallThisFrame;
        
        // Reset current state to false (innocent until proven guilty)
        isTouchingWallThisFrame = false;
        
        // Reset sound signal
        playSoundSignal = false; 
    }
    
    // Output Manager
    public boolean wasHit() {
        return playSoundSignal;
    }

    public void resetHitFlag() {
        playSoundSignal = false;
    }
    
    // Collision Manager
    public void collide(iCollidable other)
    {
        other.collideWithPlayer(this);
    }

    @Override
    public void collideWithWall(Wall wall)
    {
        resolver.resolveCollisions(this, wall);
    }

    @Override
    public void collideWithAI(AiEntity ai) {
        resolver.resolveCollisions(this, ai);
    }

    @Override
    public void collideWithPlayer(PlayableEntity player)
    {
        
    }
}


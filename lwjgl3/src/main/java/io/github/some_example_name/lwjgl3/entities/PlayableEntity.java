package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.collision.CollisionResolver;
import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.movement.MovementStrategy;
import io.github.some_example_name.lwjgl3.movement.iMovementManager;

public class PlayableEntity extends Entity implements iMovable, iCollidable {
    
    private MovementStrategy movementStrategy;
    private CollisionResolver resolver;
    
    private boolean wasTouchingObstacleLastFrame = false;
    private boolean isTouchingObstacleThisFrame = false;

    private int health = 5;

    // --- Power-up fields ---
    private int powerUps = 0;
    private boolean powerActive = false;
    private float powerDuration = 5f; 
    private float powerTimer = 0f;

    public PlayableEntity(float x, float y, iMovementManager movementManager, iCollisionManager collisionManager){
        super();
    	this.id = "player";
        this.setPosition(new Vector2(x, y));
        this.setSprite(new Sprite(new Texture(Gdx.files.internal("plane.png")),30)); //2:1 ratio for plane sprite
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
        wasTouchingObstacleLastFrame = isTouchingObstacleThisFrame;
        isTouchingObstacleThisFrame = false;

        // Power up timer
        if (powerActive) {
            powerTimer += deltaTime;
            if (powerTimer >= powerDuration) {
                powerActive = false;    // deactivate multiplier
                powerTimer = 0f;        // reset timer
            }
        }
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
        return new Rectangle(this.getPosition().x, this.getPosition().y, this.getSprite().getWidth(), this.getSprite().getHeight());
    }  
    
    // Output Manager
    public void resetCollisionState() {
        wasTouchingObstacleLastFrame = isTouchingObstacleThisFrame;
        
        isTouchingObstacleThisFrame = false;
    }
    
    public boolean isNewObstacleCollision() {
        isTouchingObstacleThisFrame = true;
        return !wasTouchingObstacleLastFrame;
    }
    
    // Collision Manager
    public void collide(iCollidable other)
    {
        other.collideWithPlayer(this);
    }

    @Override
    public void collideWithObstacle(Obstacle obstacle)
    {
        resolver.resolveCollisions(this, obstacle);
    }

    @Override
    public void collideWithAI(AiEntity ai) {
        resolver.resolveCollisions(this, ai);
    }

    @Override
    public void collideWithPlayer(PlayableEntity player)
    {
        // player-player collisions can be ignored or handled here
    }

    @Override
    public void collideWithCollectable(Collectable collectable) {
        
    }

    public int getHealth(){
        return health;
    }

    public void takeDamage(int damage){
        health -= damage;
        if (health < 0){
            health = 0;
        }
    }

    // --- Power-up methods ---
    public void addPowerUp() {
        powerUps++;
    }

    public boolean canUsePowerUp() {
        return powerUps > 0 && !powerActive;
    }

    public void usePowerUp() {
        if (canUsePowerUp()) {
            powerUps--;
            powerActive = true;
            powerTimer = 0f; 
        }
    }

    public void deactivatePowerUp() {
        powerActive = false;
    }

    public boolean isPowerActive() {
        return powerActive;
    }

    public boolean isPowerUpsAvailable() {
        return powerUps > 0;
    }
}


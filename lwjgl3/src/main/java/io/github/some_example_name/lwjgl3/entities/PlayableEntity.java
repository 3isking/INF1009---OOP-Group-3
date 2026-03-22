package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
    private boolean isTouchingObstacleThisFrame  = false;

    private int health = 5;

    // --- Power-up fields ---
    private int     powerUps    = 0;
    private boolean powerActive = false;

    // --- Invincibility / blink fields ---
    private float invincibilityTimer              = 0f;
    private static final float INVINCIBILITY_DURATION = 1f;
    private static final float BLINK_INTERVAL         = 0.1f;
    private float blinkTimer                     = 0f;

    public PlayableEntity(float x, float y, iMovementManager movementManager, iCollisionManager collisionManager) {
        super();
        this.id = "player";
        this.setPosition(new Vector2(x, y));
        this.setSprite(new Sprite(new Texture(Gdx.files.internal("plane.png")), 30));
        this.movementStrategy = movementManager.getPlayerMovement();
        this.resolver         = collisionManager.getResolver();
    }

    // -------------------------------------------------------------------------
    // Render — applies sprite alpha so blink is actually visible
    // -------------------------------------------------------------------------

    @Override
    public void render(SpriteBatch batch) {
        Color c = getSprite().getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        batch.draw(getSprite().getTexture(),
                getPosition().x, getPosition().y,
                getSprite().getWidth(), getSprite().getHeight());
        batch.setColor(1f, 1f, 1f, 1f); // reset so other entities are unaffected
    }

    // -------------------------------------------------------------------------
    // Update
    // -------------------------------------------------------------------------

    @Override
    public void update(float deltaTime) {
        wasTouchingObstacleLastFrame = isTouchingObstacleThisFrame;
        isTouchingObstacleThisFrame  = false;

        // Invincibility + blink timer
        updateInvincibility(deltaTime);
    }

    // -------------------------------------------------------------------------
    // Invincibility
    // -------------------------------------------------------------------------

    public void triggerInvincibility() {
        invincibilityTimer = INVINCIBILITY_DURATION;
        blinkTimer         = 0f;
    }

    public boolean isInvincible() {
        return invincibilityTimer > 0f;
    }

    private void updateInvincibility(float deltaTime) {
        if (invincibilityTimer <= 0f) return;

        invincibilityTimer -= deltaTime;
        blinkTimer         += deltaTime;

        // Toggle alpha every BLINK_INTERVAL seconds
        if (blinkTimer >= BLINK_INTERVAL) {
            blinkTimer = 0f;
            float currentAlpha = getSprite().getColor().a;
            getSprite().setAlpha(currentAlpha < 0.5f ? 1f : 0.3f);
        }

        // Timer expired — fully restore opacity
        if (invincibilityTimer <= 0f) {
            invincibilityTimer = 0f;
            blinkTimer         = 0f;
            getSprite().setAlpha(1f);
        }
    }

    // -------------------------------------------------------------------------
    // Movement
    // -------------------------------------------------------------------------

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

    // -------------------------------------------------------------------------
    // Collision
    // -------------------------------------------------------------------------

    @Override
    public Rectangle getCollisionBounds() {
        return new Rectangle(getPosition().x, getPosition().y,
                getSprite().getWidth(), getSprite().getHeight());
    }

    public void resetCollisionState() {
        wasTouchingObstacleLastFrame = isTouchingObstacleThisFrame;
        isTouchingObstacleThisFrame  = false;
    }

    public boolean isNewObstacleCollision() {
        isTouchingObstacleThisFrame = true;
        return !wasTouchingObstacleLastFrame;
    }

    public void collide(iCollidable other) {
        other.collideWithPlayer(this);
    }

    @Override
    public void collideWithObstacle(Obstacle obstacle) {
        // Guard here as well — CollisionManager calls this path via the generic
        // resolveCollisions(iCollidable, iCollidable) overload, which would bypass
        // the invincibility check in CollisionResolver and apply damage a second time.
        if (isInvincible()) return;
        resolver.resolveCollisions(this, obstacle);
    }

    @Override
    public void collideWithPlayer(PlayableEntity player) {
        // player-player collisions ignored
    }

    @Override
    public void collideWithCollectable(Collectable collectable) {
        resolver.resolveCollisions(this, collectable);
    }

    @Override
    public void collideWithAnswer(Answer answer) {
        resolver.resolveCollisions(this, answer);
    }

    // -------------------------------------------------------------------------
    // Health
    // -------------------------------------------------------------------------

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
        
    }

    // -------------------------------------------------------------------------
    // Power-up
    // -------------------------------------------------------------------------

    // Called when player picks up a multiplier collectable
    public void addPowerUp() {
        // accumulate every multiplier picked up
        powerUps++;
    }

    // Returns true when there is at least one multiplier
    public boolean canUsePowerUp() {
        return powerUps > 0 && !powerActive;
    }

    // Activate the multiplier immediately
    public void usePowerUp() {
        if (canUsePowerUp()) {
            powerUps--;
            powerActive = true;
        }
    }

    // Stored power-up count increments
    // Player can power-up multiple times
    public int getPowerUpCount() {
        return powerUps;
    }

    public boolean isPowerActive() {
        return powerActive;
    }

    public boolean hasPowerUpCollected() {
        return powerUps > 0;
    }

    public void consumePowerUpCharge() {
        if (powerUps > 0) {
            powerUps--;
        }
    }

    public void deactivatePowerUp() {
        powerActive = false;
    }
}
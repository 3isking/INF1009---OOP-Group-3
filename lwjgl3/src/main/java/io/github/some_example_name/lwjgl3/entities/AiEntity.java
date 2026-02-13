package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.collision.CollisionResolver;
import io.github.some_example_name.lwjgl3.movement.AIMovement;
import io.github.some_example_name.lwjgl3.movement.MovementStrategy;

public class AiEntity
 extends Entity implements iMovable, iCollidable {
    
    private MovementStrategy movementStrategy;

    public AiEntity(){
        super();
    	this.id = "ai_1";
        this.movementStrategy = new AIMovement();
        
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

    // Collision Manager
    @Override
    public void collide(iCollidable other) {
        other.collideWithAI(this); // second dispatch
    }

    @Override
    public void collideWithWall(Wall wall) {
        CollisionResolver resolver = new CollisionResolver();
        resolver.resolveCollisions(this, wall);
    }

    @Override
    public void collideWithPlayer(PlayableEntity player) {
        CollisionResolver resolver = new CollisionResolver();
        resolver.resolveCollisions(player, this);
    }

    @Override
    public void collideWithAI(AiEntity ai) {

    }
}

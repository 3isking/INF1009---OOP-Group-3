package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.collision.CollisionResolver;
import io.github.some_example_name.lwjgl3.collision.iCollisionManager;

public class Answer extends Entity implements iCollidable{
    private CollisionResolver resolver;
    private final String text;
    private final boolean isCorrect;
    private boolean wasHit = false;

    public Answer(float x, float y, float width, float height, iCollisionManager collisionManager, String text, boolean isCorrect) {
        super();
    	this.id = "answer";
        this.setPosition(new Vector2(x, y));
        this.resolver = collisionManager.getResolver();
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
    
    public void setWasHit() {
        this.wasHit = true;
    }

    public boolean wasHit() {
        return wasHit;
    }

    public void resetWasHit() {
        this.wasHit = false;
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
    public Rectangle getCollisionBounds() {
        return new Rectangle(this.getPosition().x, this.getPosition().y, this.getSprite().getWidth(), this.getSprite().getHeight());
    }

    @Override
    public void collide(iCollidable other) {
        other.collideWithAnswer(this);
    }

    @Override
    public void collideWithObstacle(Obstacle obstacle) {
        
    }

    @Override
    public void collideWithPlayer(PlayableEntity player) {
        resolver.resolveCollisions(player, this);
    }

    @Override
    public void collideWithCollectable(Collectable collectable) {

    }

    @Override
    public void collideWithAnswer(Answer answer) {
        
    }
}

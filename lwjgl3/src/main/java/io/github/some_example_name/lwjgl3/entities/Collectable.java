package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.collision.CollisionResolver;

public class Collectable extends Entity implements iCollidable {
    private CollisionResolver resolver;
    private boolean collected = false;

    public Collectable(float x, float y, float width, float height, iCollisionManager collisionManager) {
        this.setPosition(new Vector2(x, y));
        this.setVisible(true);
        this.setRotation(0);
        this.setId("collectable");
        this.resolver = collisionManager.getResolver();

    }

    // -------------------------------------------------------------------------
    // Power-up
    // -------------------------------------------------------------------------
    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
        this.setVisible(!collected);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (this.isVisible()) {
            batch.draw(this.getSprite().getTexture(), this.getPosition().x, this.getPosition().y, this.getSprite().getWidth(), this.getSprite().getHeight());
        }
    }

    @Override

    public void update(float deltaTime) {

    }

    @Override
    public Rectangle getCollisionBounds() {
        return new Rectangle(this.getPosition().x, this.getPosition().y, this.getSprite().getWidth(), this.getSprite().getHeight());
    }

    @Override
    public void collide(iCollidable other) {
        other.collideWithCollectable(this);
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
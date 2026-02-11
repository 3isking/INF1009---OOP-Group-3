package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PlayableEntity
 extends Entity implements iMovable, iCollidable {
    
    private boolean hasCollided = false;
    private long lastCollisionTime = 0;

    public PlayableEntity(){
        super();
    	this.id = "player_1";
        
        this.setSprite(new Sprite(new Texture(Gdx.files.internal("Player.png")), 50, 50));
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

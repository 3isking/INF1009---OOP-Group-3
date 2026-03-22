package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.collision.CollisionResolver;
import io.github.some_example_name.lwjgl3.collision.iCollisionManager;

public class Obstacle extends Entity implements iCollidable {
    private CollisionResolver resolver;
    private boolean hasHitPlayer = false;

    // public Obstacle(){
    //     this.setPosition(new Vector2(300, 0));
    //     this.setVisible(true);
    //     this.setLayer(1);
    //     this.setRotation(0);
    //     this.setId("0");
    //     this.setSprite(new Sprite(new Texture(Gdx.files.internal("obstacle.png")), 100, 100));
    // }
//added by chavonne - accepts parameters for more flexible positioning
    public Obstacle(float x, float y, float width, float height, iCollisionManager collisionManager){
        super();
    	this.id = "obstacle";
        this.setPosition(new Vector2(x, y));
        this.setSprite(new Sprite(new Texture(Gdx.files.internal("obstacle.png")), width, height));
        this.resolver = collisionManager.getResolver();
    }

    public boolean getHitPlayer(){
        return hasHitPlayer;
    }

    public void setHitPlayer(){
        hasHitPlayer = true;
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

    // Collision Manager
    public void collide(iCollidable other)
    {
        other.collideWithObstacle(this);
    }

    @Override
    public void collideWithObstacle(Obstacle obstacle)
    {
        
    }

    @Override
    public void collideWithPlayer(PlayableEntity player)
    {
        resolver.resolveCollisions(player, this);
    }

    @Override
    public void collideWithCollectable(Collectable collectable) {

    }

    @Override
    public void collideWithAnswer(Answer answer) {
        
    }

}
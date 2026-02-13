package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.collision.CollisionResolver;

public class Wall extends Entity implements iCollidable {

    // public Wall(){
    //     this.setPosition(new Vector2(300, 0));
    //     this.setVisible(true);
    //     this.setLayer(1);
    //     this.setRotation(0);
    //     this.setId("0");
    //     this.setSprite(new Sprite(new Texture(Gdx.files.internal("wall.png")), 100, 100));
    // }
//added by chavonne - accepts parameters for more flexible positioning
    public Wall(float x, float y, float width, float height){
        this.setPosition(new Vector2(x, y));
        this.setVisible(true);
        this.setLayer(1);
        this.setRotation(0);
        this.setId("0");
        this.setSprite(new Sprite(new Texture(Gdx.files.internal("wall.png")), width, height));
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
        return new Rectangle(
            this.getPosition().x,
            this.getPosition().y,
            this.getSprite().getWidth(),
            this.getSprite().getHeight()
        );
    }

    // Collision Manager
    public void collide(iCollidable other)
    {
        other.collideWithWall(this);
    }

    @Override
    public void collideWithWall(Wall wall)
    {
        
    }

    @Override
    public void collideWithPlayer(PlayableEntity player)
    {
        CollisionResolver resolver = new CollisionResolver();
        resolver.resolveCollisions(player, this);
    }

    @Override
    public void collideWithAI(AiEntity ai) 
    {
        CollisionResolver resolver = new CollisionResolver();
        resolver.resolveCollisions(ai, this);
    }

}
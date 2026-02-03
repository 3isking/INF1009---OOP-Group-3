package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Wall extends Entity implements iCollidable {

    public Wall(){
        this.setPosition(new Vector2(300, 0));
        this.setVisible(true);
        this.setLayer(1);
        this.setRotation(0);
        this.setId("0");
        this.setSprite(new Sprite(new Texture(Gdx.files.internal("wall.png")), 100, 100));
    }

    @Override
    public void render(SpriteBatch batch){
        batch.begin();
        batch.draw(this.getSprite().getTexture(), this.getPosition().x, this.getPosition().y, this.getSprite().getWidth(), this.getSprite().getHeight());
        batch.end();
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

    @Override
    public void onCollision(iCollidable other) {
        System.out.println("Wall collided with something!");
    }
}
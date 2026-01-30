package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public final class Player extends Entity implements iMovable {
    Texture temp = new Texture(Gdx.files.internal("player.png"));

    public Player(){
        this.setPosition(new Vector2(0, 0));
        this.setVelocity(new Vector2(3, 3));
        this.setVisible(true);
        this.setLayer(1);
        this.setRotation(0);
        this.setId("0");
        this.setSprite(new Sprite(new Texture(Gdx.files.internal("player.png")), 50, 50));
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
    public void move(){
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.setVelocity(new Vector2(-3, 0));
            System.out.println(this.getVelocity());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.setVelocity(new Vector2(3, 0));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.setVelocity(new Vector2(0, -3));			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.setVelocity(new Vector2(0, 3));				
		}

		this.setPosition(this.getPosition().add(this.getVelocity()));
        this.setVelocity(new Vector2(0, 0));
    }

    @Override
    public Vector2 getVelocity() {
        return this.velocity;
    }

    @Override
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
}

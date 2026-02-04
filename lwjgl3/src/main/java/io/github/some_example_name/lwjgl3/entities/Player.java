package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.managers.InputManager;

public final class Player extends Entity implements iMovable, iCollidable {
    
    private final InputManager inputManager;
    private boolean hasCollided = false;
    private long lastCollisionTime = 0;

    public Player(InputManager inputManager){
    	this.inputManager = inputManager;
    	 
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
        // batch.begin();
        batch.draw(this.getSprite().getTexture(), this.getPosition().x, this.getPosition().y, this.getSprite().getWidth(), this.getSprite().getHeight());
        // batch.end();
    }

    @Override
    public void update(float deltaTime){
        
    }
    
    @Override
    public void move(){
    	if (inputManager.inputHeld("left")) {
    		this.setVelocity(new Vector2(-3, 0));
    	}
    	if (inputManager.inputHeld("right")) {
    		this.setVelocity(new Vector2(3, 0));
    	}
    	if (inputManager.inputHeld("down")) {
    		this.setVelocity(new Vector2(0, -3));
    	}
    	if (inputManager.inputHeld("up")) {
    		this.setVelocity(new Vector2(0, 3));
    	}
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            this.setVelocity(new Vector2(-3, 0));
//            System.out.println(this.getVelocity());
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            this.setVelocity(new Vector2(3, 0));
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            this.setVelocity(new Vector2(0, -3));			
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            this.setVelocity(new Vector2(0, 3));				
//		}

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

        if (other instanceof Wall) {
            Rectangle player = this.getCollisionBounds();
            Rectangle wall = other.getCollisionBounds();
            
            long currentTime = System.currentTimeMillis();
            
            // Only flag for sound if enough time has passed
            if (currentTime - lastCollisionTime > 500) {
                this.hasCollided = true;
                this.lastCollisionTime = currentTime; // Reset timer
            }

            float overlapLeft = (player.x + player.width) - wall.x;
            float overlapRight = (wall.x + wall.width) - player.x;
            float overlapBottom = (player.y + player.height) - wall.y;
            float overlapTop = (wall.y + wall.height) - player.y;

            float minX = Math.min(overlapLeft, overlapRight);
            float minY = Math.min(overlapBottom, overlapTop);
            
            float pushBuffer = 5.0f;

            // Resolve along the smallest overlap axis
            if (minX < minY) {
                // Horizontal collision
                if (overlapLeft < overlapRight) {
                    // Player hit wall from left
                    this.getPosition().x -= (overlapLeft + pushBuffer);
                } else {
                    // Player hit wall from right
                    this.getPosition().x += (overlapRight + pushBuffer);
                }
            } else {
                // Vertical collision
                if (overlapBottom < overlapTop) {
                    // Player hit wall from below
                    this.getPosition().y -= (overlapBottom + pushBuffer);
                } else {
                    // Player hit wall from above
                    this.getPosition().y += (overlapTop + pushBuffer);
                }
            }
        }
    }
    
    
    // Output Manager
    public boolean wasHit() {
        return hasCollided;
    }

    public void resetHitFlag() {
        hasCollided = false;
    }
}

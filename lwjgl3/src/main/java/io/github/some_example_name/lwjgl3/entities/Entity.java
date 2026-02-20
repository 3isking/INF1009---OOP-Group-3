package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    protected String id;
    protected Vector2 position;
    protected Vector2 velocity;
    protected Sprite sprite = null;
    protected float rotation;
    protected boolean visible;
    protected Rectangle collisionBox;

    public Entity() {
        this.position = new Vector2();
        this.velocity = new Vector2(0, 0);
        this.visible = true;
        this.rotation = 0f;
        this.id = "0";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
    
    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public abstract void update(float deltaTime);

    public void render(SpriteBatch batch) {};
}
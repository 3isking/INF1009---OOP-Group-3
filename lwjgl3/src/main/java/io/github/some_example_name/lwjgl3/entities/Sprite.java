package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.graphics.Texture;

public class Sprite {
    private Texture texture;
    private float width;
    private float height;

    public Sprite() {
        this.width = 0f;
        this.height = 0f;
    }

    public Sprite(Texture texture) {
        this.texture = texture;
        if (texture != null) {
            this.width = texture.getWidth();
            this.height = texture.getHeight();
        }
    }

    public Sprite(Texture texture, float width, float height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    public String getTexturePath() {
        if (texture != null) {
            return texture.toString();
        }
        return null;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        if (texture != null && width == 0 && height == 0) {
            this.width = texture.getWidth();
            this.height = texture.getHeight();
        }
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
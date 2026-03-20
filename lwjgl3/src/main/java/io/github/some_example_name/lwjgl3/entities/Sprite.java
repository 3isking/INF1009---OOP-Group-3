package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Sprite {
    private Texture texture;
    private float width;
    private float height;
    private Color color = new Color(1f, 1f, 1f, 1f); // default: white, fully opaque

    public Sprite() {
        this.width  = 0f;
        this.height = 0f;
    }

    public Sprite(Texture texture) {
        this.texture = texture;
        if (texture != null) {
            this.width  = texture.getWidth();
            this.height = texture.getHeight();
        }
    }

    public Sprite(Texture texture, float height) {
        this.texture = texture;
        this.height  = height;
        this.width   = (texture.getWidth() / (float) texture.getHeight()) * height;
    }

    public Sprite(Texture texture, float width, float height) {
        this.texture = texture;
        this.width   = width;
        this.height  = height;
    }

    // -------------------------------------------------------------------------
    // Color / alpha
    // -------------------------------------------------------------------------

    /** Returns the current color (r, g, b, a). */
    public Color getColor() {
        return color;
    }

    /** Sets the alpha (transparency) of the sprite. 1f = fully opaque, 0f = invisible. */
    public void setAlpha(float alpha) {
        color.a = alpha;
    }

    /** Sets the full color including alpha. */
    public void setColor(Color color) {
        this.color.set(color);
    }

    /** Sets the full color by components. */
    public void setColor(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
    }

    // -------------------------------------------------------------------------
    // Existing getters / setters
    // -------------------------------------------------------------------------

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
            this.width  = texture.getWidth();
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
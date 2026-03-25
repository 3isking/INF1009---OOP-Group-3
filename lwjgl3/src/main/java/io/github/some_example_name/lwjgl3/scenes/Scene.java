package io.github.some_example_name.lwjgl3.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public abstract class Scene {
    protected String name;
    protected String background;

    public Scene(String name) {
        this.name = name;
    }

    public Scene(String name, String background) {
        this.name = name;
        this.background = background;
    }

    public abstract void onLoad();
    
    public abstract void onUnload();
    
    public abstract void onExit();
    
    public abstract void onEnter();

    public abstract void update(float deltaTime);
    
    public abstract void render(SpriteBatch batch);
    
    public abstract void renderUI(SpriteBatch batch);

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
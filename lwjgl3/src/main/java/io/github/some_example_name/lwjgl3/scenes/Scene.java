package io.github.some_example_name.lwjgl3.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.some_example_name.lwjgl3.entities.Entity;


public abstract class Scene {
    protected String name;
    protected String background;
    protected List<Entity> entityList;

    public Scene(String name) {
        this.name = name;
        this.entityList = new ArrayList<>();
    }

    public Scene(String name, String background) {
        this.name = name;
        this.background = background;
        this.entityList = new ArrayList<>();
    }

    public abstract void onLoad();
    
    public abstract void onUnload();
    
    public abstract void onExit();
    
    public abstract void onEnter();

    public abstract void update(float deltaTime);
    
    public abstract void render(SpriteBatch batch);

    public List<Entity> getEntityList() {
        return entityList;
    }
    
    public void addEntity(Entity entity) {
        if (entity != null && !entityList.contains(entity)) {
            entityList.add(entity);
        }
    }
    
    public void removeEntity(Entity entity) {
        entityList.remove(entity);
    }

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
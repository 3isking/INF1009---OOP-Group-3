package io.github.some_example_name.lwjgl3.entities;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;

public class Answer extends Obstacle{
    private final String text;
    private final boolean isCorrect;
    private boolean wasHit = false;

    public Answer(float x, float y, float width, float height, iCollisionManager collisionManager,
                          String text, boolean isCorrect) {
        super(x, y, width, height, collisionManager); // <- matches Obstacle constructor
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
    
    public void setWasHit() {
        this.wasHit = true;
    }

    public boolean wasHit() {
        return wasHit;
    }

    public void resetWasHit() {
        this.wasHit = false;
    }

    @Override
    public void collideWithPlayer(PlayableEntity player) {
        player.collideWithAnswer(this);
    }
}

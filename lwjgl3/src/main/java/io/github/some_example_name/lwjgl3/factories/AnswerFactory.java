package io.github.some_example_name.lwjgl3.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.Answer;
import io.github.some_example_name.lwjgl3.entities.Obstacle;
import io.github.some_example_name.lwjgl3.entities.Sprite;

public class AnswerFactory {

    public enum AnswerLane {
        TOP,
        MIDDLE,
        BOTTOM
    }

    private final iCollisionManager collisionManager;
    private final Texture bubbleTexture;
    
    public AnswerFactory(iCollisionManager collisionManager) {
        this.collisionManager = collisionManager;
        this.bubbleTexture = new Texture(Gdx.files.internal("speech.png"));
    }

    /**
     * Creates an AnswerObstacle in the specified lane with text and correctness
     */
    public Answer create(float x, float y, String text, boolean isCorrect) {

        float width = 80f;
        float height = 80f;

        Answer answer = new Answer(x, y, width, height, collisionManager, text, isCorrect);
        answer.setSprite(new Sprite(bubbleTexture, width));
        collisionManager.addCollidableEntity(answer); 
        return answer;
    }
    
}

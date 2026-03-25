package io.github.some_example_name.lwjgl3.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.Answer;
import io.github.some_example_name.lwjgl3.entities.Sprite;

public class AnswerFactory implements EntityFactory<Answer>{


    public static class AnswerData {
        public final String text;
        public final boolean isCorrect;

        public AnswerData(String text, boolean isCorrect) {
            this.text = text;
            this.isCorrect = isCorrect;
        }
    }

    private final iCollisionManager collisionManager;
    private final Texture bubbleTexture;
    
    public AnswerFactory(iCollisionManager collisionManager) {
        this.collisionManager = collisionManager;
        this.bubbleTexture = new Texture(Gdx.files.internal("speech.png"));
    }

    @Override
    public Answer createEntity(Class<Answer> type, float x, float y, Object extra) {
        AnswerData data = (AnswerData) extra;
        float width = 90f;
        float height = 90f;

        Answer answer = new Answer(x, y, width, height, collisionManager, data.text, data.isCorrect);
        answer.setSprite(new Sprite(bubbleTexture, width));
        return answer;
    }
    
}

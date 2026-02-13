package io.github.some_example_name.lwjgl3.entities;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface iCollidable {
    Rectangle getCollisionBounds();
    Vector2 getPosition();
    void setPosition(Vector2 position);
    void collide(iCollidable other);
    void collideWithPlayer(PlayableEntity player);
    void collideWithWall(Wall wall);
    void collideWithAI(AiEntity ai);
}

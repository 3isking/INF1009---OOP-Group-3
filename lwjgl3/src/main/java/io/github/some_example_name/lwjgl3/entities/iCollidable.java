package io.github.some_example_name.lwjgl3.entities;
import com.badlogic.gdx.math.Rectangle;


/*public interface iCollidable {
	public BoundingBox getBoundingBox();

	public void setBoundingBox(BoundingBox boudingBox);
	
	public void onCollision(iCollidable other);
	
}*/

public interface iCollidable {
    Rectangle getCollisionBounds();
    void onCollision(iCollidable other);
}

package io.github.some_example_name.lwjgl3.entities;

public interface iCollidable {
	public BoundingBox getBoundingBox();

	public void setBoundingBox(BoundingBox boudingBox);
	
	public void onCollision(iCollidable other);
	
}

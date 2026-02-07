package io.github.some_example_name.lwjgl3.collision;

import io.github.some_example_name.lwjgl3.entities.iCollidable;

public class CollisionDetector {
    public boolean checkCollisions(iCollidable entity1, iCollidable entity2)
    {
        if (entity1.getCollisionBounds().overlaps(entity2.getCollisionBounds())) 
            {
                return true;
            }
        else
            {
                return false;
            }
    }
}
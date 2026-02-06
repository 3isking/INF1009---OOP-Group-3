package io.github.some_example_name.lwjgl3.collision;

import io.github.some_example_name.lwjgl3.entities.Player;
import io.github.some_example_name.lwjgl3.entities.Wall;
import io.github.some_example_name.lwjgl3.entities.iCollidable;

public class CollisionResolver {
    public void resolveCollisions(iCollidable entity1, iCollidable entity2)
    {
        if (entity1 instanceof Player && entity2 instanceof Wall || entity2 instanceof Player && entity1 instanceof Wall)
        {

        }
    }
}
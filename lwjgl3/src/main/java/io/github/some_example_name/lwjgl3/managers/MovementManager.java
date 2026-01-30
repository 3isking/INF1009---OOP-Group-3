package io.github.some_example_name.lwjgl3.managers;

import java.util.List;

import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.iMovable;



public class MovementManager {
    public void moveEntities(List<Entity> entityList){
        for (Entity entity: entityList){
            if (entity instanceof iMovable){
                ((iMovable)entity).move();
            }
        }

    }
}

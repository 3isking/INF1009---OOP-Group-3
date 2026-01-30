package io.github.some_example_name.lwjgl3.entities;

import com.badlogic.gdx.math.Vector2;

public class Camera extends Entity implements iMovable {
    @Override
    public Vector2 getVelocity(){
        return new Vector2(0,1);
    }

    @Override
    public void setVelocity(Vector2 velocity){
        
    };

    @Override
    public void move(){
        
    };
}

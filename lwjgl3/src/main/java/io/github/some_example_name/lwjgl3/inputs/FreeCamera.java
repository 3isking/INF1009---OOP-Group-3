package io.github.some_example_name.lwjgl3.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import io.github.some_example_name.lwjgl3.entities.Entity;

public class FreeCamera implements Camera{
	private InputManager inputManager;
    private OrthographicCamera camera;
    private float speed = 500; 

    public FreeCamera(InputManager inputManager, OrthographicCamera camera) {
    	this.inputManager = inputManager;
    	this.camera = camera;
    }

    @Override
    public void control(Entity player) {
        float dt = Gdx.graphics.getDeltaTime();
        if (inputManager.inputHeld("camUp")){
        	camera.position.y += speed * dt;
        }
        if (inputManager.inputHeld("camDown")){
        	camera.position.y -= speed * dt;
        }
        if (inputManager.inputHeld("camLeft")){
        	camera.position.x -= speed * dt;
        }
        if (inputManager.inputHeld("camRight")){
        	camera.position.x += speed * dt;
        }
    }

    @Override
    public void update() {
        camera.update();
    }

    @Override
    public OrthographicCamera getCamera() {
        return camera;
    }
    
    @Override
    public Matrix4 getCombinedMatrix(){
        return camera.combined;
    }
}

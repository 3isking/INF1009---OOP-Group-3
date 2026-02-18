package io.github.some_example_name.lwjgl3.inputs;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import io.github.some_example_name.lwjgl3.entities.Entity;

public class DefaultCamera implements Camera{

	private InputManager inputManager;
    private OrthographicCamera camera;

    public DefaultCamera(InputManager inputManager, OrthographicCamera camera) {
    	this.inputManager = inputManager;
    	this.camera = camera;
    }

    @Override
    public void control(Entity player) {
    	camera.position.set(new Vector3());
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

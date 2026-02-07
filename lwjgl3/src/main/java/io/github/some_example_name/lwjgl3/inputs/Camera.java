package io.github.some_example_name.lwjgl3.inputs;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import io.github.some_example_name.lwjgl3.entities.Entity;

public class Camera {
    private OrthographicCamera camera;
    
    public Camera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void cameraControl(Entity player){
		Vector3 target = new Vector3(
            player.getPosition().x + player.getSprite().getWidth() / 2f,
            player.getPosition().y + player.getSprite().getHeight() / 2f,
            0
        );

        camera.position.lerp(target, 0.1f);
    }
}

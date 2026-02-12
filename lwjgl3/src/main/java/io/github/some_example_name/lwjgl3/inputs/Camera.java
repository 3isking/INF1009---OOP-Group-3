package io.github.some_example_name.lwjgl3.inputs;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import io.github.some_example_name.lwjgl3.entities.Entity;

public class Camera {
    private OrthographicCamera camera;
    
    public Camera() {
        this.camera = new OrthographicCamera(640, 480);
        this.camera.position.set(320, 240, 0);
        this.camera.update();
    }

    public Matrix4 getCombinedMatrix (){
        return camera.combined;
    }

    public void cameraControl(Entity player){
		Vector3 target = new Vector3(
            player.getPosition().x + player.getSprite().getWidth() / 2f,
            player.getPosition().y + player.getSprite().getHeight() / 2f,
            0
        );

        camera.position.set(target);  // Instant!
        camera.update();
    }
}

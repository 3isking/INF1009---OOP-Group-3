package io.github.some_example_name.lwjgl3.inputs;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.lwjgl3.entities.Entity;

public class PlayerCamera implements Camera {
    private OrthographicCamera camera;

    public PlayerCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void control(Entity player) {
        if (player == null) {
        	return;
        }

        Vector3 target = new Vector3(
            player.getPosition().x + player.getSprite().getWidth() / 2f,
            player.getPosition().y + player.getSprite().getHeight() / 2f,
            0
        );
        camera.position.set(target);
//        camera.position.lerp(target, 0.1f); // smooth follow
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
    public Matrix4 getCombinedMatrix (){
      return camera.combined;
    }
}

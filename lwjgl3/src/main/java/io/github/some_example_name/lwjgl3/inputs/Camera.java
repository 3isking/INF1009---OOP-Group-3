package io.github.some_example_name.lwjgl3.inputs;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import io.github.some_example_name.lwjgl3.entities.Entity;

public interface Camera {
	void update();
    void control(Entity player);
    OrthographicCamera getCamera();
    Matrix4 getCombinedMatrix();
}

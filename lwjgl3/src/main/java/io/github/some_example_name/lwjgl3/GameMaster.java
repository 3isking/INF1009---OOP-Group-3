package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.some_example_name.lwjgl3.entities.Player;
import io.github.some_example_name.lwjgl3.managers.CollisionManager;
import io.github.some_example_name.lwjgl3.managers.EntityManager;
import io.github.some_example_name.lwjgl3.managers.InputManager;
import io.github.some_example_name.lwjgl3.managers.MovementManager;
import io.github.some_example_name.lwjgl3.managers.OutputManager;
import io.github.some_example_name.lwjgl3.managers.SceneManager;

public class GameMaster extends ApplicationAdapter{
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private SceneManager sceneManager;
    private EntityManager entityManager;
    private MovementManager movementManager;
    private CollisionManager collisionManager;
    private InputManager inputManager;
    private OutputManager outputManager;

    public void create(){

        // Setup Camera
		camera = new OrthographicCamera(640, 480);
		camera.position.set(320, 240, 0);
		camera.update();
        batch = new SpriteBatch();

        
        // Setup Managers
        entityManager = new EntityManager();
        movementManager = new MovementManager();

        // Setup Player
        Player player = new Player();
        entityManager.addEntity(player);
    }

    public void render(){
        cameraControl();
        camera.update();
		batch.setProjectionMatrix(camera.combined);
        
		ScreenUtils.clear(0, 0, 0.2f, 1);

        movementManager.moveEntities(entityManager.getAllEntities());
        entityManager.render(batch);
    }

    public void dispose(){

    }

    public void cameraControl(){
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0, 3, 0);
		}
    }
}

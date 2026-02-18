package io.github.some_example_name.lwjgl3.inputs;

import java.util.Set;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.inputs.Inputs.Device;


public class InputManager extends InputAdapter{
    private final Inputs inputs;
    private Camera camera;
    private void defaultBinding() {
    	inputs.bind("up", Device.KEYBOARD, Input.Keys.UP);
		inputs.bind("down", Device.KEYBOARD, Input.Keys.DOWN);
		inputs.bind("left", Device.KEYBOARD, Input.Keys.LEFT);
		inputs.bind("right", Device.KEYBOARD, Input.Keys.RIGHT);
		inputs.bind("jump", Device.KEYBOARD, Input.Keys.SPACE);
		inputs.bind("action", Device.MOUSE, Input.Keys.LEFT);
		inputs.bind("camUp", Device.KEYBOARD, Input.Keys.W);
		inputs.bind("camLeft", Device.KEYBOARD, Input.Keys.A);
		inputs.bind("camDown", Device.KEYBOARD, Input.Keys.S);
		inputs.bind("camRight", Device.KEYBOARD, Input.Keys.D);
		inputs.bind("freeCam", Device.KEYBOARD, Input.Keys.F1);
		inputs.bind("playerCam", Device.KEYBOARD, Input.Keys.F2);
		inputs.bind("defaultCam", Device.KEYBOARD, Input.Keys.F3);
		inputs.bind("rebind", Device.KEYBOARD, Input.Keys.BACKSLASH);
    }
	
	public InputManager(Camera camera) {
		inputs = new Inputs();
		this.camera = camera;
		defaultBinding();
	}
	
	//get keypressed for keyboard and mouse
	public boolean inputPressed(String action) {
        return inputs.inputPressed(action);
	 }
	
	 public boolean inputHeld(String action) {
		 return inputs.inputHeld(action);
	 }
	 
	 public Vector2 getMousePosition() {
		 return inputs.getMousePosition();
	 }
	 
	 //For keybindings
	 public void setKeyBind(String action) {
		 inputs.setKeyBind(action);
	}	 

	//wait to set keybind for keyboard
	@Override
	public boolean keyDown(int keycode) {
		return inputs.keyDown(keycode);
   }

	//wait to set keybind for mouse
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return inputs.touchDown(x, y, pointer, button);
    } 
	
	public Set<String> getActions() {
	    return inputs.getActions();
	}
	
	public String getBindingName(String action) {
	    return inputs.getBindingName(action);
	}
	
	//For camera
	public void updateCamera(Entity player) {
		camera.control(player);
	    camera.update();
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public void usePlayerCamera() {
	    this.camera = new PlayerCamera(camera.getCamera());
	}

	public void useFreeCamera() {
	    this.camera = new FreeCamera(this, camera.getCamera());
	}
	
	public void useDefaultCamera() {
	    this.camera = new DefaultCamera(this, camera.getCamera());
	}
	 
}

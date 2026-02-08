package io.github.some_example_name.lwjgl3.managers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.inputs.Camera;
import io.github.some_example_name.lwjgl3.inputs.InputDevice;
import io.github.some_example_name.lwjgl3.inputs.InputDevice.Device;

public class InputManager extends InputAdapter{
    private final Map<String, Binding> bindings = new HashMap<>();
    private final Map<String, Boolean> lastState = new HashMap<>();
    private String waitingForBind = null;
    
    private final InputDevice inputDevice;
    private final Camera camera;
    //private final CameraController cameraController;
    
    
    public static class Binding {
        public Device device;
        public int code;

        Binding(Device device, int code) {
            this.device = device;
            this.code = code;
        }
    }
    
	
	public InputManager() {
		inputDevice = new InputDevice(this);
        camera = new Camera();
		bind("up", Device.KEYBOARD, Input.Keys.UP);
    	bind("down", Device.KEYBOARD, Input.Keys.DOWN);
    	bind("left", Device.KEYBOARD, Input.Keys.LEFT);
    	bind("right", Device.KEYBOARD, Input.Keys.RIGHT);
    	bind("jump", Device.KEYBOARD, Input.Keys.SPACE);
    	bind("action", Device.MOUSE, Input.Buttons.LEFT);
	}
	
	//add to the bindings and laststate hashmap
	public void bind(String action, Device device, int code) {
        bindings.put(action, new Binding(device, code));
        lastState.put(action, false);
    }
	
	//get keypressed for keyboard and mouse
	public boolean inputPressed(String action) {
        Binding b = bindings.get(action);
        //This will get the Binding that is mapped to the string action
        // After the above code, b.device == Device.KEYBOARD
        //b.code == Input.Keys.SPACE
        if (b == null) {
        	return false; // If it has no Binding mapped to the action
        }

        boolean pressed = inputDevice.isPressed(b); //pressed == the inputkey
        boolean wasPressed = lastState.getOrDefault(action, false);
        lastState.put(action, pressed);

        return pressed && !wasPressed; //returns true if a key is pressed the first time
	 }
	
	 public boolean inputHeld(String action) {
		 Binding b = bindings.get(action);
	     if (b == null) {
	    	 return false;
	     }
	     return inputDevice.isPressed(b);
	 }
	 
	 public Vector2 getMousePosition() {
		 return inputDevice.getMousePosition();
	 }
	 
	 //For keybindings
	 public void setKeyBind(String action) {
		waitingForBind = action;
		System.out.println("Press a key to bind for action: " + action);
	}	 
	

	public String getWaitingForBind() {
	    return waitingForBind; 
	    }
	    
	public void resetWaitingForBind() {
	    waitingForBind = null;
	    }
	
	@Override
	public boolean keyDown(int keycode) {
	    return inputDevice.keyDown(keycode);
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
	    return inputDevice.touchDown(x, y, pointer, button);
	}
	
	//For camera
	public void updateCamera(Entity player) {
	    camera.cameraControl(player);
	}

	public void setCameraProjection(SpriteBatch batch, ShapeRenderer shape){
		camera.setCameraProjection(batch, shape);
	}
	 
}

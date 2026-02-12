package io.github.some_example_name.lwjgl3.inputs;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.inputs.InputDevice.Device;


public class InputManager extends InputAdapter{
    private final Map<String, Binding> bindings = new HashMap<>();
    private final Map<String, Boolean> lastState = new HashMap<>();
    private String waitingForBind = null;
    
    private final InputDevice inputDevice;
    private Camera camera;
    
    protected static class Binding {
        public Device device;
        public int code;

        Binding(Device device, int code) {
            this.device = device;
            this.code = code;
        }
    }
    
	
	public InputManager(Camera camera) {
		inputDevice = new InputDevice(this);
		this.camera = camera;
		bind("up", Device.KEYBOARD, Input.Keys.UP);
    	bind("down", Device.KEYBOARD, Input.Keys.DOWN);
    	bind("left", Device.KEYBOARD, Input.Keys.LEFT);
    	bind("right", Device.KEYBOARD, Input.Keys.RIGHT);
    	bind("jump", Device.KEYBOARD, Input.Keys.SPACE);
    	bind("action", Device.MOUSE, Input.Buttons.LEFT);
    	bind("camUp", Device.KEYBOARD, Input.Keys.W);
    	bind("camLeft", Device.KEYBOARD, Input.Keys.A);
    	bind("camDown", Device.KEYBOARD, Input.Keys.S);
    	bind("camRight", Device.KEYBOARD, Input.Keys.D);
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
		 return inputDevice.mousePosition();
	 }
	 
	 //For keybindings
	 public void setKeyBind(String action) {
		waitingForBind = action;
		System.out.println("Press a key to bind for action: " + action);
	}	 
	

	private String getWaitingForBind() {
	    return waitingForBind; 
	    }
	    
	private void resetWaitingForBind() {
	    waitingForBind = null;
	    }
	

	//wait to set keybind for keyboard
	@Override
	public boolean keyDown(int keycode) {
       if (getWaitingForBind() != null) {
       	bind(getWaitingForBind(), Device.KEYBOARD, keycode);
           System.out.println("Bound " + getWaitingForBind() + " to button " + keycode);
           resetWaitingForBind();
           return true;
       }
       return false;
   }

	//wait to set keybind for mouse
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (getWaitingForBind()!= null) {
			 bind(getWaitingForBind(), Device.MOUSE, button);
             System.out.println("Bound " + getWaitingForBind() + " to button " + button);
             resetWaitingForBind();
             return true;
        }
        return false;
    } 
	
	//For camera
	public void updateCamera(Entity player) {
		camera.control(player);
	    camera.update();
	}

	public void setCamera(Camera newCamera) {
        this.camera = newCamera;
    }

    public Camera getCamera() {
        return camera;
    }
    
    public void switchCamera() {
    	Camera currentCam = getCamera();

        if (currentCam instanceof PlayerCamera) {
            setCamera(new FreeCamera(currentCam.getCamera(), this));
        } else {
            setCamera(new PlayerCamera(currentCam.getCamera()));
        }
    }
	 
}

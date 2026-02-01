package io.github.some_example_name.lwjgl3.managers;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class InputManager extends InputAdapter{
    private final Map<String, Binding> bindings = new HashMap<>();
    private final Map<String, Boolean> lastState = new HashMap<>();
    private String waitingForBind = null;
    
    private enum Device {
        KEYBOARD,
        MOUSE
    }
    
    private static class Binding {
        Device device;
        int code;

        Binding(Device device, int code) {
            this.device = device;
            this.code = code;
        }
    }
    
	private boolean isPressed(Binding b) {
    	if (b.device == Device.KEYBOARD) {
    	    return Gdx.input.isKeyPressed(b.code);
    	} 
    	else { //It is a mouse
    	    return Gdx.input.isButtonPressed(b.code);
    	}
    }

	public InputManager() {
		bind("up", Device.KEYBOARD, Input.Keys.UP);
    	bind("down", Device.KEYBOARD, Input.Keys.DOWN);
    	bind("left", Device.KEYBOARD, Input.Keys.LEFT);
    	bind("right", Device.KEYBOARD, Input.Keys.RIGHT);
    	bind("jump", Device.KEYBOARD, Input.Keys.SPACE);
    	bind("action", Device.MOUSE, Input.Buttons.LEFT);
	}
	
	//add to the bindings and laststate hashmap
	private void bind(String action, Device device, int code) {
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

        boolean pressed = isPressed(b); //pressed == the inputkey
        boolean wasPressed = lastState.getOrDefault(action, false);
        lastState.put(action, pressed);

        return pressed && !wasPressed; //returns true if a key is pressed the first time
    }
	
	 public boolean inputHeld(String action) {
		 Binding b = bindings.get(action);
	     if (b == null) {
	    	 return false;
	     }
	     return isPressed(b);
	 }
	
	 public Vector2 getMousePosition() {
		 return new Vector2(Gdx.input.getX(), Gdx.input.getY());
	}
	 
	 public void setKeyBind(String action) {
		waitingForBind = action;
		System.out.println("Press a key to bind for action: " + action);
	}
	
	
	//wait to set keybind for keyboard
	 public boolean keyDown(int keycode) {
        if (waitingForBind != null) {
        	bindings.put(waitingForBind, new Binding(Device.KEYBOARD, keycode));
            lastState.put(waitingForBind, false);
            System.out.println("Bound " + waitingForBind + " to button " + keycode);
            waitingForBind = null;
            return true;
        }
        return false;
    }

	 //wait to set keybind for mouse
	 public boolean touchDown(int x, int y, int pointer, int button) {
		 if (waitingForBind != null) {
			 bindings.put(waitingForBind, new Binding(Device.MOUSE, button));
             lastState.put(waitingForBind, false);
             System.out.println("Bound " + waitingForBind + " to button " + button);
             waitingForBind = null;
             return true;
        }
        return false;
    } 
	 
	 
}

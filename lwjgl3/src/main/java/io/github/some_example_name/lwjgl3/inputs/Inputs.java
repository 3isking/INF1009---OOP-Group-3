package io.github.some_example_name.lwjgl3.inputs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.lwjgl3.inputs.Inputs.Device;


public class Inputs {
	private final InputManager inputManager;
	private final Map<String, Binding> bindings = new HashMap<>();
    private final Map<String, Boolean> lastState = new HashMap<>();
    private String waitingForBind = null;
    
    private static class Binding {
        public Device device;
        public int code;

        Binding(Device device, int code) {
            this.device = device;
            this.code = code;
        }
    }

    public Inputs(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    
    public enum Device {
        KEYBOARD,
        MOUSE
    }
    
  //add to the bindings and laststate hashmap
  	public void bind(String action, Device device, int code) {
          bindings.put(action, new Binding(device, code));
          lastState.put(action, false);
      }
    
	public boolean isPressed(Binding b) {
    	if (b.device == Device.KEYBOARD) {
    	    return Gdx.input.isKeyPressed(b.code);
    	} 
    	else { //It is a mouse
    	    return Gdx.input.isButtonPressed(b.code);
    	}
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
		 
	public Vector2 mousePosition() {
		return new Vector2(Gdx.input.getX(), Gdx.input.getY());
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
	
	public boolean keyDown(int keycode) {
       if (getWaitingForBind() != null) {
       	bind(getWaitingForBind(), Device.KEYBOARD, keycode);
           System.out.println("Bound " + getWaitingForBind() + " to button " + keycode);
           resetWaitingForBind();
           return true;
       }
       return false;
   }
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (getWaitingForBind()!= null) {
			 bind(getWaitingForBind(), Device.MOUSE, button);
             System.out.println("Bound " + getWaitingForBind() + " to button " + button);
             resetWaitingForBind();
             return true;
        }
        return false;
    } 
	
	//Set is unordered and doesnt allow duplicates
	public Set<String> getActions() {
	    return bindings.keySet();
	}
	
	public String getBindingName(String action) {
	    Binding b = bindings.get(action);
	    if (b == null) {
	    	return "UNBOUND";
	    }
	    if (b.device == Device.KEYBOARD) {
	        return Input.Keys.toString(b.code);
	    } else {
	        return "Mouse " + b.code;
	    }
	}
}

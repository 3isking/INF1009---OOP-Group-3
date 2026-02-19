package io.github.some_example_name.lwjgl3.inputs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;


public class Inputs {
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

    public Inputs() {

    }
    
    public enum Device {
        KEYBOARD,
        MOUSE
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
  		 
  	public Vector2 getMousePosition() {
  		return new Vector2(Gdx.input.getX(), Gdx.input.getY());
  	}
    
  	//Bindings
    //add to the bindings and laststate hashmap
  	public boolean bind(String action, Device device, int code) {
  		for (Map.Entry<String, Binding> binded : bindings.entrySet()) {
  			String bindedAction = binded.getKey();
            Binding bindedKey = binded.getValue(); // Get the value
            if (bindedKey.device == device && bindedKey.code==code) {
            	System.out.println("Key/button " + code + " is already bound to " + bindedAction);
            	return false;
            }
            
        }
          bindings.put(action, new Binding(device, code));
          lastState.put(action, false);
          return true;
      }

	 public void setKeyBind(String action) {
		waitingForBind = action;
		System.out.println("Press a key to bind for action: " + action);
	}
	
	 // Detect keybind input
	 public boolean keyDown(int keycode) {
       if (waitingForBind != null) {
       	if (bind(waitingForBind, Device.KEYBOARD, keycode)) {
       		System.out.println("Bound " + waitingForBind);     
       	}
       	else {
       		System.out.println(waitingForBind + " is already binded to another key");
       	}
       	waitingForBind = null;
       	return true;
       }
       return false;
   }
	
	 public boolean touchDown(int x, int y, int pointer, int button) {
		if (waitingForBind != null) {
			 if (bind(waitingForBind, Device.MOUSE, button)) {
				 System.out.println("Bound " + waitingForBind + " to button " + button);
			 }
			 else {
				 System.out.println(waitingForBind + " is already binded to another key");
			 }
			 waitingForBind = null;
			 return true;
        }
        return false;
    } 
}

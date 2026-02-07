package io.github.some_example_name.lwjgl3.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.managers.InputManager;


public class InputDevice {
	
	public enum Device {
        KEYBOARD,
        MOUSE
    }
	
	private final InputManager manager;

    public InputDevice(InputManager manager) {
        this.manager = manager;
    }
    
	public boolean isPressed(InputManager.Binding b) {
    	if (b.device == Device.KEYBOARD) {
    	    return Gdx.input.isKeyPressed(b.code);
    	} 
    	else { //It is a mouse
    	    return Gdx.input.isButtonPressed(b.code);
    	}
    }
	
	//wait to set keybind for keyboard
		 public boolean keyDown(int keycode) {
	        if (manager.getWaitingForBind() != null) {
	        	manager.bind(manager.getWaitingForBind(), Device.KEYBOARD, keycode);
	            System.out.println("Bound " + manager.getWaitingForBind() + " to button " + keycode);
	            manager.resetWaitingForBind();
	            return true;
	        }
	        return false;
	    }

		 //wait to set keybind for mouse
		 public boolean touchDown(int x, int y, int pointer, int button) {
			 if (manager.getWaitingForBind()!= null) {
				 manager.bind(manager.getWaitingForBind(), Device.MOUSE, button);
	             System.out.println("Bound " + manager.getWaitingForBind() + " to button " + button);
	             manager.resetWaitingForBind();
	             return true;
	        }
	        return false;
	    } 
		 
		 public Vector2 getMousePosition() {
			 return new Vector2(Gdx.input.getX(), Gdx.input.getY());
		}
}

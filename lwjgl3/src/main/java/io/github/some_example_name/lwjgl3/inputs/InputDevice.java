package io.github.some_example_name.lwjgl3.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;


public class InputDevice {
	private final InputManager inputManager;
	
	public enum Device {
        KEYBOARD,
        MOUSE
    }

    public InputDevice(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    
	public boolean isPressed(InputManager.Binding b) {
    	if (b.device == Device.KEYBOARD) {
    	    return Gdx.input.isKeyPressed(b.code);
    	} 
    	else { //It is a mouse
    	    return Gdx.input.isButtonPressed(b.code);
    	}
    }
		 
	public Vector2 mousePosition() {
		return new Vector2(Gdx.input.getX(), Gdx.input.getY());
	}
}

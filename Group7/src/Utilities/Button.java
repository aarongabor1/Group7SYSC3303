package Utilities;

import java.io.Serializable;

/**
 * Class to simulate a button.
 * 
 * @author Marc Angers
 * @version 1.1
 */
public abstract class Button implements Serializable {
	private static final long serialVersionUID = 672315112732782201L;
	private boolean isPressed = false;
	private boolean lamp;
	
	public Button(boolean buttonLamp) {
		this.lamp = buttonLamp;
	}
	
	public void press() {
		isPressed = true;
		lamp = true;
	}
	
	public void unPress() {
		isPressed = false;
		lamp = false;
	}
	
	public void switchButton() {
		isPressed = !isPressed;
		lamp = !lamp;
	}
	
	public boolean isPressed() {
		return this.isPressed;
	}
	
	public boolean getButtonLamp() {
		return lamp;
	}
}


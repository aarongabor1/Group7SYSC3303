package Utilities;

/**
 * 
 * @author Marc Angers
 * @version 1.0.0
 */
public abstract class Button {
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


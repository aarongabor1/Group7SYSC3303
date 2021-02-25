package Utilities;

/**
 * 
 * @author Marc Angers
 * @version 1.0.0
 */
public abstract class Button {
	private boolean isPressed = false;
	private Lamp buttonLamp;
	
	public Button(Lamp buttonLamp) {
		this.buttonLamp = buttonLamp;
	}
	
	public void press() {
		isPressed = true;
		buttonLamp.turnOn();
	}
	
	public void unPress() {
		isPressed = false;
		buttonLamp.turnOff();
	}
	
	public void switchButton() {
		isPressed = !isPressed;
		buttonLamp.switchLamp();
	}
	
	public boolean isPressed() {
		return this.isPressed;
	}
}


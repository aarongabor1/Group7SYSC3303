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
	
	public void Press() {
		isPressed = true;
		buttonLamp.turnOn();
	}
	public void turnOffLamp() {
		isPressed = false;
		buttonLamp.turnOff();
	}
	
	public boolean isPressed() {
		return this.isPressed;
	}
}

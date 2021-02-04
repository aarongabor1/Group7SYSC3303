/**
 * 
 * @author Marc Angers
 * @version 1.0.0
 */
public abstract class Lamp {
	private boolean isOn = false;
	
	public void turnOn() {
		isOn = true;
	}
	public void turnOff() {
		isOn = false;
	}
}

/**
 * 
 * @author Marc Angers
 * @version 1.0.0
 */
public abstract class Lamp {
	private boolean isOn = false;
	
	public void switchLamp() {
		isOn = !isOn;
	}
	
	public boolean isOn() {
		return this.isOn;
	}
}

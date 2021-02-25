package Utilities;

/**
 * 
 * @author Marc Angers
 * @version 1.0.0
 */
public abstract class Lamp {
	private boolean isOn;
	
	public Lamp() {
		this.isOn = false;
	}
	
	public void turnOn() {
		isOn = true;
	}
	
	public void turnOff() {
		isOn = false;
	}
	
	public void switchLamp() {
		isOn = !isOn;
	}
	
	public boolean isOn() {
		return this.isOn;
	}
}

package Elevator;

import Utilities.*;

/**
 * 
 * @author Marc Angers
 * @version 1.0.0
 */
public class ElevatorButton extends Button {
	private int floor;
	
	public ElevatorButton(boolean elevatorLamp, int floor) {
		super(false);
		this.floor = floor;
	}

	public int getFloorButtonNumber() {
		return floor;
	}
	
	public void press() {
		super.press();
		System.out.println("ELEVATOR: Floor #" + floor + " has been pressed - button lamp is on");
	}
	
	public void unPress() {
		super.press();
		System.out.println("ELEVATOR: Floor #" + floor + " button lamp is off");
	}
	
}

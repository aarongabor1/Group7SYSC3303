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
	
}

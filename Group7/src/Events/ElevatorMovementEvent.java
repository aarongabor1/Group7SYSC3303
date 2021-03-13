package Events;

import Utilities.Settings;
import Floor.Floor;

import java.io.Serializable;
import java.sql.Time;

/**
 * Event class to notify an elevator that its destination floor has changed.
 * @author Marc Angers
 * @vertion 1.0.0
 */
public class ElevatorMovementEvent implements Serializable {
	private static final long serialVersionUID = -8885169692118294920L;
	public Time time;
	public int elevatorID;
	public int currentFloor;
	
	public ElevatorMovementEvent(Time time, int id, int current) {
		if (current < Floor.MINIMUM_FLOOR_NUM || current > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("The current floor cannot be outside of the building!");
		
		this.time = time;
		elevatorID = id;
		currentFloor = current;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
}

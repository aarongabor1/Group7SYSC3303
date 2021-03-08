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
public class DestinationUpdateEvent implements Serializable {
	private static final long serialVersionUID = 7558165628865501458L;
	public Time time;
	public int elevatorID;
	public int destinationFloor;
	
	public DestinationUpdateEvent(Time time, int id, int destination) {
		if (destination < Floor.MINIMUM_FLOOR_NUM || destination > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("The destination floor cannot be outside of the building!");
		
		this.time = time;
		elevatorID = id;
		destinationFloor = destination;
	}
	
	public int getDestinationFloor() {
		return destinationFloor;
	}
}

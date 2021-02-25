package Events;

import Utilities.Settings;
import Floor.Floor;
import java.sql.Time;

/**
 * Event class to notify an elevator that its destination floor has changed.
 * @author Marc Angers
 * @vertion 1.0.0
 */
public class DestinationUpdateEvent {
	public Time time;
	public int elevatorID;
	public int destinationFloor;
	
	public DestinationUpdateEvent(int id, int destination) {
		if (destination < Floor.MINIMUM_FLOOR_NUM || destination > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("The destination floor cannot be outside of the building!");
		
		elevatorID = id;
		destinationFloor = destination;
	}
}

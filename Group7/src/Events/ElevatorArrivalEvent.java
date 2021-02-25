package Events;

import Utilities.Direction;
import Utilities.Settings;
import Floor.Floor;
import java.sql.Time;

/**
 * Event class to notify the floor subsystem when an elevator has reached a floor.
 * @author Marc Angers
 * @version 1.0.0
 */
public class ElevatorArrivalEvent {
	public Time time;
	public int floorNumber;
	public Direction fromDirection;
	
	public ElevatorArrivalEvent(int floor, Direction direction) {
		if (floor < Floor.MINIMUM_FLOOR_NUM || floor > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("An elevator cannot arrive at an invalid floor!");
		
		floorNumber = floor;
		fromDirection = direction;
	}
}

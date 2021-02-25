package Events;

import Utilities.Direction;
import Utilities.Settings;
import Floor.Floor;
import java.sql.Time;

/**
 * Event class to notify the scheduler when a button on a floor is pressed.
 * @author Marc Angers
 * @version 1.0.0
 */
public class FloorButtonPressEvent {
	public Time time;
	public int floor;
	public Direction direction;
	
	public FloorButtonPressEvent(int floor, Direction direction) {
		if (floor < Floor.MINIMUM_FLOOR_NUM || floor > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("A button cannot be pressed on a floor that doesn't exist!");
		
		this.floor = floor;
		this.direction = direction;
	}
}

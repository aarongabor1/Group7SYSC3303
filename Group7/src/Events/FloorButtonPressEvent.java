package Events;

import Utilities.Direction;
import Utilities.Settings;
import Floor.Floor;

import java.io.Serializable;
import java.sql.Time;

/**
 * Event class to notify the scheduler when a button on a floor is pressed.
 * @author Marc Angers
 * @version 1.1
 */
public class FloorButtonPressEvent implements Serializable {
	private static final long serialVersionUID = 9182781161988433205L;
	public Time time;
	public int floor;
	public Direction direction;
	
	public FloorButtonPressEvent(Time time, int floor, Direction direction) {
		if (floor < Floor.MINIMUM_FLOOR_NUM || floor > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("A button cannot be pressed on a floor that doesn't exist!");
		
		this.time = time;
		this.floor = floor;
		this.direction = direction;
	}
	
	public FloorButtonPressEvent(FormattedEvent fe) {
		this.time = fe.getTime();
		this.floor = fe.getFloor();
		this.direction = fe.getDirection();
	}
	
	public Direction getDirection() {
	    return direction;
	}
	
	public int getFloor() {
	    return floor;
	}
}

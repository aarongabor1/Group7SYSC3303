package Events;

import Utilities.Direction;
import Utilities.Settings;
import Floor.Floor;

import java.io.Serializable;
import java.sql.Time;

/**
 * Event class to notify the floor subsystem when an elevator has reached a floor.
 * @author Marc Angers
 * @version 1.0.0
 */
public class ElevatorArrivalEvent implements Serializable {
	private static final long serialVersionUID = -4504430610082759470L;
	public Time time;
	public int floorNumber;
	public int elevatorID;
	public Direction goingInDirection;
	
	public ElevatorArrivalEvent(Time time, int floor, int elevator, Direction direction) {
		if (floor < Floor.MINIMUM_FLOOR_NUM || floor > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("An elevator cannot arrive at an invalid floor!");
		
		this.time = time;
		floorNumber = floor;
		elevatorID = elevator;
		goingInDirection = direction;
	}
}

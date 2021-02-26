package Events;

import Utilities.Direction;
import java.sql.Time;

/**
 * Event class to notify the scheduler when an elevator has moved so it can keep track of elevator locations.
 * @author Marc Angers
 * @version 1.0.0
 */
public class ElevatorMovementEvent {
	public Time time;
	public int elevatorID;
	public Direction direction;
	public int currentFloor;
	public boolean isMoving;
	
	public ElevatorMovementEvent(int elevator, Direction direction, int currentFloor, boolean isMoving) {
		elevatorID = elevator;
		this.direction = direction;
		this.currentFloor = currentFloor;
		this.isMoving = isMoving;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public boolean isMoving() {
		return isMoving;
	}
}

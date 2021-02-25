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
	
	public ElevatorMovementEvent(int elevator, Direction direction, int currentFloor) {
		elevatorID = elevator;
		this.direction = direction;
		this.currentFloor = currentFloor;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}
}

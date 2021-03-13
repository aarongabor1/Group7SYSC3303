package Events;

import Utilities.Settings;
import Floor.Floor;

import java.io.Serializable;
import java.sql.Time;

import Elevator.ElevatorState;

/**
 * Event class to notify an elevator that its destination floor has changed.
 * @author Marc Angers
 * @vertion 1.0.0
 */
public class ElevatorMovementEvent implements Serializable {
	private static final long serialVersionUID = -8885169692118294920L;
	public Time time;
	public int elevatorID;
	public ElevatorState elevatorState;
	
	public ElevatorMovementEvent(Time time, int id, ElevatorState state) {
		if (state.getFloor() < Floor.MINIMUM_FLOOR_NUM || state.getFloor() > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("The current floor cannot be outside of the building!");
		if (state.getDestination() < Floor.MINIMUM_FLOOR_NUM || state.getDestination() > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("The current destination cannot be outside of the building!");
		
		this.time = time;
		elevatorID = id;
		elevatorState = state;
	}
	
	public ElevatorState getElevatorState() {
		return elevatorState;
	}
}

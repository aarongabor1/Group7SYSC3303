package Events;

import Utilities.Settings;
import Floor.Floor;

import java.io.Serializable;

import Elevator.ElevatorState;

/**
 * Event class to notify an elevator that its destination floor has changed.
 * @author Marc Angers
 * @vertion 1.0
 */
public class ElevatorMovementEvent implements Serializable {
	private static final long serialVersionUID = -8885169692118294920L;
	public long time;
	public int elevatorID;
	public ElevatorState elevatorState;
	public boolean shutDown;
	public boolean softFailure;
	
	public ElevatorMovementEvent(long time, int id, ElevatorState state, boolean shutDown, boolean softFailure) {
		if (state.getFloor() < Floor.MINIMUM_FLOOR_NUM || state.getFloor() > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("The current floor cannot be outside of the building!");
		if (state.getDestination() < Floor.MINIMUM_FLOOR_NUM || state.getDestination() > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("The current destination cannot be outside of the building!");
		
		this.time = time;
		elevatorID = id;
		elevatorState = state;
		this.shutDown = shutDown;
		this.softFailure = softFailure;
	}
	
	public ElevatorState getElevatorState() {
		return elevatorState;
	}
	
	public boolean isSoftFailure() {
	    return softFailure;
	}

}

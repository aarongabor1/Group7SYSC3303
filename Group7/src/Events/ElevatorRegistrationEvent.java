package Events;

import java.io.Serializable;

import Elevator.Elevator;

/**
 * Event class to notify the scheduler when an elevator is initialized.
 * 
 * @author Marc Angers
 * @version 1.0
 */
public class ElevatorRegistrationEvent implements Serializable {
	private static final long serialVersionUID = -5613322073057457807L;
	public long time;
	public Elevator elevator;

	public ElevatorRegistrationEvent(long time, Elevator parentElevator) {
		elevator = parentElevator;
		this.time = time;
	}
	
	public Elevator getElevator() {
		return elevator;
	}
}

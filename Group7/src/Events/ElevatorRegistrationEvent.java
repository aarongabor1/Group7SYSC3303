package Events;

import java.io.Serializable;

import Elevator.Elevator;

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

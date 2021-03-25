package Events;

import java.io.Serializable;

/**
 * Generic failure event class that will contain soft and hard failure events.
 * @author Marc Angers
 * @version 1.1
 */
public class FailureEvent implements Serializable {
	private static final long serialVersionUID = 1654836718423878380L;
	private long time;
	private int elevatorID;
	
	public FailureEvent(long time, int elevatorID) {
		this.time = time;
		this.elevatorID = elevatorID;
	}
	
	// Get and set methods:
	public long getTime() {
		return this.time;
	}
	
	public int getElevator() {
		return this.elevatorID;
	}
}

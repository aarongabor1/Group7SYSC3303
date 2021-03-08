package Events;

import Floor.Floor;
import Utilities.Settings;

import java.io.Serializable;
import java.sql.Time;

/**
 * Event class to notify the scheduler that a button was pressed within an elevator.
 * @author Marc Angers
 * @version 1.0.0
 */
public class ElevatorButtonPressEvent implements Serializable {
	private static final long serialVersionUID = 8392902175566934473L;
	public Time time;
	public int elevatorID;
	public int buttonNumber;
	
	public ElevatorButtonPressEvent(Time time, int elevator, int button) {
		if (button < Floor.MINIMUM_FLOOR_NUM || button > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("Cannot press an elevator button that does not exist!");
		
		this.time = time;
		elevatorID = elevator;
		buttonNumber = button;
	}
	
	public ElevatorButtonPressEvent(FormattedEvent fe) {
		this.time = fe.getTime();
		elevatorID = 1; // <-- fix this later
		buttonNumber = fe.getCarButton();
	}
}

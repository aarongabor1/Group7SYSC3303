package Events;

import Floor.Floor;
import Utilities.Settings;

import java.io.Serializable;

/**
 * Event class to notify the scheduler that a button was pressed within an elevator.
 * 
 * @author Marc Angers
 * @version 1.1
 */
public class ElevatorButtonPressEvent implements Serializable {
	private static final long serialVersionUID = 8392902175566934473L;
	public long time;
	public int elevatorID;
	public int buttonNumber;
	
	public ElevatorButtonPressEvent(long time, int elevator, int button) {
		if (button < Floor.MINIMUM_FLOOR_NUM || button > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("Cannot press an elevator button that does not exist!");
		
		this.time = time;
		elevatorID = elevator;
		buttonNumber = button;
	}
	
	public ElevatorButtonPressEvent(FormattedEvent fe) {
		this.time = fe.getTime();
		elevatorID = 0; // We don't know which elevator the button press is coming from when the event is initialized, will update it later.
		buttonNumber = fe.getCarButton();
	}
	
	// Get and set methods:
	public void updateElevatorID(int newID) {
		elevatorID = newID;
	}
}

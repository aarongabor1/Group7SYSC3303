/**
 * 
 * @author Marc Angers
 * @version 1.0.0
 */
public class ElevatorButton extends Button {
	private int floor;
	
	public ElevatorButton(ElevatorLamp elevatorLamp, int floor) {
		super(elevatorLamp);
		this.floor = floor;
	}
}

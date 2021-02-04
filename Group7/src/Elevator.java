import java.util.LinkedList;
import java.util.List;

public class Elevator {
	private List<ElevatorButton> elevatorButtons;
	private Motor motor;
	private Door door;
	private int numberOfFloors;
	
	public Elevator(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
		this.elevatorButtons = new LinkedList<>();
	}
	
	public void generateFloorButtons() {
		for (int i = 1; i <= numberOfFloors; i++) {
			elevatorButtons.add(new ElevatorButton(new ElevatorLamp(), i));
		}
	}
	
	public void pressDestinationButton(int destinationFloor) {
		
	}
	
	public void
	
	
	
	
}

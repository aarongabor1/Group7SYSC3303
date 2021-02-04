import java.util.LinkedList;
import java.util.List;

public class Elevator {
	private List<ElevatorButton> elevatorButtons;
	private Motor motor;
	private Door door;
	private int numberOfFloors;
	private int currentFloor;
	
	public Elevator(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
		this.elevatorButtons = new LinkedList<>();
		this.motor = new Motor();
		this.door = new Door(DoorPosition.CLOSED);
		this.currentFloor = 1;
		generateElevatorButtons();
	}
	
	public void generateElevatorButtons() {
		for (int i = 1; i <= numberOfFloors; i++) {
			elevatorButtons.add(new ElevatorButton(new ElevatorLamp(), i));
		}
	}
	
	public void moveDown() {
		currentFloor--;
	}
	
	public void moveUp() {
		currentFloor++;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public Door getDoor() {
		return door;
	}
	
	public Motor getMotor() {
		return motor;
	}
	
	
}

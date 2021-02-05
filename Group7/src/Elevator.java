import java.util.LinkedList;
import java.util.List;

/**
 * Elevator class is for the ElevatorSubsytem class to control.
 * 
 * @author Diana Miraflor
 * @version 1.0
 *
 */
public class Elevator {
	public static final int MINIMUM_FLOOR_NUM = 1;
	
	private List<ElevatorButton> elevatorButtons;
	private Motor motor;
	private Door door;
	private int numberOfFloors;
	private int currentFloor;
	
	/**
	 * Constructs a new Elevator object
	 * 
	 * @param numberOfFloors
	 */
	public Elevator(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
		this.elevatorButtons = new LinkedList<>();
		this.motor = new Motor(Direction.STATIONARY);
		this.door = new Door(DoorPosition.CLOSED);
		this.currentFloor = MINIMUM_FLOOR_NUM;
		generateElevatorButtons();
	}
	
	/**
	 * Creates the elevator's floor buttons
	 */
	public void generateElevatorButtons() {
		for (int i = 1; i <= numberOfFloors; i++) {
			elevatorButtons.add(new ElevatorButton(new ElevatorLamp(), i));
		}
	}
	
	/**
	 * Moves elevator down
	 */
	public void moveDown() {
		currentFloor--;
	}
	
	/**
	 * Moves elevator up
	 */
	public void moveUp() {
		currentFloor++;
	}
	
	/**
	 * Returns the current floor the elevator is at
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	/**
	 * Returns elevator's door
	 * @return
	 */
	public Door getDoor() {
		return door;
	}
	
	/**
	 * Returns elevator's motor
	 * @return
	 */
	public Motor getMotor() {
		return motor;
	}
	
	
}

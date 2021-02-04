
public class ElevatorSubsystem implements Runnable{
	//private Network network;
	private Elevator elevator;
	private int currentFloor;
	private boolean isMoving; // May be unnecessary because of elevator state enum
	
	public ElevatorSubsystem(Elevator elevator) {
		this.elevator = elevator;
		this.currentFloor = 1; // Get rid of magic numbers
	}

	/**
	 * Elevator subsystem receives the destination floor from FloorSubsystem
	 */
	@Override
	public void run() {
		while(currentFloor != ta)
		
	}
	
	public void operateMotor() {
		elevator.
	}
	
	public void handleMovingElevator() {
		
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public void setDestinationFloor(int destFloor) {
		
	}
	
	
}

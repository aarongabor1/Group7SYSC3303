
public class ElevatorSubsystem implements Runnable{
	private Network network;
	private Scheduler scheduler;
	private Elevator elevator;
	private int destinationFloor;
	private Direction elevatorState; // May be unnecessary because of elevator state enum
	
	public ElevatorSubsystem(Elevator elevator, Scheduler scheduler, Network network) {
		this.network = network;
		this.elevator = elevator;
		this.scheduler = scheduler;
	}

	@Override
	public void run() {
		while(!scheduler.isElevatorRequestsEmpty()) {
			// int destinatonFloor = network.floorToElevator() should return the targeted floor
			// int serviceFloor = 
			// while (currentFloor!=destinationFloor)
			// if currentFloor is above destinationFloor -> move elevator up, otherwise move down
			// moveElevator()
			// elevatorScheduler(currentFloor, elevatorState)
			// if currentFloor is at destinationFloor -> stop motor, open doors	
			
			
		}
		
	}
	
	public int getDestinationFloor() {
		return destinationFloor;
	}
	
	public void moveElevator(Direction direction) {
		handleMotor(direction);
		handleDoor(true);
		
		elevatorState = direction;
		
		if (direction == Direction.UP) {
			elevator.moveUp();
		} else if (direction == Direction.DOWN) {
			elevator.moveDown();
		}
	}
	
	public void handleMotor(Direction direction) {
		elevator.getMotor().moveElevator(direction);
	}
	
	public void handleDoor(boolean isMoving) {
		if (isMoving) {
			elevator.getDoor().closeDoor();
		} else {
			elevator.getDoor().openDoor();
		}
		
	}
	
	
}

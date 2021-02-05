/**
 * ElevatorSubsystem is a class that controls one elevator
 * 
 * @author Diana Miraflor
 * @version 1.0
 */
public class ElevatorSubsystem implements Runnable{
	private Network network;
	private Elevator elevator;
	private int destinationFloor;
	private Direction elevatorState; // May be unnecessary because of elevator state enum
	
	/**
	 * Constructor for a new ElevatorSubsytem
	 * 
	 * @param elevator
	 * @param network
	 */
	public ElevatorSubsystem(Elevator elevator, Network network) {
		this.network = network;
		this.elevator = elevator;
	}

	@Override
	public void run() {
		while(true) {
			
			FloorEvent fe = network.schedToElevatorSystem(null, 2);
			System.out.println("Received from Scheduler");
			
			int destinationFloor = network.floorToElevatorSystem(0, 2); // 0 is just a random number that is not being sent in
			
			
			network.elevatorSystemToSched(fe, 2);
			System.out.println("Sent to Scheduler");
			
			 try { 
	            	Thread.sleep(500);
	            } catch (InterruptedException e){
	            	
	            }
		}
		
	}
	
	/**
	 * Returns the destination floor the user wants to go to
	 * 
	 * @return
	 */
	public int getDestinationFloor() {
		return destinationFloor;
	}
	
	/**
	 * Moves motor, closes doors and moves elevator up or down according to the targeted floor.
	 * 
	 * @param direction
	 */
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
	
	/**
	 * Moves elevator to the according direction
	 * 
	 * @param direction
	 */
	public void handleMotor(Direction direction) {
		elevator.getMotor().moveElevator(direction);
	}
	
	/**
	 * Closes door when elevator is moving,  opens door when elevator stops moving.
	 * 
	 * @param isMoving
	 */
	public void handleDoor(boolean isMoving) {
		if (isMoving) {
			elevator.getDoor().closeDoor();
		} else {
			elevator.getDoor().openDoor();
		}
	}
		
}

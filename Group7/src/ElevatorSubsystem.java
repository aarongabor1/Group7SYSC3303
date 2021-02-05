/**
 * 
 * @author Diana Miraflor, Marc Angers
 *
 */
public class ElevatorSubsystem implements Runnable {
	private Network network;
	private Elevator elevator;
	private int destinationFloor;
	private Direction elevatorState; // May be unnecessary because of elevator state enum
	
	public ElevatorSubsystem(Elevator elevator, Network network) {
		this.network = network;
		this.elevator = elevator;
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			FloorEvent floorEvent = network.getSchedulerSystemEvent();
			System.out.println("Elevator received FloorEvent from Scheduler");			
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Will most likely be a different kind of event with different information in the future, but for now just return the original floor event
			network.putElevatorSystemEvent(floorEvent);
			System.out.println("Elevator sent FloorEvent to Scheduler");
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

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

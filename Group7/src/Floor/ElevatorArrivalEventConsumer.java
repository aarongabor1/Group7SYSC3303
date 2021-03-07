package Floor;

import Events.ElevatorArrivalEvent;

/**
 * Class to consume the elevator arrival events provided by the scheduler.
 * @author Marc Angers
 * @version 1.0.0
 */
public class ElevatorArrivalEventConsumer implements Runnable {
	private FloorSubsystem parent;
	
	public ElevatorArrivalEventConsumer(FloorSubsystem floorSubsystem) {
		parent = floorSubsystem;
	}
	
	/**
	 * Method to consume the provided event.
	 */
	public void consume(ElevatorArrivalEvent elevatorEvent) {
		parent.turnOffLampForFloor(elevatorEvent.floorNumber, elevatorEvent.goingInDirection);
	}
	
	@Override
	public void run() {
		while (true) {
			consume(parent.getScheduler().getElevatorArrivalEvent());
		}
	}
}

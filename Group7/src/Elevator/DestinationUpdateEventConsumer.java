package Elevator;

import Events.DestinationUpdateEvent;

/**
 * Class to update the elevator's destination by consuming the DestinationUpdateEvents provided by the scheduler.
 * @author Marc Angers
 * @version 1.0.0
 */
public class DestinationUpdateEventConsumer implements Runnable {
	private ElevatorSubsystem parent;
	
	public DestinationUpdateEventConsumer(ElevatorSubsystem elevatorSubsystem) {
		parent = elevatorSubsystem;
	}
	
	/**
	 * Function to consume the destination update event from the scheduler.
	 * @param destinationUpdateEvent
	 */
	public void consume(DestinationUpdateEvent destinationUpdateEvent) {
		if (destinationUpdateEvent.elevatorID != parent.getElevator().ID)
			parent.getScheduler().addDestinationUpdateEvent(destinationUpdateEvent);
		else {
			System.out.println("ElevatorSubsystem destination floor: " + destinationUpdateEvent.destinationFloor);
			System.out.println("Elevator current floor: " + parent.getElevator().getCurrentFloor());
			parent.getElevator().updateDestination(destinationUpdateEvent.destinationFloor);
		}
	}
	
	@Override
	public void run() {
		while (true) {
			consume(parent.getScheduler().getDestinationUpdateEvent());
		}
	}
}

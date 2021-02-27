package Floor;

import Events.FloorButtonPressEvent;

/**
 * Class to consume the floor button press events that are provided by the scheduler.
 * @author Marc Angers
 * @version 1.0.0
 */
public class FloorButtonPressEventConsumer implements Runnable {
	private FloorSubsystem parent;
	
	public FloorButtonPressEventConsumer(FloorSubsystem floorSubsystem) {
		parent = floorSubsystem;
	}
	
	/**
	 * Method to consume the provided event.
	 */
	public void consume(FloorButtonPressEvent floorEvent) {
		parent.turnOnLampForFloor(floorEvent.floor, floorEvent.direction);
	}
	
	@Override
	public void run() {
		while (true) {
			consume(parent.getScheduler().getFloorButtonEvent());
		}
	}
}

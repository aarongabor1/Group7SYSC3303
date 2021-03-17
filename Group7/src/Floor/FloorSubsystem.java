package Floor;

import Utilities.*;
import java.util.HashMap;
import java.util.Map;

/***
 * The Floor Subsystem acts as the controller for the floors of the building. Information from the scheduler is passed through this class to the individual floors.
 * @author lynnmehyou, Marc Angers
 * @version 1.2
 */

public class FloorSubsystem implements Runnable {
	private Map<Integer, Floor> floors;
	
	private Thread floorButtonPressEventConsumer;
	private Thread elevatorArrivalEventConsumer;
		
	
	
	public FloorSubsystem () {
		floors = new HashMap<Integer, Floor>();
		
		floorButtonPressEventConsumer = new Thread(new FloorButtonPressEventConsumer(this), "Floor button press event consumer");
		elevatorArrivalEventConsumer = new Thread(new ElevatorArrivalEventConsumer(this), "Elevator arrival event consumer");
		
		generateFloors();
	}
	
	/**
	 * generates floors depending on the number of floors specified in the settings file. 
	 */
	public void generateFloors() {
		for (int i = 1; i <= Settings.NUMBER_OF_FLOORS; i++) {
			Floor tempFloor = new Floor(i);
			floors.put(i, tempFloor);
		}
	}
	
	/**
	 * Function to turn on the lamp for a button on a floor
	 * @param floor
	 * @param direction
	 */
	public void turnOnLampForFloor(int floor, Direction direction) {
		floors.get(floor).turnOnLamp(direction);
	}
	
	/**
	 * Function to turn off the lamp for a button on a floor
	 * @param floor
	 * @param direction
	 */
	public void turnOffLampForFloor(int floor, Direction direction) {
		floors.get(floor).turnOffLamp(direction);
	}
	
	@Override
	public void run() {
		floorButtonPressEventConsumer.start();
		elevatorArrivalEventConsumer.start();
	}
	
	// Get and set methods:
	public Map<Integer, Floor> getFloors() {
		return floors;
	}
}

/***
 * The Floor Subsystem is responsible for reading the requests from
 * a textfile and sending these requests to the network.
 * @author lynnmehyou, Marc Angers
 */
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloorSubsystem implements Runnable{
	private Scheduler scheduler;
	private Map<Integer, Floor> floors;
	
	private Thread floorEventGenerator;
	
	/***
	 * FloorSubsytem constructor
	 * @param numberOfFloors
	 * @param network
	 */
	public FloorSubsystem (int numberOfFloors, Scheduler scheduler) {
		this.scheduler = scheduler;
		this.floors = new HashMap<Integer, Floor>();
		floorEventGenerator = new Thread(new FloorEventGenerator(scheduler), "Floor Event Generator");
	}
	/***
	 * generates floors depending on the floor number 
	 * @param numberOfFloors
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
		floorEventGenerator.start();
	}
}

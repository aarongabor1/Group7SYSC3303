import java.text.ParseException;

/**
 * Class to simulate the presses of the floor buttons.
 * This class will send the floor events to the scheduler.
 * 
 * @author Marc Angers
 * @version 1.0.0
 */
public class FloorEventGenerator implements Runnable {
	private FloorSubsystem parent;
	private Network network;
	
	private Parser parser;
	
	public FloorEventGenerator(Network network) {
		this.network = network;
		
		this.parser = new Parser();
	}
	
	public void run() {
		FloorEvent floorEvent;
		
		while(true) {			
			try {
				floorEvent = generateFloorEvent();
				
				parent.turnOnLampForFloor(floorEvent.getFloor(), floorEvent.getDirection());
				network.putFloorSystemEvent(floorEvent);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			//floorEvent = network.getSchedulerSystemEvent();
			//System.out.println("Floor event (" + floorEvent + ") received by the floor subsystem.");
		
		}
	}

	/**
	 * Generates floor events using the parser file.
	 * @return the parsed floor event from the text file
	 * @throws ParseException
	 */
	public FloorEvent generateFloorEvent() throws ParseException {
		return parser.parseFile();
	}
}

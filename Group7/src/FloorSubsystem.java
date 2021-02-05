/***
 * The Floor Subsystem is responsible for reading the requests from
 * a textfile and sending these requests to the network.
 * @author lynnmehyou, Marc Angers
 */
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class FloorSubsystem implements Runnable{
	private Network network;
	private List<Floor> floors;
	private Parser p;
	
	/***
	 * FloorSubsytem constructor
	 * @param numberOfFloors
	 * @param network
	 */
	public FloorSubsystem(int numberOfFloors, Network network) {
		this.network = network;
		this.floors = new ArrayList<Floor>(numberOfFloors);
		this.p = new Parser();
	}
	/***
	 * generates floors depending on the floor number 
	 * @param numberOfFloors
	 */
	public void generateFloors(int numberOfFloors) {
		this.floors = new ArrayList<Floor>(numberOfFloors);
	}
	
	@Override
	public void run() {
		FloorEvent floorEvent;
		
		while(true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try {
				network.putFloorSystemEvent(generateFloorEvent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			floorEvent = network.getSchedulerSystemEvent();
			System.out.println("Floor event " + floorEvent + "Received by the floor subsystem.");
		}
	}
	
	/***
	 * generates floor events using the parser file.
	 * @return the parsed floor event from the text file
	 * @throws ParseException
	 */
	public FloorEvent generateFloorEvent() throws ParseException {
		return p.parseFile();	
	}
}

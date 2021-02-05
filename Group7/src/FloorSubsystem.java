/***
 * The Floor Subsystem is responsible for reading the requests from
 *a textfile and sending these requests to the network.
 * @author lynnmehyou
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
	
	
	public synchronized void run() {
		while(true) {
			try {
				network.schedToFloorSystem(generateFloorEvent(), 1);
				System.out.println("FloorSystem sent to Scheduler");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	/***
	 * generates floor events using the parser file.
	 * @return
	 * @throws ParseException
	 */

	public FloorEvent generateFloorEvent() throws ParseException {
		return p.parseFile();
		
	}
}

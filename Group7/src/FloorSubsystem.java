package sysc3303proj;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class FloorSubsystem implements Runnable{
	private Network network;
	private List<Floor> floors;
	private Parser p;
	
	public FloorSubsystem(int numberOfFloors, Network network) {
		this.network = network;
		this.floors = new ArrayList<Floor>(numberOfFloors);
	}
	
	public void generateFloors(int numberOfFloors) {
		this.floors = new ArrayList<Floor>(numberOfFloors);
	}
	
	public synchronized void run() {
		while(true) {
			try {
				network.schedToFloorSystem(generateFloorEvent(), 1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public FloorEvent generateFloorEvent() throws ParseException {
		return p.parseFile();
		
	}
}

import java.util.ArrayList;
/**
 * Scheduler class is a connection point that connects the Floor subsystem to the 
 * Elevator subsystem and back again.
 * 
 * @author Aaron Gabor, Marc Angers
 * @version 1.0.2
 */
public class Scheduler extends Thread implements Runnable {
	private Network network;
	private ArrayList<FloorEvent> elevatorRequests;
 
	/**
	 * Scheduler is a constructor that creates the thread.
	 * 
	 * @param network a Network object used to connect all variable together
	 */
	public Scheduler(Network network)
	{
		this.network = network;
		this.elevatorRequests = new ArrayList<FloorEvent>();
	}
 
	/**
	 * run is a method that will run the thread.
	 */
	public void run()
	{
		FloorEvent floorEvent;
		
		while(true)
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			handleFloorSystemEvent();
			
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			handleElevatorSystemEvent();
		}
	}
 
	/**
	 * handleElevatorEvent is a method that will tell the Elevator subsystem that 
	 * there is work to be done and then will remove the FloorEvent object from the
	 * ArrayList.
	 */
	public void handleFloorSystemEvent()
	{
		FloorEvent floorEvent = this.network.getFloorSystemEvent();
		this.elevatorRequests.add(floorEvent);
		System.out.println("Event added to scheduler queue");
		
		this.network.putSchedulerSystemEvent(floorEvent);
	}
	
	/**
	 * handleElevatorEvent is a method that will tell the Elevator subsystem that 
	 * there is work to be done and then will remove the FloorEvent object from the
	 * ArrayList.
	 */
	public void handleElevatorSystemEvent()
	{
		FloorEvent floorEvent = this.network.getElevatorSystemEvent();
		this.elevatorRequests.add(floorEvent);
		System.out.println("Event added to scheduler queue");
		
		this.network.putSchedulerSystemEvent(floorEvent);
	}
}

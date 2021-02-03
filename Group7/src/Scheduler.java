import java.util.ArrayList;
/**
 * Scheduler class is a connection point that connects the Floor subsystem to the 
 * Elevator subsystem and back again.
 * 
 * @author Aaron Gabor
 * @version 1.0.0
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
		while(true)
		{
			this.elevatorRequests.add(this.network.floorSystemToSched(null, 1));
			while(this.elevatorRequests.size() > 0)
			{
				this.network.schedToFloorSystem(this.elevatorRequests.get(0), 1);
				handleElevatorEvent();
			}
		}
	}
	
	/**
	 * handleElevatorEvent is a method that will tell the Elevator subsystem that 
	 * there is work to be done and then will remove the FloorEvent object from the
	 * ArrayList.
	 */
	public void handleElevatorEvent()
	{
		this.network.schedToElevatorSystem(this.elevatorRequests.get(0), 1);
		this.elevatorRequests.remove(0);
	}
	
}

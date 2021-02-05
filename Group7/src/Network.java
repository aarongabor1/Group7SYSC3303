/** 
 * Network class is the connection point of the whole project. All of the information that each
 * subsystem would like to send to another subsystem comes across the Network class. 
 *  
 * 
 * @author Aaron Gabor, Marc Angers
 * @version 1.1.1
 */
public class Network {
	private FloorEvent floorSystemEvent, schedulerSystemEvent, elevatorSystemEvent;
	private boolean containsFloorSystemEvent, containsSchedulerSystemEvent, containsElevatorSystemEvent;
	
	/**
	 * Constructor that will create the Network object.
	 */
	public Network()
	{
		this.floorSystemEvent = null;
		this.schedulerSystemEvent = null;
		this.elevatorSystemEvent = null;
		this.containsFloorSystemEvent = false;
		this.containsSchedulerSystemEvent = false;
		this.containsElevatorSystemEvent = false;
	}
	
	/** 
	 * floorSystemToSched is a method to transfer a FloorEvent object from the Floor subsystem to
	 * the Scheduler subsystem.
	 * 
	 * @param floorEvent is the FloorEvent object that needs to be transfered
	 * @param systemNumber the designated system number for the subsystem check key in class comment
	 * @return Either the FloorEvent object that needs to be transfered or null
	 */
	public synchronized void putFloorSystemEvent(FloorEvent floorEvent)
	{
		while(this.containsFloorSystemEvent)
		{
			try
			{
				wait();
			}
			catch(InterruptedException e)
			{
				System.err.print(e);
			}
		}

		this.containsFloorSystemEvent = true;
		this.floorSystemEvent = floorEvent;
		System.out.println("Floor system event added to the network");
		notifyAll();
	}
	
	/** 
	 * schedToFloorSystem is a method to transfer a FloorEvent object from the Scheduler subsystem
	 * and the Floor subsystem. 
	 *
	 * @param floorEvent is the FloorEvent object that needs to be transfered
	 * @param systemNumber the designated system number for the subsystem check key in class comment
	 * @return Either the FloorEvent object that needs to be transfered or null
	 */
	public synchronized FloorEvent getFloorSystemEvent()
	{
		//Waits if this in not the method that should have been entered
		while(!this.containsFloorSystemEvent)
		{
			try
			{
				wait();
			}
			catch(InterruptedException e)
			{
				System.err.print(e);
			}
		}

		this.containsFloorSystemEvent = false;
		FloorEvent event = this.floorSystemEvent;
		this.floorSystemEvent = null;
		System.out.println("Floor system event retreived from network");
		notifyAll();
		
		return event;
	}
	
	/** 
	 * floorSystemToSched is a method to transfer a FloorEvent object from the Floor subsystem to
	 * the Scheduler subsystem.
	 * 
	 * @param floorEvent is the FloorEvent object that needs to be transfered
	 * @param systemNumber the designated system number for the subsystem check key in class comment
	 * @return Either the FloorEvent object that needs to be transfered or null
	 */
	public synchronized void putSchedulerSystemEvent(FloorEvent floorEvent)
	{
		while(this.containsSchedulerSystemEvent)
		{
			try
			{
				wait();
			}
			catch(InterruptedException e)
			{
				System.err.print(e);
			}
		}

		this.containsSchedulerSystemEvent = true;
		this.schedulerSystemEvent = floorEvent;
		System.out.println("Scheduler system event added to the network");
		notifyAll();
	}
	
	/** 
	 * schedToFloorSystem is a method to transfer a FloorEvent object from the Scheduler subsystem
	 * and the Floor subsystem. 
	 *
	 * @param floorEvent is the FloorEvent object that needs to be transfered
	 * @param systemNumber the designated system number for the subsystem check key in class comment
	 * @return Either the FloorEvent object that needs to be transfered or null
	 */
	public synchronized FloorEvent getSchedulerSystemEvent()
	{
		//Waits if this in not the method that should have been entered
		while(!this.containsSchedulerSystemEvent)
		{
			try
			{
				wait();
			}
			catch(InterruptedException e)
			{
				System.err.print(e);
			}
		}

		this.containsSchedulerSystemEvent = false;
		FloorEvent event = this.schedulerSystemEvent;
		this.floorSystemEvent = null;
		System.out.println("Scheduler system event retreived from network");
		notifyAll();
		
		return event;
	}

	/**
	 * elevatorSystemToSched is a method that transfers a FloorEvent object from the Elevator
	 * subsystem to the Scheduler subsystem.
	 * 
	 * @param floorEvent is the FloorEvent object that needs to be transfered
	 * @param systemNumber the designated system number for the subsystem check key in class comment
	 * @return Either the FloorEvent object that needs to be transfered or null
	 */
	public synchronized FloorEvent putElevatorSystemEvent(FloorEvent floorEvent)
	{
		while(this.containsElevatorSystemEvent)
		{
			try
			{
				wait();
			}
			catch(InterruptedException e)
			{
				System.err.print(e);
			}
		}
		
		this.containsElevatorSystemEvent = true;
		this.elevatorSystemEvent = floorEvent;
		System.out.println("Elevator system event added to network");
		notifyAll();
		return null;
	}
	
	/**
	 * schedToElevatorSystem is a method that transfers a FloorEvent object from the Scheduler
	 * subsystem to the Elevator subsystem.
	 * 
	 * @param floorEvent is the FloorEvent object that needs to be transfered
	 * @param systemNumber the designated system number for the subsystem check key in class comment
	 * @return Either the FloorEvent object that needs to be transfered or null
	 */
	public synchronized FloorEvent getElevatorSystemEvent()
	{
		while(!this.containsElevatorSystemEvent)
		{
			try
			{
				wait();
			}
			catch(InterruptedException e)
			{
				System.err.print(e);
			}
		}

		FloorEvent event = this.elevatorSystemEvent;
		this.elevatorSystemEvent = null;
		this.containsElevatorSystemEvent = false;
		System.out.println("Elevator system event retreived from network");
		notifyAll();
		
		return event;
	}
}

/** 
 * Network class is the connection point of the whole project. All of the information that each
 * subsystem would like to send to another subsystem comes across the Network class. 
 *  
 * @author Aaron Gabor, Marc Angers
 * @version 1.1.1
 */
public class Scheduler {
	private FloorEvent floorSystemEvent, schedulerSystemEvent, elevatorSystemEvent;
	private boolean containsFloorSystemEvent, containsSchedulerSystemEvent, containsElevatorSystemEvent;
	
	/**
	 * Constructor that will create the Network object.
	 */
	public Scheduler()
	{
		this.floorSystemEvent = null;
		this.schedulerSystemEvent = null;
		this.elevatorSystemEvent = null;
		this.containsFloorSystemEvent = false;
		this.containsSchedulerSystemEvent = false;
		this.containsElevatorSystemEvent = false;
	}
	
	/** 
	 * putFloorSystemEvent is a method that it receive a FloorEvent object that the Floor System
	 * wants to send.
	 * 
	 * @param floorEvent is the FloorEvent object that needs to be transfered
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
		System.out.println("Floor system event: (" + floorSystemEvent + ") added to the network");
		notifyAll();
	}
	
	/** 
	 * getFloorSystemEvent is a method where the Floor system can get a FloorEvent object from another
	 * system.
	 *
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
		System.out.println("Floor system event: (" + event + ") retrieved by Scheduler");
		notifyAll();
		
		return event;
	}
	
	/** 
	 * putSchedulerSystemEvent is a method where the Scheduler system can send a FloorEvent object
	 * to another system
	 * 
	 * @param floorEvent is the FloorEvent object that needs to be transfered
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
		System.out.println("Event: (" + schedulerSystemEvent + ") Scheduler sent floor event to the network");
		notifyAll();
	}
	
	/** 
	 * getSchedulerSystemEvent is a method that the Scheduler system can receive a FloorEvent object
	 * from another system.
	 *
	 * @param floorEvent is the FloorEvent object that needs to be transfered
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
		System.out.println("Event: (" + event + ") Network retrieved floor event from Scheduler");
		notifyAll();
		
		return event;
	}

	/**
	 * putElevatorSystemEvent is a method that the Elevator system can send a FloorEvent object
	 * to another system.
	 * 
	 * @param floorEvent is the FloorEvent object that needs to be transfered
	 * @return null
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
		System.out.println("Event: (" + elevatorSystemEvent + ") Elevator system event sent to Scheduler");
		notifyAll();
		return null;
	}
	
	/**
	 * getElevatorSystemEvent is a method where the Elevator system can receive a FloorEvent object
	 * from another system.
	 * 
	 * @return Either the FloorEvent object that needs to be transfered or nullyjky6
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
		System.out.println("Event: (" + event + " )Elevator system event retreived by Scheduler");
		notifyAll();
		
		return event;
	}
}

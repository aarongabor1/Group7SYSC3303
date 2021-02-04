/** 
 * Network class is the connection point of the whole project. All of the information that each
 * subsystem would like to send to another subsystem comes across the Network class. 
 *  
 *  Key for the systemNumber variable: 
 *  	0 = Floor Subsystem
 *  	1 = Scheduler Subsystem
 *  	2 = Elevator Subsystem 
 * 
 * @author Aaron Gabor
 * @version 1.1.1
 */
public class Network {
	private FloorEvent floorEvent;
	private boolean needToWait1, needToWait2, needToWait3, needToWait4, needToWait5; //Allows the correct method to be triggered
	private int floor;
	
	/**
	 * Constructor that will create the Network object.
	 */
	public Network()
	{
		this.needToWait1 = false;
		this.needToWait2 = false;
		this.needToWait3 = false;
		this.needToWait4 = false;
		this.needToWait5 = false;
	}
	
	/** 
	 * schedToFloorSystem is a method to transfer a FloorEvent object from the Scheduler subsystem
	 * and the Floor subsystem. 
	 *
	 * @param floorEvent is the FloorEvent object that needs to be transfered
	 * @param systemNumber the designated system number for the subsystem check key in class comment
	 * @return Either the FloorEvent object that needs to be transfered or null
	 */
	public synchronized FloorEvent schedToFloorSystem(FloorEvent floorEvent, int systemNumber)
	{
		//Waits if this in not the method that should have been entered
		while(this.needToWait2 || this.needToWait3 || this.needToWait4 || this.needToWait5)
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
		//Runs if the Scheduler subsystem entered this method
		if(systemNumber == 1)
		{
			this.needToWait1 = true;
			this.floorEvent = floorEvent;
			notifyAll();
			return null;
		}
		//Runs if the Floor subsystem entered this method
		else if(systemNumber == 0)
		{
			this.needToWait1 = false;
			FloorEvent newFloorEvent = this.floorEvent;
			this.floorEvent = null;
			notifyAll();
			return newFloorEvent;
		}
		notifyAll();
		return null;
	}
	
	/** 
	 * floorSystemToSched is a method to transfer a FloorEvent object from the Floor subsystem to
	 * the Scheduler subsystem.
	 * 
	 * @param floorEvent is the FloorEvent object that needs to be transfered
	 * @param systemNumber the designated system number for the subsystem check key in class comment
	 * @return Either the FloorEvent object that needs to be transfered or null
	 */
	public synchronized FloorEvent floorSystemToSched(FloorEvent floorEvent, int systemNumber)
	{
		while(this.needToWait1 || this.needToWait3 || this.needToWait4 || this.needToWait5)
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
		//Runs if the Floor subsystem entered this method
		if(systemNumber == 0)
		{
			this.needToWait2 = true;
			this.floorEvent = floorEvent;
			notifyAll();
			return null;
		}
		//Runs if the Scheduler subsystem entered this method
		else if(systemNumber == 1)
		{
			this.needToWait2 = false;
			FloorEvent newFloorEvent = this.floorEvent;
			this.floorEvent = null;
			notifyAll();
			return newFloorEvent;
		}
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
	public synchronized FloorEvent schedToElevatorSystem(FloorEvent floorEvent, int systemNumber)
	{
		while(this.needToWait1 || this.needToWait2 || this.needToWait4 || this.needToWait5)
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
		//Runs if the Floor subsystem entered this method
		if(systemNumber == 1)
		{
			this.needToWait3 = true;
			this.floorEvent = floorEvent;
			notifyAll();
			return null;
		}
		//Runs if the Elevator subsystem entered this method
		else if(systemNumber == 2)
		{
			this.needToWait3 = false;
			FloorEvent newFloorEvent = this.floorEvent;
			this.floorEvent = null;
			notifyAll();
			return newFloorEvent;
		}
		notifyAll();
		return null;
	}
	
	/**
	 * elevatorSystemToSched is a method that transfers a FloorEvent object from the Elevator
	 * subsystem to the Scheduler subsystem.
	 * 
	 * @param floorEvent is the FloorEvent object that needs to be transfered
	 * @param systemNumber the designated system number for the subsystem check key in class comment
	 * @return Either the FloorEvent object that needs to be transfered or null
	 */
	public synchronized FloorEvent elevatorSystemToSched(FloorEvent floorEvent, int systemNumber)
	{
		while(this.needToWait1 || this.needToWait2 || this.needToWait3 || this.needToWait5)
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
		//Runs if the Elevator subsystem entered this method
		if(systemNumber == 2)
		{
			this.needToWait4 = true;
			this.floorEvent = floorEvent;
			notifyAll();
			return null;
		}
		//Runs if the Scheduler subsystem entered this method
		else if(systemNumber == 1)
		{
			this.needToWait4 = false;
			FloorEvent newFloorEvent = this.floorEvent;
			this.floorEvent = null;
			notifyAll();
			return newFloorEvent;
		}
		notifyAll();
		return null;
	}
	
	/**
	 * floorToElevatorSystem is a method that transfers an floor number from the Floor subsystem to
	 * the Elevator subsystem.
	 * 
	 * @param floor the floor number that needs to be transfered
	 * @param systemNumber the designated system number for the subsystem check key in class comment
	 * @return Either the floor number that needs to be transfered or -1
	 */
	public synchronized int floorToElevatorSystem(int floor, int systemNumber)
	{
		while(this.needToWait1 || this.needToWait2 || this.needToWait3 || this.needToWait4)
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
		//Runs if the Floor subsystem entered this method
		if(systemNumber == 0)
		{
			this.needToWait5 = true;
			this.floor = floor;
			notifyAll();
			return -1;
		}
		//Runs if the Elevator subsystem entered this method
		else if(systemNumber == 2)
		{
			this.needToWait5 = false;
			int newFloorEvent = this.floor;
			this.floor = -1;
			notifyAll();
			return newFloorEvent;
		}
		notifyAll();
		return -1;
	}
}

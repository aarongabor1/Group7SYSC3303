package Utilities;

import java.util.LinkedList;

import Events.DestinationUpdateEvent;
import Events.ElevatorMovementEvent;
import Floor.FloorEvent;

/** 
 * Network class is the connection point of the whole project. All of the information that each
 * subsystem would like to send to another subsystem comes across the Network class. 
 *  
 * @author Aaron Gabor, Marc Angers
 * @version 1.1.1
 */
public class Scheduler {
	private FloorEvent floorSystemEvent, schedulerSystemEvent;
	private ElevatorMovementEvent elevatorSystemEvent;
	
	private boolean containsFloorSystemEvent, containsSchedulerSystemEvent, containsElevatorSystemEvent;
	
	private LinkedList<FloorEvent> requestedElevators;
	
	private boolean isElevatorAtRequestedFloor;
	private boolean isElevatorAtPassengerFloor;
	
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
		this.isElevatorAtRequestedFloor = false;
		this.isElevatorAtPassengerFloor = false;
		this.requestedElevators = new LinkedList<>();
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
		
		requestedElevators.add(floorEvent);
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
	public synchronized DestinationUpdateEvent getFloorSystemEvent()
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
		
		return null;
	}
	
	/** 
	 * NOT USED
	 * 
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
	 * @param elevatorEvent is the FloorEvent object that needs to be transfered
	 * @return 
	 * @return null
	 */
	public synchronized void putElevatorSystemEvent(ElevatorMovementEvent elevatorEvent)
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
		
		monitorElevator(elevatorEvent); // Monitor elevator's location every time Scheduler receives info about it
		
		this.containsElevatorSystemEvent = true;
		this.elevatorSystemEvent = elevatorEvent;
		System.out.println("Event: (" + elevatorSystemEvent + ") Elevator system event sent to Scheduler");
		notifyAll();
	}
	
	/**
	 * Keeps track of elevator's location.
	 * 
	 * @param ev Data sent by the Elevator Subsystem
	 */
	public void monitorElevator(ElevatorMovementEvent ev) {
		System.out.println("Elevator is currently at floor " + ev.getCurrentFloor());
		
		for (FloorEvent e:requestedElevators) {	
			if (e.getFloor() == ev.getCurrentFloor()) {
				isElevatorAtRequestedFloor = true;
				System.out.println("Elevator is at requested floor #" + ev.getCurrentFloor());
			} else if (e.getFloor() == ev.getCurrentFloor()) {
				isElevatorAtPassengerFloor = true;
				System.out.println("Elevator is at floor #" + ev.getCurrentFloor());
				requestedElevators.remove(e); // Remove elevator request from the list if it has been completed
			} else {
				isElevatorAtRequestedFloor = false;
				isElevatorAtPassengerFloor = false;
				System.out.println("Elevator is approaching floor #" + ev.getCurrentFloor());
			}
		}
	}
	
	/**
	 * getElevatorSystemEvent is a method where the Elevator system can receive a FloorEvent object
	 * from another system.
	 * 
	 * @return Either the FloorEvent object that needs to be transfered or nullyjky6
	 */
	public synchronized ElevatorMovementEvent getElevatorSystemEvent()
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

		ElevatorMovementEvent event = this.elevatorSystemEvent;
		this.elevatorSystemEvent = null;
		this.containsElevatorSystemEvent = false;
		System.out.println("Event: (" + event + " )Elevator system event retreived by Scheduler");
		notifyAll();
		
		return event;
	}
	
}

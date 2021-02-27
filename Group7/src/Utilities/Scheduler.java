package Utilities;

import java.sql.Time;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import Events.*;

/** 
 * The scheduler class is the connection point of the whole project. All of the information that each
 * subsystem provides and consumes is organized and routed through this class. 
 *  
 * @author Aaron Gabor, Marc Angers
 * @version 1.1
 */
public class Scheduler implements Runnable {
	private FormattedEvent currentEventFromInput;
	
	private FloorButtonPressEvent floorButtonEvent;
	private ElevatorButtonPressEvent elevatorButtonEvent;
	private DestinationUpdateEvent destinationUpdateEvent;
	private ElevatorArrivalEvent elevatorArrivalEvent;
	
	private boolean containsFloorButtonEvent, containsElevatorButtonEvent, containsDestinationUpdateEvent, containsElevatorArrivalEvent;
		
	private Parser parser;
	private List<FloorButtonPressEvent> floorRequests;
	private List<ElevatorButtonPressEvent> elevatorRequests;
	
	private Time currentTime;
	//private Map<Integer, Integer> elevatorLocations; //something like this is probably needed right?
	
	/**
	 * Constructor that will create the Network object.
	 */
	public Scheduler()
	{
		floorButtonEvent = null;
		destinationUpdateEvent = null;
		elevatorArrivalEvent = null;
		
		containsFloorButtonEvent = false;
		containsDestinationUpdateEvent = false;
		containsElevatorArrivalEvent = false;

		parser = new Parser();

		floorRequests = new LinkedList<FloorButtonPressEvent>();
		elevatorRequests = new LinkedList<ElevatorButtonPressEvent>();
		//elevatorLocations = new HashMap<Integer, Integer>();
		
		currentTime = new Time(System.currentTimeMillis());
	}
	
	// Event storage and retrieval methods. Could put this in a separate class. --------------------------------
	/** 
	 * addFloorSystemEvent is a method that adds floor system events to the floor event queue.
	 * @param floorEvent is the event object that needs to be added
	 */
	public synchronized void addFloorButtonEvent(FloorButtonPressEvent floorEvent)
	{		
		while(containsFloorButtonEvent)
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
		
		containsFloorButtonEvent = true;
		floorButtonEvent = floorEvent;
		
		notifyAll();
	}
	
	/**
	 * getFloorButtonEvent is a method that provides the stored floor button press event.
	 * @return the most recent floor button press event
	 */
	public synchronized FloorButtonPressEvent getFloorButtonEvent() {
		while(!containsFloorButtonEvent)
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
		
		containsFloorButtonEvent = false;
		FloorButtonPressEvent tempEvent = floorButtonEvent;
		floorButtonEvent = null;
		
		notifyAll();
		
		return tempEvent;
	}
	
	/**
	 * addElevatorButtonEvent is a method that gets the stored elevator button press event.
	 */
	public synchronized void addElevatorButtonEvent(ElevatorButtonPressEvent elevatorEvent)
	{		
		while(containsElevatorButtonEvent)
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
		
		containsElevatorButtonEvent = true;
		elevatorButtonEvent = elevatorEvent;
		
		notifyAll();
	}
	
	/**
	 * getElevatorButtonEvent is a method that provides the stored elevator button press event.
	 * @return the most recent elevator button press event
	 */
	public synchronized ElevatorButtonPressEvent getElevatorButtonEvent() {
		while(!containsElevatorButtonEvent)
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
		
		containsElevatorButtonEvent = false;
		ElevatorButtonPressEvent tempEvent = elevatorButtonEvent;
		elevatorButtonEvent = null;
		
		notifyAll();
		
		return tempEvent;
	}
	
	/** 
	 * addElevatorArrivalEvent adds an event that the scheduler holds for the floor subsystem to consume.
	 */
	public synchronized void addElevatorArrivalEvent(ElevatorArrivalEvent elevatorEvent)
	{		
		while(containsElevatorArrivalEvent)
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
		
		/**
		for (FloorButtonPressEvent e : floorRequests) {
			if (e.floor == elevatorEvent.floorNumber) {
				floorRequests.remove(e);
			}
				
		}
		*/
		
		System.out.println("addElevatorArrivalEvent has been called.");
		containsElevatorArrivalEvent = true;
		elevatorArrivalEvent = elevatorEvent;
		
		//removeInitiatingEventFor(elevatorEvent);
		
		notifyAll();
	}
	
	/**
	 * getElevatorArrivalEvent is a method that provides the stored elevator arrival event.
	 * @return the most recent elevator arrival event
	 */
	public synchronized ElevatorArrivalEvent getElevatorArrivalEvent() {
		while(!containsElevatorArrivalEvent)
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
		
		System.out.println("getElevatorArrivalEvent has been called.");
		
		containsElevatorArrivalEvent = false;
		ElevatorArrivalEvent tempEvent = elevatorArrivalEvent;
		elevatorArrivalEvent = null;
		
		notifyAll();
		
		return tempEvent;
	}
	
	/** 
	 * addDestinationUpdateEvent adds an event that the scheduler holds for the floor subsystem to consume.
	 */
	public synchronized void addDestinationUpdateEvent(DestinationUpdateEvent destinationEvent)
	{		
		while(containsDestinationUpdateEvent)
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
		
		containsDestinationUpdateEvent = true;
		destinationUpdateEvent = destinationEvent;
				
		notifyAll();
	}
	
	/**
	 * getDestinationUpdateEvent is a method that provides the stored destination update event.
	 * @return the most recent destination update event
	 */
	public synchronized DestinationUpdateEvent getDestinationUpdateEvent() {
		while(!containsDestinationUpdateEvent)
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
		
		containsDestinationUpdateEvent = false;
		DestinationUpdateEvent tempEvent = destinationUpdateEvent;
		destinationUpdateEvent = null;
		
		notifyAll();
		
		return tempEvent;
	}
	// End of event storage and retrieval. ---------------------------------------------------------------------
	
	// This method could be its own event generator class.
	/**
	 * Generates floor events using the parser file.
	 * @throws ParseException
	 */
	public void generateFloorEvent() throws ParseException {
		currentEventFromInput = parser.parseFile();
		FloorButtonPressEvent floorButtonEvent = new FloorButtonPressEvent(currentEventFromInput);
		
		floorRequests.add(floorButtonEvent);
		addFloorButtonEvent(floorButtonEvent);
		
		scheduleElevators("floor");
		
		System.out.println("Before loop: " + containsElevatorArrivalEvent);
		
		// Temporary way of waiting for the elevator to arrive at the requested floor.
		while(!containsElevatorArrivalEvent) 
			;
		
		// Debugging
		System.out.println("After loop: " + containsElevatorArrivalEvent);
		System.out.println("Elevator's turn to move to passenger's target floor.");
		
		ElevatorButtonPressEvent elevatorButtonEvent = new ElevatorButtonPressEvent(currentEventFromInput);
		
		elevatorRequests.add(elevatorButtonEvent);
		addElevatorButtonEvent(elevatorButtonEvent);
		
		scheduleElevators("elevator");
		
		System.out.println("Added passenger's requested floor");
		
		// Temporary way of waiting for the elevator to arrive at the requested floor.
		while(!containsElevatorArrivalEvent)
			;
	}
	
	// The main scheduling method.
	/**
	 * Method to organize the button press events and find the optimal elevator schedule.
	 * Once the optimal schedule is found, the elevators will be notified of updates to their destinations.
	 */
	public void scheduleElevators(String mode) { // <-- Temporary variable, this will need to be changed in the future!
		// This is the method where the elevator scheduling algorithm will go.
		// VERY IMPORTANT, VERY COMPLICATED!
		
		// Temporary placeholder for the algorithm:
		if (mode.equals("floor")) {
			FloorButtonPressEvent mostImportantEvent = floorRequests.get(0);
			addDestinationUpdateEvent(new DestinationUpdateEvent(getTime(), 1, mostImportantEvent.floor));
		} else if (mode.equals("elevator")) {
			ElevatorButtonPressEvent mostImportantEvent = elevatorRequests.get(0);
			addDestinationUpdateEvent(new DestinationUpdateEvent(getTime(), 1, mostImportantEvent.buttonNumber));
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				generateFloorEvent();
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
		}
	}
	
	// Get and set methods:
	public Time getTime() {
		currentTime = new Time(System.currentTimeMillis());
		return currentTime;
	}
	
}

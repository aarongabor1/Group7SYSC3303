package Scheduler;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Time;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Events.*;
import Utilities.Parser;

/** 
 * The scheduler class is the connection point of the whole project. All of the information that each
 * subsystem provides and consumes is organized and routed through this class. 
 *  
 * @author Aaron Gabor, Marc Angers, Diana Miraflor
 * @version 1.1
 */
public class Scheduler implements Runnable {
	private Thread elevatorMovementEventConsumer;
	private FormattedEvent currentEventFromInput;
		
	private Parser parser;
	private DatagramSocket sendSocket;
	private List<FloorButtonPressEvent> floorRequests;
	private List<ElevatorButtonPressEvent> elevatorRequests;
	private List<FormattedEvent> newElevatorRequests;
	
	private Time currentTime;
	private Map<Integer, Integer> elevatorLocations;
	private Map<Integer, Integer> elevatorDestinations;
	
	/**
	 * Constructor that will create the Network object.
	 */
	public Scheduler()
	{
		elevatorMovementEventConsumer = new Thread(new ElevatorMovementEventConsumer(this), "Elevator movement event consumer");
		parser = new Parser();
		
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

		floorRequests = new LinkedList<FloorButtonPressEvent>();
		elevatorRequests = new LinkedList<ElevatorButtonPressEvent>();
		newElevatorRequests = new LinkedList<FormattedEvent>();
		elevatorLocations = new HashMap<Integer, Integer>();
		elevatorLocations.put(1, 1);
		
		currentTime = new Time(System.currentTimeMillis());
	}
		
	// This method could be its own event generator class.
	/**
	 * Generates floor events using the parser file.
	 * @throws ParseException
	 */
	public void generateFloorEvent() throws ParseException {
		try {
			currentEventFromInput = parser.parseFile();
		} catch (ParseException pe) {
			return;
		}
		newElevatorRequests.add(currentEventFromInput);
		
		FloorButtonPressEvent floorButtonEvent = new FloorButtonPressEvent(currentEventFromInput);
		
		floorRequests.add(floorButtonEvent);
		// Send the event to the appropriate consumer.
		try {
			sendSocket.send(Parser.packageObject(floorButtonEvent));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		scheduleElevators("floor");
		
		// Temporary way of waiting for the elevator to arrive at the requested floor.
		for (FloorButtonPressEvent e : floorRequests) {
			while (e.floor != elevatorLocations.get(1))
				;
		}
		
		ElevatorButtonPressEvent elevatorButtonEvent = new ElevatorButtonPressEvent(currentEventFromInput);
		
		elevatorRequests.add(elevatorButtonEvent);
		// Send the event to the appropriate consumer.
		try {
			sendSocket.send(Parser.packageObject(elevatorButtonEvent));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		scheduleElevators("elevator");
		
		// Temporary way of waiting for the elevator to arrive at the requested floor.
		for (ElevatorButtonPressEvent e : elevatorRequests) {
			while (e.buttonNumber != elevatorLocations.get(1))
				;
		}
	}
	
	// The main scheduling method.
	/**
	 * Method to organize the button press events and find the optimal elevator schedule.
	 * Once the optimal schedule is found, the elevators will be notified of updates to their destinations.
	 */
	public void scheduleElevators(String mode) { // <-- Temporary variable, this will need to be changed in the future!
		// This is the method where the elevator scheduling algorithm will go.
		// VERY IMPORTANT, VERY COMPLICATED!
		
		DestinationUpdateEvent event;
		
		// Temporary placeholder for the algorithm:
		if (mode.equals("floor")) {
			FloorButtonPressEvent mostImportantEvent = floorRequests.get(0);
			// Update the elevator's destination within the scheduler.
			elevatorDestinations.put(1, mostImportantEvent.floor);
			// Send packet to destination update event consumer
			event = new DestinationUpdateEvent(getTime(), 1, mostImportantEvent.floor);
		} else {
			ElevatorButtonPressEvent mostImportantEvent = elevatorRequests.get(0);
			// Update the elevator's destination within the scheduler.
			elevatorDestinations.put(1, mostImportantEvent.buttonNumber);
			// Send packet to destination update event consumer
			event = new DestinationUpdateEvent(getTime(), 1, mostImportantEvent.buttonNumber);
		}
		
		// Send the event to the appropriate consumer.
		try {
			sendSocket.send(Parser.packageObject(event));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Method to update an elevators location within the scheduler.
	 * @param elevatorID, the elevator's location to update
	 * @param currentLocation, the floor that the elevator has moved to
	 */
	public void updateLocation(int elevatorID, int currentLocation) {
		elevatorLocations.put(elevatorID, currentLocation);
	}
	
	@Override
	public void run() {
		// Probably just want to start 2 other threads like the other subsystems.
		// TODO: Make an event generator thread that will send events to the scheduler.
		elevatorMovementEventConsumer.start();
		//int numOfRequestsFinished = 0;
		while (true) {
			try {
				generateFloorEvent();
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
			//numOfRequestsFinished++;
		}
	}
	
	// Get and set methods:
	public Time getTime() {
		currentTime = new Time(System.currentTimeMillis());
		return currentTime;
	}
	
}

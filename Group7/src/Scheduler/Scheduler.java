package Scheduler;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Time;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Elevator.Elevator;
import Elevator.ElevatorState;
import Events.*;
import Utilities.Direction;
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
	private Thread eventGenerator;
		
	private DatagramSocket sendSocket;
	private Map<Integer, ElevatorState> elevatorStates;
	private Map<Integer, List<Integer>> elevatorDestinations;
	
	private Time currentTime;
	
	/**
	 * Constructor that will create the Network object.
	 */
	public Scheduler()
	{
		elevatorMovementEventConsumer = new Thread(new ElevatorMovementEventConsumer(this), "Elevator movement event consumer");
		eventGenerator = new Thread(new EventGenerator(this), "Event generator");
		
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

		elevatorStates = new HashMap<Integer, ElevatorState>();
		elevatorDestinations = new HashMap<Integer, List<Integer>>();
		
		currentTime = new Time(System.currentTimeMillis());
	}
	
	// The main scheduling method.
	/**
	 * Method to organize the button press events and find the optimal elevator schedule.
	 * Once the optimal schedule is found, the elevators will be notified of updates to their destinations.
	 */
	public void scheduleEvent(FloorButtonPressEvent floorButtonPressEvent) { 	
	    
	    int bestElevator = getBestElevator(floorButtonPressEvent);
	    
	    // Send the destination update event to the elevator with the ID == bestElevator;
	    
		DestinationUpdateEvent event = new DestinationUpdateEvent(getTime(), 1, 1); // <-- Remove this!!!
		
		// Send the event to the appropriate consumer.
		try {
			sendSocket.send(Parser.packageObject(event));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Method to get the best elevator to service a new floor request
	 * 
	 * @param fe A new floor button press event (new floor request)
	 * @return ElevatorState
	 */
	public int getBestElevator(FloorButtonPressEvent fe) {
	    
	    ElevatorState bestElevator = null;
	    int bestElevatorID = 0;
	    
	    for (int i = 1; i <= elevatorStates.size(); i++) {
	        ElevatorState currentElevator = elevatorStates.get(i);
	        
	        /////// IF CURRENT ELEVATOR IS STATIONARY //////
	        
	        if (currentElevator.getDirection() == Direction.STATIONARY) {
	            if (currentElevator.getFloor() == fe.getFloor()) {
	                return i;
	            }
	            
	            if (Math.abs(currentElevator.getFloor() - fe.getFloor()) < Math.abs(bestElevator.getFloor() - fe.getFloor())) {
	                bestElevator = currentElevator;
                    bestElevatorID = i;
	            }
	        }
 	        
	        ////// IF CURRENT ELEVATOR IS MOVING //////
	        
	        if (currentElevator.getDirection() == fe.getDirection()) {
	            
	            if (bestElevator == null) {
	                bestElevator = currentElevator;
	                bestElevatorID = i;
	            }
 	            
	            if (currentElevator.getFloor() > fe.getFloor() && currentElevator.getDirection() == Direction.DOWN) {
	                if (Math.abs(currentElevator.getFloor() - fe.getFloor()) < Math.abs(bestElevator.getFloor() - fe.getFloor())) {
	                    bestElevator = currentElevator;
	                    bestElevatorID = i;
	                }	               
	            }
	            
	            if (currentElevator.getFloor() < fe.getFloor() && currentElevator.getDirection() == Direction.UP) {                   
	                if (Math.abs(currentElevator.getFloor() - fe.getFloor()) < Math.abs(bestElevator.getFloor() - fe.getFloor())) {
	                    bestElevator = currentElevator;
	                    bestElevatorID = i;
                    }                    
                }	            
	        }	        
	    }
	    
	   return bestElevatorID;
	}
	
	/**
	 * Overload of the scheduling method to deal with elevator button presses.
	 * @param elevatorButtonPressEvent
	 * @param elevatorID
	 */
	public void scheduleEvent(ElevatorButtonPressEvent elevatorButtonPressEvent, int elevatorID) {
	    List<Integer> destRequests = elevatorDestinations.get(elevatorID); 
	    destRequests.add(elevatorButtonPressEvent.buttonNumber);
	    Collections.sort(destRequests);
	    
	    
		DestinationUpdateEvent event = new DestinationUpdateEvent(getTime(), 1, 1); // <-- Remove this!!!
		
		// Send the event to the appropriate consumer.
		try {
			sendSocket.send(Parser.packageObject(event));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Method to add a new elevator to the elevatorStates and elevatorDestinations maps.
	 * @param newElevator
	 */
	public void registerNewElevator(Elevator newElevator) {
		elevatorStates.put(newElevator.ID, newElevator.getState());
		elevatorDestinations.put(newElevator.ID, new LinkedList<Integer>());
	}
	
	@Override
	public void run() {
		elevatorMovementEventConsumer.start();
		eventGenerator.start();
	}
	
	// Get and set methods:
	/**
	 * Method to update an elevators location within the scheduler.
	 * @param elevatorID, the elevator's location to update
	 * @param currentLocation, the floor that the elevator has moved to
	 */
	public void updateElevatorState(int elevatorID, ElevatorState state) {
		elevatorStates.put(elevatorID, state);
		
		// If the elevator is at its current destination, update the elevator's destination.
		if (elevatorDestinations.get(elevatorID).get(0) == state.getFloor())
			elevatorDestinations.get(elevatorID).remove(0);
		
		DestinationUpdateEvent event = new DestinationUpdateEvent(getTime(), elevatorID, elevatorDestinations.get(elevatorID).get(0));
		
		// Send the event to the appropriate consumer.
		try {
			sendSocket.send(Parser.packageObject(event));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public Map<Integer, ElevatorState> getElevatorStates() {
		return elevatorStates;
	}
	
	public Time getTime() {
		currentTime = new Time(System.currentTimeMillis());
		return currentTime;
	}
	
}

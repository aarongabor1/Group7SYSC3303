package Scheduler;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Time;
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
 * @version 1.2
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
	    // Find the best elevator to serve the floor request
		int bestElevator = mostConvenientElevator(floorButtonPressEvent);
		elevatorStates.get(bestElevator).updateRequests();
	    
		// Grab the current destination of the elevator
	    List<Integer> currentDestinations = elevatorDestinations.get(bestElevator);
	    int previousDestination = elevatorStates.get(bestElevator).getDestination();
	    if (currentDestinations.size() > 0)
	    	previousDestination = currentDestinations.get(0);
	    
	    // Add the new destination into the elevator's destination queue
	    addDestination(bestElevator, floorButtonPressEvent.floor);
	    
	    currentDestinations = elevatorDestinations.get(bestElevator);
	    int newDestination = elevatorStates.get(bestElevator).getDestination();
	    if (currentDestinations.size() > 0)
	    	newDestination = currentDestinations.get(0);
	    
	    if (newDestination != previousDestination) {
	    	// The current destination for the elevator should change, so generate a new DestinationUpdateEvent
			DestinationUpdateEvent event = new DestinationUpdateEvent(getTime(), bestElevator, newDestination);
			
			// Send the event to the appropriate consumer.
			try {
				sendSocket.send(Parser.packageObject(event));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
	    }
	}
	
	/**
	 * Overload of the scheduling method to deal with elevator button presses.
	 * @param elevatorButtonPressEvent
	 * @param elevatorID
	 */
	public void scheduleEvent(ElevatorButtonPressEvent elevatorButtonPressEvent, int elevatorID) {    
	    addDestination(elevatorID, elevatorButtonPressEvent.buttonNumber);
	    
	    int previousDestination = elevatorDestinations.get(elevatorID).get(0);
	    		
		int newDestination = elevatorDestinations.get(elevatorID).get(0);
	    if (newDestination != previousDestination) {
	    	// The current destination for the elevator should change, so generate a new DestinationUpdateEvent
			DestinationUpdateEvent event = new DestinationUpdateEvent(getTime(), elevatorID, newDestination);
			
			// Send the event to the appropriate consumer.
			try {
				sendSocket.send(Parser.packageObject(event));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
	    }
	}
	
	/**
	 * Method to get the best elevator to service a new floor request
	 * 
	 * @param fe A new floor button press event (new floor request)
	 * @return int ID of the best elevator
	 */
	public int mostConvenientElevator(FloorButtonPressEvent fe) {

        ElevatorState bestElevator = null;
        int bestElevatorID = 0;
        
        for (int i = 1; i <= elevatorStates.size(); i++) {
            ElevatorState currentElevator = elevatorStates.get(i);        
         
            if (currentElevator.getDirection() == Direction.STATIONARY) {
                
                if (bestElevator == null) {
                    bestElevator = currentElevator;
                    bestElevatorID = i;
                
                } else if (currentElevator.getFloor() == fe.getFloor()) {                
                    return i;
                
                } else if (Math.abs(currentElevator.getFloor() - fe.getFloor()) < Math.abs(bestElevator.getFloor() - fe.getFloor())) {
                    bestElevator = currentElevator;
                    bestElevatorID = i;
                } else if (currentElevator.getRequests() < bestElevator.getRequests()) {
                    
                    bestElevator = currentElevator;
                    bestElevatorID = i;
                       
                }
            }
               
            // If current elevator is moving
            else if (currentElevator.getDirection() == fe.getDirection() && (currentElevator.getFloor() - fe.getFloor() != 0)) {
                
                if (bestElevator == null) {
                    bestElevator = currentElevator;
                    bestElevatorID = i;
                }
                
                else if (currentElevator.getFloor() > fe.getFloor() && currentElevator.getDirection() == Direction.DOWN) {              
                        if (Math.abs(currentElevator.getFloor() - fe.getFloor()) < Math.abs(bestElevator.getFloor() - fe.getFloor())) {
                            bestElevator = currentElevator;
                            bestElevatorID = i;
                        }                          
                }
                
                else if (currentElevator.getFloor() < fe.getFloor() && currentElevator.getDirection() == Direction.UP) {             
                    if (Math.abs(currentElevator.getFloor() - fe.getFloor()) < Math.abs(bestElevator.getFloor() - fe.getFloor())) {
                        bestElevator = currentElevator;
                        bestElevatorID = i;
                    }   
                }               
            } 
            
            // Checks the case where all the elevators are above or below the current floor
            else if (allElevatorsAbove(fe) || allElevatorsBelow(fe) && currentElevator.getFloor() != fe.getFloor()) {
               
                if (bestElevator == null) {
                    bestElevator = currentElevator;
                    bestElevatorID = i;
                }
                
                else if (!(fe.getFloor() < currentElevator.getFloor() && fe.getFloor() > currentElevator.getDestination()) 
                    || !(fe.getFloor() > currentElevator.getFloor() && fe.getFloor() < currentElevator.getDestination())) {
                    
                    if (Math.abs(currentElevator.getDestination() - fe.getFloor()) < Math.abs(bestElevator.getDestination() - fe.getFloor())){             
                           bestElevator = currentElevator;
                           bestElevatorID = i;                    
                    }
                }
            }
            
            //System.out.println("Current best elevator: " + bestElevatorID);    
        }
        
       return bestElevatorID;
	}
	
	/**
	 * Checks if all the elevators are currently above the requested floor
	 * 
	 * @param fe
	 * @return
	 */
	 public boolean allElevatorsAbove(FloorButtonPressEvent fe) {
	     for (int i = 1; i <= elevatorStates.size(); i++) {
	         if (elevatorStates.get(i).getFloor() < fe.getFloor()) {
	             return false;               
	         }
	     }
	     return true;
	 }
	
	 /**
	  * Checks if all the elevators are currently below the requested floor
	  * 
	  * @param fe
	  * @return
	  */
	public boolean allElevatorsBelow(FloorButtonPressEvent fe) {
	    for (int i = 1; i <= elevatorStates.size(); i++) {
	        if (elevatorStates.get(i).getFloor() > fe.getFloor()) {
	            return false;
	        }
	    }
	    return true;
	}
	
	/**
	 * Method to add a destination to the destination list for a given elevator.
	 * @param elevatorID
	 * @param destinationFloor
	 */
	private void addDestination(int elevatorID, int destinationFloor) {
	    List<Integer> currentDestinations = elevatorDestinations.get(elevatorID);
	    
	    if (currentDestinations.size() == 0) {
            currentDestinations.add(destinationFloor);
            // OR should it be elevatorDestinations.get(elevatorID).add(destinationFloor)
            // Because the local variable currentDestinations is the list that is being mutated, not the list that is inside elevatorDestinations
	    }
	    
		// Loop through the destinations for the elevator, and determine if the new destination is "on the way".
		int previousDest = elevatorStates.get(elevatorID).getFloor();
		for (int i = 0; i < currentDestinations.size(); i ++) {
		    
		    // If the new destination is between the previous destination and the next destination, add it into the list at that location.
            // Return
            
		    if (elevatorStates.get(elevatorID).getDirection() == Direction.UP) {
		        if (currentDestinations.get(i) > destinationFloor && destinationFloor > previousDest) {
		            currentDestinations.add(i++, destinationFloor);
		        }
		    }
		    
		    if (elevatorStates.get(elevatorID).getDirection() == Direction.DOWN) {
		        if (currentDestinations.get(i) < destinationFloor && destinationFloor < previousDest) {
		            currentDestinations.add(i++, destinationFloor);
		        }
		    }
		    
		    // Set previousDest to nextDest.
		    previousDest = currentDestinations.get(i);
		}
		
		// If we have gotten to this point, there is nowhere that the new destination fits well, so add it at the end of the list.
		currentDestinations.add(destinationFloor);
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
		
		// If the elevator has nowhere to go, we don't need to update its destination.
		if (elevatorDestinations.get(elevatorID).size() <= 0)
			return;
		
		// If the elevator is at its current destination, update the elevator's destination.
		if (elevatorDestinations.get(elevatorID).get(0) == state.getFloor())
			elevatorDestinations.get(elevatorID).remove(0);
		
		if (elevatorDestinations.get(elevatorID).size() <= 0)
			return;
		
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

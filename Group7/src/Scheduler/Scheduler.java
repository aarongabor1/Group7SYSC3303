package Scheduler;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Elevator.Elevator;
import Elevator.ElevatorState;
import Events.*;
import Utilities.Direction;
import Utilities.Parser;
import Utilities.GUI;

/** 
 * The scheduler class is the connection point of the whole project. All of the information that each
 * subsystem provides and consumes is organized and routed through this class. 
 *  
 * @author Aaron Gabor, Marc Angers, Diana Miraflor
 * @version 1.2
 */
public class Scheduler implements Runnable {
	private Thread elevatorRegistrationEventConsumer;
	private Thread elevatorMovementEventConsumer;
	private Thread eventGenerator;
		
	private DatagramSocket sendSocket;
	private Map<Integer, ElevatorState> elevatorStates;
	private Map<Integer, List<Integer>> elevatorDestinations;
	
	private long startTime;
	private int elevatorCount;
	
	private GUI gui;
	
	/**
	 * Constructor that will create the Network object.
	 */
	public Scheduler()
	{
		elevatorRegistrationEventConsumer = new Thread(new ElevatorRegistrationEventConsumer(this), "Elevator registration event consumer");
		elevatorMovementEventConsumer = new Thread(new ElevatorMovementEventConsumer(this), "Elevator movement event consumer");
		eventGenerator = new Thread(new EventGenerator(this), "Event generator");
		gui = new GUI();
		
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

		elevatorStates = new HashMap<Integer, ElevatorState>();
		elevatorDestinations = new HashMap<Integer, List<Integer>>();
	
		elevatorCount = 0;
		startTime = System.currentTimeMillis();
	}
	
	// The main scheduling method.
	/**
	 * Method to organize the button press events and find the optimal elevator schedule.
	 * Once the optimal schedule is found, the elevators will be notified of updates to their destinations.
	 */
	public void scheduleEvent(FloorButtonPressEvent floorButtonPressEvent) { 
	    // Find the best elevator to serve the floor request
		int bestElevator = mostConvenientElevator(floorButtonPressEvent);
		System.out.println("Best Elevator: " + bestElevator + " for floor request " + floorButtonPressEvent.getFloor());
	    
		// Grab the current destination of the elevator
	    List<Integer> currentDestinations = elevatorDestinations.get(bestElevator);
	    int previousDestination = elevatorStates.get(bestElevator).getDestination();
	    
	    if (currentDestinations!=null) {
	        if (currentDestinations.size() > 0) {
	            previousDestination = currentDestinations.get(0);
	        }
	    }
	    
	    
	    // Add the new destination into the elevator's destination queue
	    if (elevatorStates.get(bestElevator).getFloor()!=floorButtonPressEvent.floor) {
	              
	        addDestination(bestElevator, floorButtonPressEvent.floor);
	    }
	    
	    currentDestinations = elevatorDestinations.get(bestElevator);
	    int newDestination = elevatorStates.get(bestElevator).getDestination();
	    if (currentDestinations.size() > 0)
	    	newDestination = currentDestinations.get(0);
	    
	    if (newDestination != previousDestination && !elevatorStates.get(bestElevator).isShutDown()) {
	        //System.out.println(bestElevator + ": " + floorButtonPressEvent.getFloor());
	        
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
	    //System.out.println("Floor event: " + bestElevator + ": " + elevatorDestinations.get(bestElevator));
	    }

	
	/**
	 * Overload of the scheduling method to deal with elevator button presses.
	 * @param elevatorButtonPressEvent
	 * @param elevatorID
	 */
	public void scheduleEvent(ElevatorButtonPressEvent elevatorButtonPressEvent, int elevatorID) { 
		
	    
	    int previousDestination = -1;
		if (elevatorDestinations.get(elevatorID).size() > 0)
	    	previousDestination = elevatorDestinations.get(elevatorID).get(0);
		
		addDestination(elevatorID, elevatorButtonPressEvent.buttonNumber);
		//System.out.println("Elevator button event: " + elevatorID + ": " + elevatorDestinations.get(elevatorID)); - DEBUGGING
	    	    		
		int newDestination = elevatorDestinations.get(elevatorID).get(0);
	    if (newDestination != previousDestination && !elevatorStates.get(elevatorID).isShutDown()) {
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
	 * Method to send a failure event to its corresponding subsystem.
	 * @param failureEvent
	 */
	public void sendFailure(FailureEvent failureEvent) {
		try {
			sendSocket.send(Parser.packageObject(failureEvent));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		//gui.setElevatorError(failureEvent.getElevator(), failureEvent.toString());
	}
   
    /**
	 * Method to get the best elevator to service a new floor request
	 * 
	 * @param fe, A new floor button press event (new floor request)
	 * @return The ID of the best elevator
	 */
	public int mostConvenientElevator(FloorButtonPressEvent fe) {
        ElevatorState bestElevator = null;
        int bestElevatorID = 0;
        
        for (int i = 1; i <= elevatorStates.size(); i++) {
            ElevatorState currentElevator = elevatorStates.get(i);        
         
            if (!currentElevator.isShutDown()) {
                
	            if (currentElevator.getDirection() == Direction.STATIONARY) {
	                
	                if (bestElevator == null) {
	                    bestElevator = currentElevator;
	                    bestElevatorID = i;
	                   
	                
	                } else if (currentElevator.getFloor() == fe.getFloor()) { 
	                    
	                    return i;
	                
	                // Checks if elevator that is stationary is the closest to the floor request
	                } else if (Math.abs(currentElevator.getFloor() - fe.getFloor()) < Math.abs(bestElevator.getFloor() - fe.getFloor())) {
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
	                
	                else if (elevatorDestinations.get(i).contains(fe.getFloor())) {
	                    return i;
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
	        }
        }
        return bestElevatorID;
	}
	
	/**
	 * Checks if all the elevators are currently above the requested floor
	 * 
	 * @param fe, the new floor request.
	 * @return whether or not all the elevators are currently above the new floor request.
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
	 * @param fe, the new floor request.
	 * @return whether or not all the elevators are currently below the new floor request.
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
	    boolean highestDestFloor = true;
        
	    synchronized (elevatorDestinations.get(elevatorID)) {
        List<Integer> currentDestinations = elevatorDestinations.get(elevatorID);
        
        if (currentDestinations.size() == 0) {
            currentDestinations.add(destinationFloor);
            return;
        }
        
        for (Integer i : currentDestinations) {
            if (i > destinationFloor) {
                highestDestFloor = false;
            }      
        }
        
        // Loop through the destinations for the elevator, and determine if the new destination is "on the way".
        int previousDest = elevatorStates.get(elevatorID).getFloor();
        
        for (int i = 0; i < currentDestinations.size(); i++) {
            if (!currentDestinations.contains(destinationFloor)) {             
                  
            // If the new destination is between the previous destination and the next destination, add it into the list at that location.            
            if (elevatorStates.get(elevatorID).getDirection() == Direction.UP) {
              
                if (currentDestinations.size() > 1 && 
                        currentDestinations.get(i) < destinationFloor && 
                        destinationFloor > previousDest &&
                        !highestDestFloor) {
                    currentDestinations.add(i++, destinationFloor);
                    
                    //System.out.println(elevatorID + " current destinations: " +  currentDestinations);
                    return;
                }   
                
                if (currentDestinations.size() == 1 
                        && destinationFloor > currentDestinations.get(i)
                        && previousDest < destinationFloor
                        && elevatorStates.get(elevatorID).getDestination() > destinationFloor){
                    currentDestinations.add(0, destinationFloor);
                   
                    return;
                }
                
                if (previousDest < destinationFloor && currentDestinations.get(i) > destinationFloor
                        && !currentDestinations.contains(destinationFloor)) {
                    currentDestinations.add(i, destinationFloor); 
                    
                    return;
                }
               
            }
            
            if (elevatorStates.get(elevatorID).getDirection() == Direction.DOWN) {
                if (currentDestinations.size() > 1 &&
                        currentDestinations.get(i) > destinationFloor && 
                        destinationFloor < previousDest &&
                        !highestDestFloor) {
                    currentDestinations.add(i++, destinationFloor);
                   
                    return;
                }
               
                
                if (currentDestinations.size() == 1 
                        && destinationFloor < currentDestinations.get(i)
                        && destinationFloor < previousDest
                        && elevatorStates.get(elevatorID).getDestination() < destinationFloor) {
                    currentDestinations.add(0, destinationFloor);
                    
                    return;
                }
                
                if (previousDest > destinationFloor && currentDestinations.get(i) < destinationFloor
                        && !currentDestinations.contains(destinationFloor)) {
                    currentDestinations.add(i, destinationFloor); 
                    
                    return;
                }
                
                
            }
            
            if (elevatorStates.get(elevatorID).getDirection() == Direction.STATIONARY) {
                
                if (previousDest < destinationFloor && currentDestinations.get(i) > destinationFloor
                        && !currentDestinations.contains(destinationFloor)) {
                    
                    if (currentDestinations.size() == 1 || i == 0) {
                        currentDestinations.add(0, destinationFloor);
                     
                        return;
                    } else {
                        currentDestinations.add(i, destinationFloor); 
                      
                        return;
                    }
                    
                }
                
                if (previousDest > destinationFloor && currentDestinations.get(i) < destinationFloor
                        && !currentDestinations.contains(destinationFloor)) {
                    
                    if (currentDestinations.size() == 1) {
                        currentDestinations.add(0, destinationFloor);
                
                        return;
                    } else {
                        if (i!=0) {
                            currentDestinations.add(i--, destinationFloor); 
                       
                            return;
                        } else {
                            currentDestinations.add(i, destinationFloor);                         
                            return;
                        }
                    }
                    
                }
            }
            
            // Set previousDest to nextDest.
            if (currentDestinations.size()!=0) {
                previousDest = currentDestinations.get(i);
            }
            }
        }
        
        if (!currentDestinations.contains(destinationFloor)) {
        // If we have gotten to this point, there is nowhere that the new destination fits well, so add it at the end of the list.      
        currentDestinations.add(destinationFloor);
        }
	    }
    }
	    
	
	/**
	 * Method to add a new elevator to the elevatorStates and elevatorDestinations maps.
	 * @param newElevator
	 */
	public void registerNewElevator(Elevator newElevator) {
		elevatorStates.put(newElevator.ID, newElevator.getState());
		elevatorDestinations.put(newElevator.ID, new LinkedList<Integer>());
		
		elevatorCount++;
	}
	
	/**
	 * Transfers an elevator's floor events to the most convenient elevator
	 * @param elevatorID The ID of the elevator with the hard fault
	 */
	public void transferFloorEvents(int elevatorID) {
        Map.Entry<Integer, List<Integer>> leastDestinations = null;

        if (elevatorDestinations.get(elevatorID) != null) {
            List<Integer> destinations = elevatorDestinations.get(elevatorID);
            // Choose elevator with least destinations
            for (Map.Entry<Integer, List<Integer>> entry : elevatorDestinations.entrySet()) {
                if (!elevatorStates.get(entry.getKey()).isShutDown()) {
                    if (leastDestinations == null) {
                        leastDestinations = entry;
                    }

                    if (entry.getValue().size() < leastDestinations.getValue().size()) {
                        leastDestinations = entry;
                    }
                    
                    if (entry.getValue().size() == leastDestinations.getValue().size()) {
                        if (highestDestinationFloor(entry.getValue()) < highestDestinationFloor(leastDestinations.getValue())) {
                            leastDestinations = entry;
                        }
                    }
                }
            }

            // If the current floor of the stuck elevator is not in the destination list of
            // the chosen elevator, add to destination list
            if (!elevatorDestinations.get(leastDestinations.getKey())
                    .contains(elevatorStates.get(elevatorID).getFloor()) 
                    && elevatorStates.get(elevatorID).getFloor() != elevatorStates.get(leastDestinations.getKey()).getFloor()) {
                elevatorDestinations.get(leastDestinations.getKey()).add(elevatorStates.get(elevatorID).getFloor());
            }

            // Transfer floor events
            for (Integer i : destinations) {
                if (!leastDestinations.getValue().contains(i)) {
                    elevatorDestinations.get(leastDestinations.getKey()).add(i);
                }
            }
                  
            routeElevator(leastDestinations.getKey());
        }
	   
	}
	
	/**
	 * Returns the highest destination in a destination list
	 * 
	 * @param destList
	 * @return
	 */
	public int highestDestinationFloor(List<Integer> destList) {
	    int max = 0;
	    for (int i = 0; i < destList.size(); i++) {
           if (destList.get(i) > max) {
               max = destList.get(i);
           }
        }
	    return max;
	}
	
	/**
	 * Moves the elevator to where the elevator (with hard failure) is at
	 * 
	 * @param elevatorID
	 */
	public void routeElevator(int elevatorID) {
	    
	    // FIX THIS.
	    if (elevatorDestinations.get(elevatorID).size() != 0) {
            DestinationUpdateEvent event = new DestinationUpdateEvent(getTime(), elevatorID,
                    elevatorDestinations.get(elevatorID).get(0));
           
            try {
                sendSocket.send(Parser.packageObject(event));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
	}
	
	/**
	 * Method to update an elevators location within the scheduler.
	 * @param elevatorID, the elevator's location to update
	 * @param currentLocation, the floor that the elevator has moved to
	 */
	public void updateElevatorState(int elevatorID, ElevatorState state, boolean softFailure) {
	    //Debugging - System.out.println(elevatorID + ": " + elevatorDestinations.get(elevatorID));

	    elevatorStates.put(elevatorID, state);
		
		gui.updateState(elevatorID, elevatorStates.get(elevatorID), softFailure);
		
		 if (!state.isShutDown()) {     
            // If the elevator has nowhere to go, we don't need to update its destination.
            if (elevatorDestinations.get(elevatorID).size() <= 0)
                return;
            
            synchronized (elevatorDestinations) { // Should it be the Map or the List in the map
            // If the elevator is at its current destination, update the elevator's
            // destination.
            if (elevatorDestinations.get(elevatorID).get(0) == state.getFloor())
                elevatorDestinations.get(elevatorID).remove(0);
                elevatorDestinations.notifyAll(); // Do you need this?
            }

            if (elevatorDestinations.get(elevatorID).size() <= 0)
                return;

            DestinationUpdateEvent event = new DestinationUpdateEvent(getTime(), elevatorID,
                    elevatorDestinations.get(elevatorID).get(0));    

            // Send the event to the appropriate consumer.
            try {
                sendSocket.send(Parser.packageObject(event));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else {
        	// Hard failure event has occurred and the elevator must be shut down. So transfer any upcoming floor requests to a different elevator.
            transferFloorEvents(elevatorID);
            elevatorDestinations.remove(elevatorID);
        }
        //gui.updateState(elevatorID, elevatorStates.get(elevatorID), softFailure);
	}
	
	@Override
	public void run() {
		elevatorRegistrationEventConsumer.start();
		elevatorMovementEventConsumer.start();
		eventGenerator.start();
	}
	
	// Get and set methods:
	public synchronized Map<Integer, ElevatorState> getElevatorStates() {
		return elevatorStates;
	}
	public long getTime() {
		return System.currentTimeMillis() - startTime;
	}
	public int getElevatorCount() {
		return elevatorCount;
	}
	public Map<Integer, List<Integer>> getElevatorDestinations() {
	    return elevatorDestinations;
	}
	public boolean checkIfStationary()
	{
		if ((elevatorStates.get(1).getDirection() == Direction.STATIONARY) && (elevatorStates.get(2).getDirection() == Direction.STATIONARY)
				&& (elevatorStates.get(3).getDirection() == Direction.STATIONARY) && (elevatorStates.get(4).getDirection() == Direction.STATIONARY))
		{
			return true;
		}
		return false;
	}
}

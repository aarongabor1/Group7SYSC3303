package TestCases;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import Elevator.ElevatorState;
import Utilities.Direction;

public class AddDestinationTest {
    
    private Map<Integer, List<Integer>> elevatorDestinations;
    private Map<Integer, ElevatorState> elevatorStates;
   
    @Before
    public void setUp() throws Exception {
        elevatorDestinations = new HashMap<>();
        elevatorStates = new HashMap<>();
        
        ElevatorState e1state = new ElevatorState(2, Direction.UP, 7);
        ElevatorState e2state = new ElevatorState(5, Direction.UP, 10);
        ElevatorState e3state  = new ElevatorState(16, Direction.STATIONARY, 20);
        
        elevatorStates.put(1, e1state);
        elevatorStates.put(2, e2state);
        elevatorStates.put(3, e3state);
        
        
        LinkedList<Integer> elevator1dest = new LinkedList<>();
        LinkedList<Integer> elevator2dest = new LinkedList<>();
        LinkedList<Integer> elevator3dest = new LinkedList<>();
        LinkedList<Integer> elevator4dest = new LinkedList<>();
        
        elevator1dest.add(4);
        elevator1dest.add(7);
        
        elevator2dest.add(10);
        
        elevator3dest.add(20);
        elevator3dest.add(21);
        elevator3dest.add(22);
          
        elevatorDestinations.put(1, elevator1dest);
        elevatorDestinations.put(2, elevator2dest);
        elevatorDestinations.put(3, elevator3dest);
    }

    @Test
    public void test() {
        addDestination(3, 1);
        System.out.println(elevatorDestinations.get(3));
    }
    
private void addDestination(int elevatorID, int destinationFloor) {
        boolean highestDestFloor = true;
        
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
                    System.out.println("Here");
                    //System.out.println(elevatorID + " current destinations: " +  currentDestinations);
                    return;
                }   
                
                if (currentDestinations.size() == 1 
                        && destinationFloor > currentDestinations.get(i)
                        && previousDest < destinationFloor
                        && elevatorStates.get(elevatorID).getDestination() > destinationFloor) {
                    currentDestinations.add(0, destinationFloor);
                    System.out.println("Here2");
                    return;
                }
                
                if (previousDest < destinationFloor && currentDestinations.get(i) > destinationFloor
                        && !currentDestinations.contains(destinationFloor)) {
                    currentDestinations.add(i, destinationFloor); 
                    System.out.println("Here3");
                    return;
                }
               
            }
            
            if (elevatorStates.get(elevatorID).getDirection() == Direction.DOWN) {
                if (currentDestinations.size() > 1 &&
                        currentDestinations.get(i) > destinationFloor && 
                        destinationFloor < previousDest) {
                    currentDestinations.add(i++, destinationFloor);
                    return;
                }
               
                
                if (currentDestinations.size() == 1 
                        && destinationFloor < currentDestinations.get(i)
                        && destinationFloor < previousDest) {
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
                        System.out.println("yo");
                        return;
                    } else {
                        currentDestinations.add(i-1, destinationFloor); 
                        System.out.println("yo2");
                        return;
                    }
                    
                }
                
                if (previousDest > destinationFloor && currentDestinations.get(i) < destinationFloor
                        && !currentDestinations.contains(destinationFloor)) {
                    
                    if (currentDestinations.size() == 1 || i == 0) {
                        currentDestinations.add(0, destinationFloor);
                        return;
                    } else {
                        currentDestinations.add(i, destinationFloor); 
                        return;
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

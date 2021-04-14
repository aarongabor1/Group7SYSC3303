package TestCases;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import Elevator.ElevatorState;
import Utilities.Direction;
/**
 * A test class to see if a destination floor has been properly added to an elevator's
 * destination list.
 * 
 * @author Diana Miraflor
 *
 */
public class AddDestinationTest {
    
    private Map<Integer, List<Integer>> elevatorDestinations;
    private Map<Integer, ElevatorState> elevatorStates;
   
    @Before
    public void setUp() throws Exception {
        elevatorDestinations = new HashMap<>();
        elevatorStates = new HashMap<>();
        
        ElevatorState e1state = new ElevatorState(7, Direction.UP, 7);
        ElevatorState e2state = new ElevatorState(5, Direction.UP, 10);
        ElevatorState e3state  = new ElevatorState(16, Direction.STATIONARY, 20);
        
        elevatorStates.put(1, e1state);
        elevatorStates.put(2, e2state);
        elevatorStates.put(3, e3state);
        
        
        LinkedList<Integer> elevator1dest = new LinkedList<>();
        LinkedList<Integer> elevator2dest = new LinkedList<>();
        LinkedList<Integer> elevator3dest = new LinkedList<>();
        LinkedList<Integer> elevator4dest = new LinkedList<>();
        
        elevator1dest.add(1);
        elevator1dest.add(18);
        
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
        addDestination(1, 6);
        System.out.println(elevatorDestinations.get(1));
        List<Integer> correctList = Arrays.asList(1, 6, 18);
        assertEquals(elevatorDestinations.get(1), correctList);
    }
    
    /**
     * Algorithm to add destination floor to correct position
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
                  
            // CASE 1: Elevator going up         
            if (elevatorStates.get(elevatorID).getDirection() == Direction.UP) {
              
                // If list size is greater than 1 and floor being added is 
                // not in between the elevator's current floor
                // and the next destination floor add floor after last highest destination floor
                if (currentDestinations.size() > 1 && 
                        currentDestinations.get(i) < destinationFloor && 
                        destinationFloor > previousDest &&
                        !highestDestFloor) {
                    currentDestinations.add(i++, destinationFloor);
                    return;
                }   
                
                // If list is 1 and if current floor is between elevator current floor and
                // next dest in the list, add in front of list
                if (currentDestinations.size() == 1 
                        && destinationFloor > currentDestinations.get(i)
                        && previousDest < destinationFloor
                        && elevatorStates.get(elevatorID).getDestination() > destinationFloor){
                    currentDestinations.add(0, destinationFloor);
                   
                    return;
                }
                
                // If in between, add in between
                if (previousDest < destinationFloor && currentDestinations.get(i) > destinationFloor
                        && !currentDestinations.contains(destinationFloor)) {
                    currentDestinations.add(i, destinationFloor); 
                    return;
                }
               
            }
            
           
            // CASE 1: Elevator going down         
            if (elevatorStates.get(elevatorID).getDirection() == Direction.DOWN) {
                // If list size is greater than 1 and floor being added is 
                // not in between the elevator's current floor
                // and the next destination floor add floor after last lowest destination floor
                if (currentDestinations.size() > 1 &&
                        currentDestinations.get(i) > destinationFloor && 
                        destinationFloor < previousDest &&
                        !highestDestFloor) {
                    currentDestinations.add(i++, destinationFloor);
                   
                    return;
                }
               
                
                // If list is 1 and if current floor is between elevator current floor and
                // next dest in the list, add in front of list
                if (currentDestinations.size() == 1 
                        && destinationFloor < currentDestinations.get(i)
                        && destinationFloor < previousDest
                        && elevatorStates.get(elevatorID).getDestination() < destinationFloor) {
                    currentDestinations.add(0, destinationFloor);
                    
                    return;
                }
                
                // If in between, add in between
                if (previousDest > destinationFloor && currentDestinations.get(i) < destinationFloor
                        && !currentDestinations.contains(destinationFloor)) {
                    currentDestinations.add(i, destinationFloor); 
                    
                    return;
                }
                
                
            }
            
            // CASE 3: Stationary
            if (elevatorStates.get(elevatorID).getDirection() == Direction.STATIONARY) {
                
                // Dest in list is above current floor
                if (previousDest < destinationFloor && currentDestinations.get(i) > destinationFloor
                        && !currentDestinations.contains(destinationFloor)) {
                    
                    // Add in front if size is 1
                    if (currentDestinations.size() == 1) {
                        currentDestinations.add(0, destinationFloor); 
                        return;
                    } else {
                        // Add in between
                        currentDestinations.add(i, destinationFloor); 
                        return;
                    }
                    
                }
                
                // Dest in list is below current floor
                if (previousDest > destinationFloor && currentDestinations.get(i) < destinationFloor
                        && !currentDestinations.contains(destinationFloor)) {
                    
                    // Add in front if size is 1
                    if (currentDestinations.size() == 1) {
                        currentDestinations.add(0, destinationFloor);
                        return;
                    } else {
                        if (i!=0) {
                            //Add in front if current index is not 0
                            currentDestinations.add(i--, destinationFloor); 
                            return;
                        } else {
                            // Add in between
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
    }

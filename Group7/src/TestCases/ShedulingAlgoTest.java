package TestCases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Elevator.ElevatorState;
import Events.FloorButtonPressEvent;
import Utilities.Direction;

/**
 * A class to test the algorithm for Scheduler for picking the best elevator
 * to service a new floor request
 * 
 * @author Diana Miraflor, Aaron Gabor
 *
 */
class ShedulingAlgoTest {
    
    private Map<Integer, ElevatorState> elevatorStates;
    
    /**
     * Current states of the elevators
     */
    @BeforeEach
    void setUp() {
        elevatorStates = new HashMap<Integer, ElevatorState>();
        
        ElevatorState e1 = new ElevatorState(6, Direction.DOWN, 3);
        ElevatorState e2 = new ElevatorState(4, Direction.UP, 6);
        ElevatorState e3 = new ElevatorState(3, Direction.UP, 5);
    
        
        elevatorStates.put(1, e1);
        elevatorStates.put(2, e2);
        elevatorStates.put(3, e3);
    }

    /**
     * Tests the best elevator and prints the elevator ID
     */
    @Test
    void test() {
     
        FloorButtonPressEvent fe = new FloorButtonPressEvent(1000, 2, Direction.UP);      
        int bestElevator = mostConvenientElevator(fe);        
        assertEquals(3, bestElevator);
        FloorButtonPressEvent fe2 = new FloorButtonPressEvent(1000, 5, Direction.DOWN);      
        bestElevator = mostConvenientElevator(fe2);        
        assertEquals(1, bestElevator);
        FloorButtonPressEvent fe3 = new FloorButtonPressEvent(1000, 5, Direction.UP);      
        bestElevator = mostConvenientElevator(fe3);        
        assertEquals(2, bestElevator);
        
    }
    
    /*
     * The algorithm
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
                } 
                
                else if (currentElevator.getFloor() == fe.getFloor()) {                
                    return i;
                } 
                
                else if (Math.abs(currentElevator.getFloor() - fe.getFloor()) < Math.abs(bestElevator.getFloor() - fe.getFloor())) {
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
        }
        
       return bestElevatorID;
    }
    
    public boolean allElevatorsAbove(FloorButtonPressEvent fe) {
        for (int i = 1; i <= elevatorStates.size(); i++) {
            if (elevatorStates.get(i).getFloor() < fe.getFloor()) {
                return false;               
            }
        }
        return true;
    }
    
    public boolean allElevatorsBelow(FloorButtonPressEvent fe) {
        for (int i = 1; i <= elevatorStates.size(); i++) {
            if (elevatorStates.get(i).getFloor() > fe.getFloor()) {
                return false;
            }
        }
        return true;
    }

}

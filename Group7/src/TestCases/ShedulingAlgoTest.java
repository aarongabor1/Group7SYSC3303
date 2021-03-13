package TestCases;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
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
 * @author Diana Miraflor
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
        
        ElevatorState e1 = new ElevatorState(3, Direction.DOWN, 1);
        ElevatorState e2 = new ElevatorState(4, Direction.DOWN, 2);
        ElevatorState e3 = new ElevatorState(1, Direction.UP, 4);
    
        
        elevatorStates.put(1, e1);
        elevatorStates.put(2, e2);
        elevatorStates.put(3, e3);
    }

    /**
     * Tests the best elevator and prints the elevator ID
     */
    @Test
    void test() {
     
        FloorButtonPressEvent fe = new FloorButtonPressEvent(new Time(1000), 2, Direction.UP);      
        int bestElevator = getBestElevator(fe);        
        System.out.println("Elevator #" + bestElevator + " is the best elevator");
        
    }
    
    /*
     * The algorithm
     */
    public int getBestElevator(FloorButtonPressEvent fe) {
        
        ElevatorState bestElevator = null;
        int bestElevatorID = 0;
        
        for (int i = 1; i <= elevatorStates.size(); i++) {
            ElevatorState currentElevator = elevatorStates.get(i);
            
            /////// IF CURRENT ELEVATOR IS STATIONARY //////
            
            if (currentElevator.getDirection() == Direction.STATIONARY) {
                
                if (bestElevator == null) {
                    bestElevator = currentElevator;
                    bestElevatorID = i;
                }
                
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

}

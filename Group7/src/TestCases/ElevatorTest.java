package TestCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Elevator.Elevator;
import Utilities.Direction;

/**
 * A test class that tests the Elevator's movement between floors
 * @author Diana Miraflor
 *
 */
class ElevatorTest {
	Elevator elevator;
	
	@BeforeEach
	void setUp() {
		elevator = new Elevator();
	}
	
	/**
	 * Checks the number of the elevator's ground floor
	 */
	@Test
	void testGroundFloor() {
		assertEquals(Direction.STATIONARY, elevator.getCurrentDirection());
		assertEquals(1, elevator.getCurrentFloor());
	}
	
	/**
	 * Tests elevator moving up one floor
	 */
	@Test
	void testMoveOneFloor() {
		elevator.moveUp();
		assertEquals(2, elevator.getCurrentFloor());		
	}
	
	/**
	 * Tests elevator moving to destination floor
	 */
	@Test
	void testMoveTwoFloors() {
		 elevator.moveUp();
		 elevator.moveUp();
		 assertEquals(3, elevator.getCurrentFloor());
	}
	
	/**
	 * Checks if elevator's destination floor is updated
	 */
	@Test
	void testUpdateDestinationFloor() {
		elevator.updateDestination(4);
		assertEquals(4, elevator.getCurrentDestination());
	}
	
	/**
	 * Tests elevator moving to destination floor
	 */
	@Test
	void testMoveElevatorToDestinationFloor() {
		elevator.updateDestination(4);
		
		while(elevator.getCurrentDestination() != elevator.getCurrentFloor()) {
			if (elevator.getCurrentDestination() > elevator.getCurrentFloor()) {
				elevator.moveUp();
			}
			
			if (elevator.getCurrentDestination() < elevator.getCurrentFloor()) {
				elevator.moveUp();
			}
		}
		
		assertEquals(4, elevator.getCurrentFloor());
	}

}

package TestCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Elevator.Elevator;
import Elevator.ElevatorSubsystem;
import Utilities.Direction;
import Utilities.Scheduler;

/**
 * A test class that tests the Elevator's movement between floors
 * @author Diana Miraflor
 *
 */
class ElevatorTest {
	Elevator elevator;
	Scheduler scheduler;
	
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
	 */@Test
		void testMoveToDestinationFloor() {
			elevator.updateDestination(3);
			
			if (elevator.getCurrentDestination() < elevator.getCurrentFloor())
				elevator.changeDirection(Direction.UP);
			if (elevator.getCurrentDestination() > elevator.getCurrentFloor())
				elevator.changeDirection(Direction.DOWN);
			if (elevator.getCurrentDestination() == elevator.getCurrentFloor() && elevator.isMoving())
				elevator.changeDirection(Direction.STATIONARY);
			
			while (elevator.getCurrentFloor() != elevator.getCurrentDestination()) {
				if (elevator.getCurrentDirection() == Direction.UP) {
					elevator.moveUp();
				}
				
				if (elevator.getCurrentDirection() == Direction.DOWN) {
					elevator.moveDown();
				}
			}
			
			elevator.changeDirection(Direction.STATIONARY);
			assertEquals(Direction.STATIONARY, elevator.getCurrentDirection());
			assertEquals(3, elevator.getCurrentFloor());
			
		}

}

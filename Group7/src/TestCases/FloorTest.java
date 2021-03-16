package TestCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Floor.Floor;
import Utilities.Direction;

/**
 * @author Momin Mushtaha, Lynn Mehyou
 * @version 101114546
 * Test class for floor class methods
 **/
class FloorTest {
	Floor floor;
	
	@BeforeEach
	void setUp() {
		floor = new Floor(1);
	}

    /**
     * checks the floor number
     **/
	@Test
	void getFloorLevelTest() {
		assertEquals(1, floor.getFloorNumber());
	}
	
    /**
     * Tests the boolean if a button is pressed and moving in the right direction
     **/
	@Test
	void isGroundFloorButtonPressed() {
		floor.turnOnLamp(Direction.UP);
		assertEquals(true, floor.getUpButton().isPressed());
	}

}

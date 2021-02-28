package TestCases;

import Floor.Floor;
import Utilities.Direction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * @author Momin Mushtaha, Lynn Mehyou
 * @version 101114546
 * Test class for floor class methods
 **/
class FloorTest {
    /**
     * checks the floor number
     **/
    @Test
    public void getFloorLevelTest() {
        Floor floor = new Floor(1);
        assertEquals(floor.getFloorNumber(), 1);
    }

    /**
     * Tests the boolean if a button is pressed and moving in the right direction
     **/
    @Test
    public void isPressedTest() {
        Floor floor = new Floor(1);
        floor.turnOnLamp(Direction.UP);
        assertEquals(floor.getUpButton().isPressed(), true);
    }
    
}
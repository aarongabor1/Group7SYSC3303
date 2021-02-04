/**
 * Class Door is the door object in the elevator system
 *
 * @author Momin Mushtaha
 * @version 02 February 2021
 */
public class Door {
	private DoorPosition position;
	
/**
 * Constructor for Door class
 * @param startingPosition is the position of the door when the object was constructed 
 * Default is closed
 */
public Door(DoorPosition startingPosition)
{
	this.position = startingPosition;
}

/**
 * method closeDoor commands the door to close
 */
public void closeDoor()
{
	System.out.println(DoorPosition.CLOSED);
}

/**
 * method openDoor commands the door to open
 */
public void openDoor()
{	
	System.out.println(DoorPosition.OPEN);
}

}

package Utilities;

/**
 * Class Door is the door object in the elevator system
 *
 * @author Momin Mushtaha
 * @version 02 February 2021
 */
public class Door {
	private boolean state;
	
	/**
	 * Constructor for Door class
	 * @param startingPosition is the position of the door when the object was constructed 
	 * Default is closed
	 */
	public Door(boolean startingPosition)
	{
		this.state = startingPosition;
	}
	
	/**
	 * method closeDoor commands the door to close
	 */
	public void closeDoor()
	{
		try {
			Thread.sleep(Settings.TIME_TO_OPEN_OR_CLOSE_DOORS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		state = false;
	}
	
	/**
	 * method openDoor commands the door to open
	 */
	public void openDoor()
	{	
		try {
			Thread.sleep(Settings.TIME_TO_OPEN_OR_CLOSE_DOORS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		state = true;;
	}
	
	/**
	 * Method to tell if the door is currently open
	 * @return
	 */
	public boolean isOpen() {
		return state;
	}
}

package Utilities;

import java.io.Serializable;

import Elevator.*;

/**
 * The Motor class is responsible for moving the elevator when a request has been made
 *
 * @author Momin Mushtaha, Marc Angers
 * @version 1.2
 */
public class Motor implements Serializable {
	private static final long serialVersionUID = -4732599232139952812L;
	private Elevator parentElevator;
	
	public Motor(Elevator parent)
	{
		this.parentElevator = parent;
	}
	
	/**
	 * Moves the elevator in the direction indicated
	 * @param direction is the direction the motor is commanded to move the elevator
	 */
	public void moveElevator(Direction direction)
	{  
	    if (!parentElevator.getState().isShutDown()) {
		parentElevator.changeDirection(direction);
		try {
			Thread.sleep(Settings.TIME_TO_TRAVEL_BETWEEN_FLOORS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (direction == Direction.UP)
			parentElevator.moveUp();
		if (direction == Direction.DOWN)
			parentElevator.moveDown();
		
		System.out.println("Elevator #" + parentElevator.getID() + " current floor: " + parentElevator.getCurrentFloor());
	    }
	}
	
	/**
	 * Stops the elevator at the current floor.
	 */
	public void stopElevator()
	{
		System.out.println("Elevator #" + parentElevator.getID() + " stopped at floor " + parentElevator.getCurrentFloor());
		parentElevator.changeDirection(Direction.STATIONARY);
	}
}

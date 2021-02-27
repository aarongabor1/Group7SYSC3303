package Utilities;

import Elevator.*;

/**
 * The Motor class is responsible for moving the elevator when a request has been made
 *
 * @author Momin Mushtaha, Marc Angers
 * @version 1.1
 */
public class Motor {
	private Elevator parentElevator;
	private Direction state;
	
	
	
	public Motor(Elevator parent)
	{
		this.parentElevator = parent;
		this.state = Direction.STATIONARY;
	}
	
	/**
	 * Moves the elevator in the direction indicated
	 * @param direction is the direction the motor is commanded to move the elevator
	 */
	public void moveElevator(Direction direction)
	{
		System.out.println("Elevator going " + direction);
		state = direction;
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
	}
	
	/**
	 * Stops the elevator at the current floor.
	 */
	public void stopElevator()
	{
		System.out.println("Elevator Stopping");
		state = Direction.STATIONARY;
		parentElevator.changeDirection(Direction.STATIONARY);
	}
	
	// Get and set methods:
	public Direction getState() {
		return state;
	}
}

/**
 * Class Motor is responsible for moving the elevator when the a request has been made
 *
 * @author Momin Mushtaha
 * @version 02 February 2021
 */
public class Motor {
	private Direction state;
	
	/**
	 * Constructor method for Motor class
	 */
	public Motor(Direction direction)
	{
		this.state = direction;
		System.out.println("motor constructed");
	}
	
	/**
	 * Moves the elevator in the direction indicated
	 * @param direction is the direction the motor is commanded to move the elevator
	 */
	public void moveElevator(Direction direction)
	{
		System.out.println("Elevator going " + direction);
		state = direction;
	}
	
	/**
	 * Starts the process of stopping/deaccelerating the elevator to stop on the next floor
	 */
	public void stopElevator()
	{
		System.out.println("Start deaccelerating the elevator");
		// state = Direction.STATIONARY;
	}
	
	public Direction getState() {
		return state;
	}
}

/**
 * Class Motor is responsible for moving the elevator when the a request has been made
 *
 * @author Momin Mushtaha
 * @version 02 February 2021
 */
public class Motor {
	private Elevator parentElevator;
	private Direction state;
	
	/**
	 * Constructor method for Motor class
	 */
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
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		parentElevator.moveUp();
	}
	
	/**
	 * Starts the process of stopping/deaccelerating the elevator to stop on the next floor
	 */
	public void stopElevator()
	{
		System.out.println("Elevator Stopping");
		state = Direction.STATIONARY;
		parentElevator.changeDirection(Direction.STATIONARY);
	}
	
	public Direction getState() {
		return state;
	}
}

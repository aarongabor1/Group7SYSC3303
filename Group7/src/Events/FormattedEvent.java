package Events;

import Floor.Floor;
import Utilities.*;

/** 
 * FormattedEvent class which creates an object that stores the information needed for 
 * each elevator request.
 * 
 * @author Aaron Gabor, Marc Angers, Momin Mushataha
 * @version 1.2
 */
public class FormattedEvent {
	private long time;
	private int currentFloor;
	private Direction direction;
	private int carButton;
	
	public boolean isError;
	private String errorOccurred;
	private String errorType;
	private long errorDuration = 0;
	private int failingElevator;
	
	/**
	 * Constructor for the FloorEvent class, it will read all of the parameters
	 * and set the internal variable to equal the same value.
	 * 
	 * @param time an object of Time, currentFloor an integer value for the floor 
	 * where the request was made, direction a boolean value that states if the 
	 * elevator needs to go up or down, carButton is an integer value for the floor 
	 * where the user would like to travel to.
	 */
	public FormattedEvent(long time, int currentFloor, Direction direction, int carButton)
	{
		if (currentFloor < Floor.MINIMUM_FLOOR_NUM || currentFloor > Settings.NUMBER_OF_FLOORS)
			throw new IllegalArgumentException("An event cannot be generated for a floor that doesn't exist!");
		
		this.time = time;
		this.currentFloor = currentFloor;
		this.direction = direction;
		this.carButton = carButton;
	}

	/**
	 * FormattedEvent constructor for HardFailure errors
	 * @param errorOccurred is the type of error occurred in the system
	 */
	public FormattedEvent(long time, String errorType, String errorOccurred, int elevatorID)
	{
		isError = true;
		this.time = time;
		this.errorOccurred = errorOccurred;
		this.errorType = errorType;
		failingElevator = elevatorID;
	}
	
	/**
	 * FormattedEvent constructor for SoftFailure errors
	 * @param errorOccurred is the type of error occurred in the system
	 */
	public FormattedEvent(long time, String errorType, String errorOccurred, int elevatorID, long errorDuration)
	{
		isError = true;
		this.time = time;
		this.errorOccurred = errorOccurred;
		this.errorType = errorType;
		failingElevator = elevatorID;
		this.errorDuration = errorDuration;
	}
	
	/**
	 * getTime is a method where the time variable stored in the FloorEvent object
	 * is sent to different class.
	 * 
	 * @return returns a Time object
	 */
	public long getTime()
	{
		return this.time;
	}
	
	/**
	 * getCurrentFloor is a method where the currentFloor variable stored in the 
	 * FloorEvent object is sent to different class.
	 * 
	 * @return returns a integer value of the currentFloor
	 */
	public int getFloor()
	{
		return this.currentFloor;
	}
	
	/**
	 * getDirection is a method where the direction variable stored in the 
	 * FloorEvent object is sent to different class.
	 * 
	 * @return returns a boolean value of the direction variable
	 */
	public Direction getDirection()
	{
		return this.direction;
	}
	
	/**
	 * getCarButton is a method where the carButton variable stored in the 
	 * FloorEvent object is sent to different class.
	 * 
	 * @return returns an integer value of where the elevator wants to go
	 */
	public int getCarButton()
	{
		return this.carButton;
	}

	/**
	 * getErrorOccurred is a method to retrieve the errorOccurred String
	 * @return returns the errorOccurred String
	 */
	public String getErrorOccurred()
	{
		return this.errorOccurred;
	}

	/**
	 * getErrorType is a method to retrieve the errorType String
	 * @return returns the errorType String
	 */
	public String getErrorType() { 
		return this.errorType;
	}
	
	public int getElevator() {
		return this.failingElevator;
	}
	
	public long getDuration() {
		return this.errorDuration;
	}

	/**
	 * toString returns the a string version of all information stored in the FloorEvent object.
	 * 
	 * @return string containing all information.
	 */
	public String toString() {
		if (isError) {
			String result = time + ": " + errorType + " " + errorOccurred + " " + failingElevator;
			if (errorDuration > 0)
				result += " " + errorDuration;
			return result;
		} else
			return time + ": " + currentFloor + " " + direction + " " + carButton;
	}
}

package Events;

import Utilities.Direction;

/**
 * Event class to send information to the Elevator Subsystem from Scheduler
 * 
 * @author Diana Miraflor
 * @version 1.0.0
 */
public class SchedulerToElevatorEvent {
	private int passengerFloor;
	private int targetFloor;
	
	private Direction direction;
	
	public RequestType request;
	
	public enum RequestType {NEW_ELEVATOR_REQUEST, MOVE_ELEVATOR, STOP_ELEVATOR};
		
	public SchedulerToElevatorEvent(Direction direction, int floor, RequestType request) {
		this.direction = direction;
		this.targetFloor = floor;
		this.request = request;
	}

	public SchedulerToElevatorEvent(int floor, RequestType request) {
		this.passengerFloor = floor;
		this.request = request;
	}
	
	public int getPassengerFloor() {
		return passengerFloor;
	}
	
	public int getTargetFloor() {
		return targetFloor;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public RequestType getRequest() {
		return request;
	}
}

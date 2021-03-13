package Elevator;

import Utilities.Direction;

/**
 * Class containing information about the state of an elevator.
 * @author Marc Angers
 * @version 1.1
 */
public class ElevatorState {
	private int currentFloor;
	private Direction currentDirection;
	private int currentDestination;

	
	public ElevatorState(int floor, Direction direction, int destination) {
		currentFloor = floor;
		currentDirection = direction; 
		currentDestination = destination;
	}
	
	public boolean triggersElevatorButtonEvent(ElevatorState requiredState) {
		boolean satisfiesCurrentFloor = currentFloor == requiredState.getFloor();
		boolean satisfiesCurrentDirection = currentDirection == requiredState.getDirection() || currentDirection == Direction.STATIONARY;
		
		return satisfiesCurrentFloor && satisfiesCurrentDirection;
	}
	
	// Get and set methods:
	public int getFloor() {
		return currentFloor;
	}
	public Direction getDirection() {
		return currentDirection;
	}
	public int getDestination() {
		return currentDestination;
	}

	
	public void updateFloor(int floor) {
		currentFloor = floor;
	}
	public void updateDirection(Direction direction) {
		currentDirection = direction;
	}
	public void updateDestination(int destination) {
		currentDestination = destination;
	}
	
	
}

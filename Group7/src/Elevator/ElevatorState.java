package Elevator;

import java.io.Serializable;

import Utilities.Direction;

/**
 * Class containing information about the state of an elevator.
 * @author Marc Angers
 * @version 1.1
 */
public class ElevatorState implements Serializable {
	private static final long serialVersionUID = -1437296404849135721L;
	private int currentFloor;
	private Direction currentDirection;
	private int currentDestination;
	private volatile boolean shutDown;
	private int numRequests;
	
	public ElevatorState(int floor, Direction direction, int destination) {
		currentFloor = floor;
		currentDirection = direction; 
		currentDestination = destination;
		shutDown = false;
		numRequests = 0;
	}
	
	public boolean triggersElevatorButtonEvent(ElevatorState requiredState) {
		boolean satisfiesCurrentFloor = (currentFloor == requiredState.getFloor());
		boolean satisfiesCurrentDirection = (currentDirection == requiredState.getDirection()
		        || currentDirection == Direction.STATIONARY);
		        // currentDirection is needed because of elevators at ground floor
		
		boolean satisfiesStillAlive = shutDown == false;
		
		return satisfiesCurrentFloor && satisfiesCurrentDirection && satisfiesStillAlive;
	}
	
	// Get and set methods:
	public int getFloor() {
		return currentFloor;
	}
	public synchronized Direction getDirection() {
		return currentDirection;
	}
	public int getDestination() {
		return currentDestination;
	}
	public synchronized boolean isShutDown() {
	    return shutDown;
	}
	public void updateFloor(int floor) {
		currentFloor = floor;
	}
	public synchronized void updateDirection(Direction direction) {
		currentDirection = direction;
	}
	public void updateDestination(int destination) {
		currentDestination = destination;
		numRequests++;
	}	
	public void addNumRequest() {
	    numRequests++;
	}
	public int getNumRequests() {
	    return numRequests;
	}
	public synchronized void shutDown() {
	    shutDown = true;
	}
	public String toString() {
		return "State: " + currentFloor + ", " + currentDirection.toString() + ", " + currentDestination;
	}
}

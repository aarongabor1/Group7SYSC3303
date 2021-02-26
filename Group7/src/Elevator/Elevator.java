package Elevator;

import Utilities.*;
import Floor.Floor;
import java.util.LinkedList;
import java.util.List;

/**
 * Elevator class is for the ElevatorSubsytem class to control.
 * 
 * @author Diana Miraflor
 * @version 1.0
 *
 */
public class Elevator {	
	public int ID; // Initialize this somewhere
	private ElevatorSubsystem elevatorSubsystem;
	private Motor motor;
	private Door door;
	private int currentFloor;
	private Direction currentDirection;
	private boolean isMoving;
	
	/**
	 * Constructs a new Elevator object
	 * 
	 * @param numberOfFloors
	 */
	public Elevator(Scheduler network, int numberOfFloors) {
		if (numberOfFloors <= Floor.MINIMUM_FLOOR_NUM)
			throw new IllegalArgumentException("Your building must have more than 1 floor to use an elevator!");
		
		this.motor = new Motor(this);
		this.door = new Door(DoorPosition.CLOSED);
		this.currentFloor = Floor.MINIMUM_FLOOR_NUM;
		this.currentDirection = Direction.STATIONARY;
		
		this.elevatorSubsystem = new ElevatorSubsystem(network, this);
		
		this.isMoving = false;
	}
	
	/**
	 * Changes the direction the elevator is currently traveling.
	 * @param direction
	 */
	public void changeDirection(Direction direction) {
		currentDirection = direction;
		
		if (direction != Direction.STATIONARY) {
			isMoving = true;
		} else {
			isMoving = false;
		}
	}
	
	/**
	 * Moves elevator down
	 */
	public void moveDown() {
		currentFloor--;
	}
	
	/**
	 * Moves elevator up
	 */
	public void moveUp() {
		currentFloor++;
	}
	
	/**
	 * Returns the current floor the elevator is at
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	/**
	 * Returns elevator's door
	 * @return
	 */
	public Door getDoor() {
		return door;
	}
	
	/**
	 * Returns elevator's motor
	 * @return
	 */
	public Motor getMotor() {
		return motor;
	}
	
	/**
	 * Returns the elevator subsystem for the elevator
	 * @return
	 */
	public ElevatorSubsystem getElevatorSubsystem() {
		return elevatorSubsystem;
	}
	
	public Direction getCurrentDirection() {
		return currentDirection;
	}
	
	public int getElevatorID() {
		return ID;
	}
	
	public boolean isMoving() {
		return isMoving;
	}
}

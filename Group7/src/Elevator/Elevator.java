package Elevator;

import Utilities.*;
import Floor.Floor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Elevator class is for the ElevatorSubsytem class to control.
 * 
 * @author Diana Miraflor, Marc Angers
 * @version 1.1
 *
 */
public class Elevator {	
	public int ID;
	private int currentFloor;
	private Direction currentDirection;
	private int currentDestination;
	
	private ElevatorSubsystem elevatorSubsystem;
	private Motor motor;
	private Door door;
	
	private Map<Integer, ElevatorButton> elevatorButtons;
	
	
	
	public Elevator(Scheduler scheduler) {
		if (Settings.NUMBER_OF_FLOORS <= Floor.MINIMUM_FLOOR_NUM)
			throw new IllegalArgumentException("Your building must have more than 1 floor to use an elevator!");
		
		this.elevatorSubsystem = new ElevatorSubsystem(scheduler, this);
		this.motor = new Motor(this);
		this.door = new Door(false);
		this.elevatorButtons = new HashMap<Integer, ElevatorButton>();
		
		generateElevatorButtons();

		this.currentDestination = Floor.MINIMUM_FLOOR_NUM;
		this.currentFloor = Floor.MINIMUM_FLOOR_NUM;
		this.currentDirection = Direction.STATIONARY;
						
		ID = 1; // While there is only one elevator in the system, it is given an ID of 1. Will change this in later iterations.
	}
	
	/**
	 * Creates the elevator's floor buttons
	 */
	public void generateElevatorButtons() {
		for (int i = 1; i <= Settings.NUMBER_OF_FLOORS; i++) {
			elevatorButtons.put(i, new ElevatorButton(false, i));
		}
	}
	
	/**
	 * Function to turn on a specified button lamp.
	 * @param buttonNumber
	 */
	public void turnOnLamp(int buttonNumber) {
		elevatorButtons.get(buttonNumber).press();
	}
	
	/**
	 * Function to turn off a specified button lamp. 
	 * Lamp is turned off once elevator is at the lamp's corresponding floor number.
	 * 
	 * @param buttonNumber
	 */
	public void turnOffLamp(int buttonNumber) {
		elevatorButtons.get(buttonNumber).unPress();
	}
	
	/**
	 * Changes the direction the elevator is currently traveling.
	 * @param direction
	 */
	public void changeDirection(Direction direction) {
		currentDirection = direction;
	}
	
	/**
	 * Moves the elevator down
	 */
	public void moveDown() {
		currentFloor--;
	}
	
	/**
	 * Moves the elevator up
	 */
	public void moveUp() {
		currentFloor++;
	}
	
	/**
	 * Determines whether the elevator is currently moving or not.
	 * @return
	 */
	public boolean isMoving() {
		return currentDirection != Direction.STATIONARY;
	}
	
	/**
	 * Checks if any elevator button lamps are on. 
	 * @return
	 */
	public boolean haveLitElevatorLamps() {
		Collection<ElevatorButton> buttons = elevatorButtons.values();
	
		for (ElevatorButton b : buttons) {
			if (b.getButtonLamp()) {
				return true;
			}
		}
		
		return false;
	}

	// Get and set methods:
	public int getCurrentFloor() {
		return currentFloor;
	}
	public synchronized int getCurrentDestination() {
		return currentDestination;
	}
	public Direction getCurrentDirection() {
		return currentDirection;
	}
	public Door getDoor() {
		return door;
	}
	public Motor getMotor() {
		return motor;
	}
	public ElevatorSubsystem getElevatorSubsystem() {
		return elevatorSubsystem;
	}
	public synchronized void updateDestination(int newDestination) {
		currentDestination = newDestination;
	}
	public Map<Integer, ElevatorButton> getElevatorButtons() {
		return elevatorButtons;
	}
}

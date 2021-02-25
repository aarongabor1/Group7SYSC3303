package Elevator;

import Utilities.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ElevatorSubsystem is a class that controls one elevator
 * 
 * @author Diana Miraflor, Marc Angers
 * @version 1.0
 */
public class ElevatorSubsystem implements Runnable {
	private Scheduler scheduler;
	private Elevator parentElevator;
	private int numberOfFloors;
	private Map<Integer, ElevatorButton> elevatorButtons;
	
	private int currentDestination;
	
	/**
	 * Constructor for a new ElevatorSubsytem
	 * 
	 * @param elevator
	 * @param network
	 */
	public ElevatorSubsystem(Scheduler scheduler, Elevator parent) {
		this.scheduler = scheduler;
		this.parentElevator = parent;
		
		this.elevatorButtons = new HashMap<Integer, ElevatorButton>();
		this.numberOfFloors = numberOfFloors;

		generateElevatorButtons();
	}
	
	/**
	 * Creates the elevator's floor buttons
	 */
	public void generateElevatorButtons() {
		for (int i = 1; i <= numberOfFloors; i++) {
			elevatorButtons.put(i, new ElevatorButton(new ElevatorLamp(), i));
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			FloorEvent floorEvent = scheduler.getFloorSystemEvent();
			System.out.println("Event: (" + floorEvent + ") Elevator received FloorEvent from Scheduler");			
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Will most likely be a different kind of event with different information in the future, 
			// but for now just return the original floor event
			scheduler.putElevatorSystemEvent(floorEvent);
			System.out.println("Elevator sent FloorEvent to Scheduler");
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		
	}
	
	/**
	 * Moves motor, closes doors and moves elevator up or down according to the targeted floor.
	 * 
	 * @param direction
	 */
	public void moveElevator(Direction direction) {
		handleDoor(DoorPosition.CLOSED);
		handleMotor(direction);
	}
	
	/**
	 * Moves motor, stops elevator and opens doors.
	 */
	public void stopElevator() {
		handleMotor(Direction.STATIONARY);
		handleDoor(DoorPosition.OPEN);
	}
	
	/**
	 * Moves elevator in the appropriate direction
	 * 
	 * @param direction, the direction to move the elevator
	 */
	public void handleMotor(Direction direction) {
		if (direction == Direction.STATIONARY)
			parentElevator.getMotor().stopElevator();
		else {
			if (!parentElevator.getDoor().isOpen())
				parentElevator.getMotor().moveElevator(direction);
			else
				; // Will need to throw some sort of error here! Do not want the elevator to start moving if the doors are open!
		}
	}
	
	/**
	 * Closes door when elevator is moving,  opens door when elevator stops moving.
	 * 
	 * @param isMoving
	 */
	public void handleDoor(DoorPosition desiredPosition) {
		if (desiredPosition == DoorPosition.CLOSED) {
			parentElevator.getDoor().closeDoor();
		} else {
			parentElevator.getDoor().openDoor();
		}
	}
		
}

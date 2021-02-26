package Elevator;

import Utilities.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Events.DestinationUpdateEvent;
import Events.ElevatorMovementEvent;
import Floor.FloorEvent;

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
			
			DestinationUpdateEvent destinationFloorUpdate = scheduler.getFloorSystemEvent(); // Instead of FloorEvent, it should DestinationUpdateEvent
			System.out.println("Event: (" + destinationFloorUpdate + ") Elevator received FloorEvent from Scheduler");
			
			// New destination request has been received. Tells elevator to move to a new floor.
			if (parentElevator.getCurrentDirection() == Direction.STATIONARY && parentElevator.getCurrentFloor() != destinationFloorUpdate.getDestinationFloor()) {	
				handleDoor(DoorPosition.CLOSED); // Close doors
				
				if (parentElevator.getCurrentFloor() > destinationFloorUpdate.getDestinationFloor()) { // The floor where the elevator was requested
					handleMotor(Direction.UP); // Handle motor - elevator starts to go up
				} else {
					handleMotor(Direction.DOWN); // Handle motor - elevator starts to go down
				}	
				
			} else {
				// Information received by Scheduler while elevator is moving to its target floor.
				// Elevator should be informed when it reaches a floor so it should stop.
				if (destinationFloorUpdate.getDestinationFloor() == parentElevator.getCurrentFloor()) {
					handleMotor(Direction.STATIONARY);
					handleDoor(DoorPosition.OPEN);
				}
			}
					
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
					
			// Sends Scheduler info about Elevator's location
			ElevatorMovementEvent elevatorEvent = new ElevatorMovementEvent(parentElevator.getElevatorID(), parentElevator.getCurrentDirection(), parentElevator.getCurrentFloor());
			scheduler.putElevatorSystemEvent(elevatorEvent);
			System.out.println("Elevator sent Elevator Event to Scheduler");
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		
	}
	
	/**
	 * DELETE
	 * 
	 * Moves motor, closes doors and moves elevator up or down according to the targeted floor.
	 * 
	 * @param direction
	 */
	public void moveElevator(Direction direction) {
		handleDoor(DoorPosition.CLOSED);
		handleMotor(direction);
	}
	
	/**
	 * DELETE
	 * 
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

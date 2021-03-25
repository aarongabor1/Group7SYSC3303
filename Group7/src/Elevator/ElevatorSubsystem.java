package Elevator;

import Utilities.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import Events.*;

/**
 * ElevatorSubsystem is a class that controls one elevator
 * 
 * @author Diana Miraflor, Marc Angers
 * @version 1.2
 */
public class ElevatorSubsystem implements Runnable {	
	private Elevator parentElevator;
	private Thread destinationUpdateEventConsumer;
	private Thread elevatorButtonPressEventConsumer;
	private Thread errorEventConsumer;
	private DatagramSocket sendSocket;
	
	private boolean shutDown;
	
	private long startTime;
	
	public ElevatorSubsystem(Elevator parent) {
		parentElevator = parent;
		
		destinationUpdateEventConsumer = new Thread(new DestinationUpdateEventConsumer(this, parent.ID), "Destination update event consumer for elevator " + parent.ID);
		elevatorButtonPressEventConsumer = new Thread(new ElevatorButtonPressEventConsumer(this, parent.ID), "Elevator button press event consumer");
		errorEventConsumer = new Thread(new ErrorEventConsumer(this, parent.ID), "Error event consumer");
		
		startTime = System.currentTimeMillis();

		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		
		ElevatorRegistrationEvent initializationEvent = new ElevatorRegistrationEvent(getTime(), parent);
		
		try {
			sendSocket.send(Parser.packageObject(initializationEvent));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		this.shutDown = false;
	}
	
	/**
	 * Moves motor, closes doors and moves elevator up or down according to the targeted floor.
	 * 
	 * @param direction
	 */
	public void moveElevator(Direction direction) {
		handleDoor(false);
		handleMotor(direction);
	}
	
	/**
	 * Moves motor, stops elevator and opens doors.
	 */
	public void stopElevator() {
		handleMotor(Direction.STATIONARY);
		handleDoor(true);
		
		if (!shutDown) {
 		ElevatorArrivalEvent event = new ElevatorArrivalEvent(getTime(), parentElevator.getCurrentFloor(), parentElevator.ID, parentElevator.getCurrentDirection());
		// Send the event to the appropriate consumer.
		try {
			sendSocket.send(Parser.packageObject(event));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		}
		// Create and send an elevator movement event to the scheduler.
		ElevatorMovementEvent elevatorMovementEvent;
		elevatorMovementEvent = new ElevatorMovementEvent(getTime(), parentElevator.ID, parentElevator.getState(), shutDown);
		
		if (shutDown) {
		    parentElevator.shutDown();
		    elevatorMovementEvent = new ElevatorMovementEvent(getTime(), parentElevator.ID, parentElevator.getState(), true);
		}
		
		try {		   
			sendSocket.send(Parser.packageObject(elevatorMovementEvent));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
				
		parentElevator.turnOffLamp(parentElevator.getCurrentFloor());
	}
	
	/**
	 * Moves elevator in the appropriate direction
	 * 
	 * @param direction, the direction to move the elevator
	 */
	public void handleMotor(Direction direction) {
		System.out.println("ElevatorSubsystem: Handling Motor: " + direction);

		if (direction == Direction.STATIONARY)
			parentElevator.getMotor().stopElevator();
		else {
			if (!parentElevator.getDoor().isOpen()) {
				parentElevator.getMotor().moveElevator(direction);
				
				// Create and send an elevator movement event to the scheduler.
				ElevatorMovementEvent elevatorMovementEvent = new ElevatorMovementEvent(getTime(), parentElevator.ID, parentElevator.getState(), shutDown);
				
				try {
					sendSocket.send(Parser.packageObject(elevatorMovementEvent));
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			} else
				System.out.println("DOOR NOT OPERATING"); // Iter4 - Will need to throw some sort of error here! Do not want the elevator to start moving if the doors are open!
		}
	}

	/**
	 * Updates the elevator door to the desired position.
	 * 
	 * @param desiredPosition
	 */
	public void handleDoor(boolean desiredPosition) {
		if (desiredPosition == false) {
			parentElevator.getDoor().closeDoor();
		} else {
			parentElevator.getDoor().openDoor();
		}
	}
	
	/*
	 * Shuts down an elevator
	 */
	public void shutDownElevator() {
	    shutDown = true;
	    stopElevator();
	}

	@Override
	public void run() {
		destinationUpdateEventConsumer.start();
		elevatorButtonPressEventConsumer.start();
		errorEventConsumer.start();
		
		while(!isShutDown()) {
			if (parentElevator.getCurrentDestination() < parentElevator.getCurrentFloor())
				moveElevator(Direction.DOWN);
			if (parentElevator.getCurrentDestination() > parentElevator.getCurrentFloor())
				moveElevator(Direction.UP);
			if (parentElevator.getCurrentDestination() == parentElevator.getCurrentFloor() && parentElevator.isMoving())
				stopElevator();
		}
	}
	
	// Get and set methods:
	public Elevator getElevator() {
		return parentElevator;
	}
	public long getTime() {
		return System.currentTimeMillis() - startTime;
	}
	public boolean isShutDown() {
	    return shutDown;
	}
}

package Elevator;

import Utilities.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

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
	private Thread hardFailureEventConsumer;
	private Thread softFailureEventConsumer;
	private DatagramSocket sendSocket;
	
	private long startTime;
	
	public ElevatorSubsystem(Elevator parent) {
		parentElevator = parent;
		
		destinationUpdateEventConsumer = new Thread(new DestinationUpdateEventConsumer(this, parent.ID), "Destination update event consumer for elevator " + parent.ID);
		elevatorButtonPressEventConsumer = new Thread(new ElevatorButtonPressEventConsumer(this, parent.ID), "Elevator button press event consumer");
		hardFailureEventConsumer = new Thread(new HardFailureEventConsumer(this, parent.ID), "Hard failure event consumer");
		softFailureEventConsumer = new Thread(new SoftFailureEventConsumer(this, parent.ID), "Soft failure event consumer");
		
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

		ElevatorArrivalEvent event = new ElevatorArrivalEvent(getTime(), parentElevator.getCurrentFloor(), parentElevator.ID, parentElevator.getCurrentDirection());
		// Send the event to the appropriate consumer.
		try {
			sendSocket.send(Parser.packageObject(event));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		// Create and send an elevator movement event to the scheduler.
		ElevatorMovementEvent elevatorMovementEvent = new ElevatorMovementEvent(getTime(), parentElevator.ID, parentElevator.getState());
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
				ElevatorMovementEvent elevatorMovementEvent = new ElevatorMovementEvent(getTime(), parentElevator.ID, parentElevator.getState());
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
	
	@Override
	public void run() {
		destinationUpdateEventConsumer.start();
		elevatorButtonPressEventConsumer.start();
		hardFailureEventConsumer.start();
		softFailureEventConsumer.start();
		
		while(true) {
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
}

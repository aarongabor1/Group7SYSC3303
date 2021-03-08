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
 * @version 1.1
 */
public class ElevatorSubsystem implements Runnable {
	private Scheduler scheduler;
	private Elevator parentElevator;
	private Thread destinationUpdateEventConsumer;
	private Thread elevatorButtonPressEventConsumer;
	private DatagramSocket sendSocket;
	
	
	
	public ElevatorSubsystem(Scheduler scheduler, Elevator parent) {
		this.scheduler = scheduler;
		parentElevator = parent;
		destinationUpdateEventConsumer = new Thread(new DestinationUpdateEventConsumer(this), "Destination update event cosnumer");
		elevatorButtonPressEventConsumer = new Thread(new ElevatorButtonPressEventConsumer(this), "Elevator button press event consumer");
		
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
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

		ElevatorArrivalEvent event = new ElevatorArrivalEvent(scheduler.getTime(), parentElevator.getCurrentFloor(), parentElevator.ID, parentElevator.getCurrentDirection());
		// Send the event to the appropriate consumer.
		try {
			sendSocket.send(Parser.packageObject(event));
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
			if (!parentElevator.getDoor().isOpen())
				parentElevator.getMotor().moveElevator(direction);
			else
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
	public Scheduler getScheduler() {
		return scheduler;
	}
	public Elevator getElevator() {
		return parentElevator;
	}
}

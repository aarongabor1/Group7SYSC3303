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
 * @version 1.3
 */
public class ElevatorSubsystem implements Runnable {	
	private Elevator parentElevator;
	private Thread destinationUpdateEventConsumer;
	private Thread elevatorButtonPressEventConsumer;

	private Thread hardFailureEventConsumer;
	private Thread softFailureEventConsumer;
	private DatagramSocket sendSocket;
	
	private volatile boolean shutDown;
	private volatile boolean doorStuck;
	
	private long duration;
	
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
		
		this.shutDown = false;
		this.doorStuck = false;
		this.duration = 0;
	}
	
	/**
	 * Moves motor, closes doors and moves elevator up or down according to the targeted floor.
	 * 
	 * @param direction
	 */
	public void moveElevator(Direction direction) {
	    if (!isShutDown()) {
	        handleDoor(false);
	        handleMotor(direction);
	    }
	}
	
	/**
	 * Moves motor, stops elevator and opens doors.
	 */
	public void stopElevator() {
	    if (!isShutDown()) {
	        handleMotor(Direction.STATIONARY);
	        
	        if (!doorStuck) {
	            handleDoor(true);
	        }
		
	        ElevatorArrivalEvent event = new ElevatorArrivalEvent(getTime(), parentElevator.getCurrentFloor(),
                    parentElevator.ID, parentElevator.getCurrentDirection());         
       
	        // Send the event to the appropriate consumer.
	        try {
	            sendSocket.send(Parser.packageObject(event));
	        } catch (IOException e) {
	            e.printStackTrace();
	            System.exit(1);
	        }
    
	        schedulerSend(getTime(), shutDown, doorStuck, parentElevator.getState());
				
	        parentElevator.turnOffLamp(parentElevator.getCurrentFloor());
	   }

	}

	/**
	 * Moves elevator in the appropriate direction
	 * 
	 * @param direction, the direction to move the elevator
	 */
	public void handleMotor(Direction direction) {
        if (!isShutDown()) {
            System.out.println("ElevatorSubsystem " + parentElevator.getID() + ": Handling Motor: " + direction);

            if (direction == Direction.STATIONARY) {
                parentElevator.getMotor().stopElevator();
            } else {
                if (!parentElevator.getDoor().isOpen()) {
                    if (parentElevator.getCurrentDestination() != parentElevator.getCurrentFloor()) {                  
                        
                    parentElevator.getMotor().moveElevator(direction);
                    
                    schedulerSend(getTime(), shutDown, doorStuck, parentElevator.getState());
                    }
                } else {
                    System.out.println("DOOR NOT OPENING");
                }
            }
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
	
	/**
	 * Shuts down an elevator
	 */
	public synchronized void shutDownElevator() {
	    handleMotor(Direction.STATIONARY);
	    handleDoor(false);
	    shutDown = true;
	    
        parentElevator.shutDown();           
        
        schedulerSend(getTime(), shutDown, doorStuck, parentElevator.getState());
	}
	
	/**
	 * Stops an elevator and makes it sleep due to a soft failure
	 * 
	 * @param duration The duration an elevator is down
	 */
	public synchronized void handleSoftFailure(long dur) {
	    doorStuck = true;
	    duration = dur;
	    
	    handleMotor(Direction.STATIONARY);
        
        // Section that handles an elevator's door being stuck
        if (doorStuck) {
            schedulerSend(getTime(), shutDown, doorStuck, parentElevator.getState());
            
            try {
                Thread.sleep(duration); // Let the elevator be offline for some duration
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        doorStuck = false;
        handleDoor(true);  
	}
	
	/**
	 * Sends an elevator movement event to the scheduler whenever an elevator moves or stops.
	 * @param e
	 */
	public void schedulerSend(long time, boolean shutDownFlag, boolean doorStuckFlag, ElevatorState state) {
	    ElevatorMovementEvent elevatorMovementEvent;
	    
	    if (state.getFloor() == state.getDestination() && state.getDirection() != Direction.STATIONARY) {
	        state.updateDirection(Direction.STATIONARY);
	        
	        // Create and send an elevator movement event to the scheduler.
	        elevatorMovementEvent = new ElevatorMovementEvent(time, parentElevator.ID, state, shutDownFlag, doorStuckFlag);
	    
	        try {          
	            sendSocket.send(Parser.packageObject(elevatorMovementEvent));
	        } catch (IOException exception) {
	            exception.printStackTrace();
	            System.exit(1);
	        }
	        
	        return;
	    }
	    
	    // Create and send an elevator movement event to the scheduler.
	    elevatorMovementEvent = new ElevatorMovementEvent(time, parentElevator.ID, state, shutDownFlag, doorStuckFlag);
    
        try {          
            sendSocket.send(Parser.packageObject(elevatorMovementEvent));
        } catch (IOException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
	}

	@Override
	public void run() {
		destinationUpdateEventConsumer.start();
		elevatorButtonPressEventConsumer.start();

		hardFailureEventConsumer.start();
		softFailureEventConsumer.start();
		
        while (!shutDown) {
            if (parentElevator.getCurrentDestination() < parentElevator.getCurrentFloor() && !isShutDown())
                moveElevator(Direction.DOWN);
            if (parentElevator.getCurrentDestination() > parentElevator.getCurrentFloor() && !isShutDown())
                moveElevator(Direction.UP);
            if (parentElevator.getCurrentDestination() == parentElevator.getCurrentFloor() && parentElevator.isMoving())
                stopElevator();
        }
		
		System.out.println("Elevator " + parentElevator.getID() + " has been shut down.");
	}
	
	// Get and set methods:
	public Elevator getElevator() {
		return parentElevator;
	}
	public long getTime() {
		return System.currentTimeMillis() - startTime;
	}
	public synchronized boolean isShutDown() {
	    return shutDown;
	}
	public synchronized boolean isDoorStuck() {
	    return doorStuck;
	}
}

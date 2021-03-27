package Elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Events.SoftFailureEvent;
import Utilities.Direction;
import Utilities.Parser;
import Utilities.Settings;

/**
 * Class to update the elevator's destination by consuming the DestinationUpdateEvents provided by the scheduler.
 * @author Marc Angers
 * @version 1.1
 */
public class SoftFailureEventConsumer implements Runnable {
	private ElevatorSubsystem parent;
	private ElevatorState currentState;
	private DatagramSocket receiveSocket;
	
	public SoftFailureEventConsumer(ElevatorSubsystem elevatorSubsystem, int elevatorID) {
		parent = elevatorSubsystem;
		
		try {
			receiveSocket = new DatagramSocket(Settings.SOFT_FAILURE_ECP + elevatorID);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Function to consume the destination update event from the scheduler.
	 * @param
	 */
	public void consume(SoftFailureEvent softFailureEvent) {
		
	    // Wait until elevator stops to get failure
	    while(parent.getElevator().getState().getDirection()!=Direction.STATIONARY) 
	        ;
	    
	    /*
	    currentState = parent.getElevator().getState();
		long start = System.currentTimeMillis();
		long end = start + softFailureEvent.getDuration();
		
		while (System.currentTimeMillis() < end){
			parent.shutDownElevator();
		}*/
	    
	    System.out.println("Elevator #" + softFailureEvent.getElevator() + " door is stuck! Elevator is now offline");
		parent.handleSoftFailure(softFailureEvent.getElevator());

		//parent.getElevator().setState(currentState);

		//parent.handleSoftFailure(softFailureEvent.getDuration());
		try {
		    Thread.sleep(softFailureEvent.getDuration());
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		
		// Waits for elevator fix soft failure
		while(parent.getElevator().getState().getDirection()==Direction.STATIONARY) 
            ;
		System.out.println("Elevator #" + softFailureEvent.getElevator() + " is back online!");
	}
	
	@Override
	public void run() {
		DatagramPacket packet = new DatagramPacket(new byte[8191], 8191);
		
		while (true) {
			try {
				receiveSocket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			consume((SoftFailureEvent)Parser.unpackDatagram(packet));
		}
	}
}

package Elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Events.DestinationUpdateEvent;
import Utilities.Parser;

/**
 * Class to update the elevator's destination by consuming the DestinationUpdateEvents provided by the scheduler.
 * @author Marc Angers
 * @version 1.1
 */
public class DestinationUpdateEventConsumer implements Runnable {
	private ElevatorSubsystem parent;
	
	private DatagramSocket receiveSocket, sendSocket;
	
	public DestinationUpdateEventConsumer(ElevatorSubsystem elevatorSubsystem, int elevatorID) {
		parent = elevatorSubsystem;
		
		try {
			receiveSocket = new DatagramSocket(ElevatorSubsystem.elevatorSystemPorts.get(elevatorID));
			sendSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Function to consume the destination update event from the scheduler.
	 * @param destinationUpdateEvent
	 */
	public void consume(DestinationUpdateEvent destinationUpdateEvent) {
		if (destinationUpdateEvent.elevatorID != parent.getElevator().ID)
			// Send the event to the appropriate consumer.
			try {
				sendSocket.send(Parser.packageObject(destinationUpdateEvent));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		else {
			System.out.println("Elevator current floor: " + parent.getElevator().getCurrentFloor());
			parent.getElevator().updateDestination(destinationUpdateEvent.destinationFloor);
		}
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
			consume((DestinationUpdateEvent)Parser.unpackDatagram(packet));
		}
	}
}

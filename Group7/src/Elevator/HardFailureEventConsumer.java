package Elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Events.HardFailureEvent;
import Utilities.Parser;
import Utilities.Settings;

/**
 * Class to update the elevator's destination by consuming the DestinationUpdateEvents provided by the scheduler.
 * 
 * @author Marc Angers
 * @version 1.1
 */
public class HardFailureEventConsumer implements Runnable {
	private ElevatorSubsystem parent;
	
	private DatagramSocket receiveSocket;
	
	public HardFailureEventConsumer(ElevatorSubsystem elevatorSubsystem, int elevatorID) {
		parent = elevatorSubsystem;
		
		try {
			receiveSocket = new DatagramSocket(Settings.HARD_FAILURE_ECP + elevatorID);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Function to consume the destination update event from the scheduler.
	 * @param destinationUpdateEvent
	 */
	public void consume(HardFailureEvent hardFailureEvent) {
		System.out.println("Elevator #" + hardFailureEvent.getElevator() + " is stuck!");		
		parent.shutDownElevator();
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
			consume((HardFailureEvent)Parser.unpackDatagram(packet));
		}
	}
}

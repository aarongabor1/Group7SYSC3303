package Scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Events.ElevatorMovementEvent;
import Utilities.Parser;
import Utilities.Settings;

/**
 * Class to update the elevator's location within the scheduler by consuming the ElevatorMovementEvents.
 * @author Marc Angers
 * @version 1.0.0
 */
public class ElevatorMovementEventConsumer implements Runnable {
	private Scheduler parent;
	
	private DatagramSocket receiveSocket;
	
	public ElevatorMovementEventConsumer(Scheduler scheduler) {
		parent = scheduler;
		
		try {
			receiveSocket = new DatagramSocket(Settings.ELEVATOR_MOVEMENT_ECP);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Function to consume the elevator movement event from the elevator subsystem.
	 * @param destinationUpdateEvent
	 */
	public void consume(ElevatorMovementEvent elevatorMovementEvent) {
		parent.updateElevatorState(elevatorMovementEvent.elevatorID, elevatorMovementEvent.getElevatorState());
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
			consume((ElevatorMovementEvent)Parser.unpackDatagram(packet));
		}
	}
}

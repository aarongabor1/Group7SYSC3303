package Scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Events.ElevatorRegistrationEvent;
import Utilities.Parser;
import Utilities.Settings;

/**
 * Class to consume the elevator registration events from when the elevators are initialized.
 * 
 * @author Marc Angers
 * @version 1.0
 */
public class ElevatorRegistrationEventConsumer implements Runnable {
private Scheduler parent;
	
	private DatagramSocket receiveSocket;
	
	public ElevatorRegistrationEventConsumer(Scheduler scheduler) {
		parent = scheduler;
		
		try {
			receiveSocket = new DatagramSocket(Settings.ELEVATOR_REGISTRATION_ECP);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Function to consume the elevator registration event from the elevator subsystem.
	 * @param elevatorRegistrationEvent
	 */
	public void consume(ElevatorRegistrationEvent elevatorRegistrationEvent) {
		parent.registerNewElevator(elevatorRegistrationEvent.getElevator());
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
			consume((ElevatorRegistrationEvent)Parser.unpackDatagram(packet));
		}
	}
}

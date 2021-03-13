package Elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Events.ElevatorButtonPressEvent;
import Utilities.Parser;
import Utilities.Settings;

/**
 * Class to consume the elevator button press events generated by the scheduler.
 * @author Marc Angers
 * @version 1.1
 */
public class ElevatorButtonPressEventConsumer implements Runnable {
	private ElevatorSubsystem parent;
	private DatagramSocket receiveSocket;
	
	public ElevatorButtonPressEventConsumer(ElevatorSubsystem elevatorSubsystem) {
		parent = elevatorSubsystem;
		
		try {
			receiveSocket = new DatagramSocket(Settings.ELEVATOR_BUTTON_PRESS_ECP);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Method to consume the provided event.
	 */
	public void consume(ElevatorButtonPressEvent elevatorButtonEvent) {
		parent.getElevator().turnOnLamp(elevatorButtonEvent.buttonNumber);
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
			consume((ElevatorButtonPressEvent)Parser.unpackDatagram(packet));
		}
	}
}

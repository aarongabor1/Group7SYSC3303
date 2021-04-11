package Floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Events.ElevatorArrivalEvent;
import Utilities.Parser;
import Utilities.Settings;

/**
 * Class to consume the elevator arrival events provided by the scheduler.
 * 
 * @author Marc Angers
 * @version 1.1
 */
public class ElevatorArrivalEventConsumer implements Runnable {
	private FloorSubsystem parent;
	
	private DatagramSocket receiveSocket;
	
	public ElevatorArrivalEventConsumer(FloorSubsystem floorSubsystem) {
		parent = floorSubsystem;
		
		try {
			receiveSocket = new DatagramSocket(Settings.ELEVATOR_ARRIVAL_ECP);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Method to consume the provided event.
	 */
	public void consume(ElevatorArrivalEvent elevatorEvent) {
		parent.turnOffLampForFloor(elevatorEvent.floorNumber, elevatorEvent.goingInDirection);
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
			consume((ElevatorArrivalEvent)Parser.unpackDatagram(packet));
		}
	}
}

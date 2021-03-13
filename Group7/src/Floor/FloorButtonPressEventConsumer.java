package Floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Events.FloorButtonPressEvent;
import Utilities.Parser;
import Utilities.Settings;

/**
 * Class to consume the floor button press events that are provided by the scheduler.
 * @author Marc Angers
 * @version 1.1
 */
public class FloorButtonPressEventConsumer implements Runnable {
	private FloorSubsystem parent;
	private DatagramSocket receiveSocket;
	
	public FloorButtonPressEventConsumer(FloorSubsystem floorSubsystem) {
		parent = floorSubsystem;
		
		try {
			receiveSocket = new DatagramSocket(Settings.FLOOR_BUTTON_PRESS_ECP);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Method to consume the provided event.
	 */
	public void consume(FloorButtonPressEvent floorEvent) {
		parent.turnOnLampForFloor(floorEvent.floor, floorEvent.direction);
		System.out.println("New floor request: " + floorEvent.direction + " lamp is on");
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
			consume((FloorButtonPressEvent)Parser.unpackDatagram(packet));
		}
	}
}

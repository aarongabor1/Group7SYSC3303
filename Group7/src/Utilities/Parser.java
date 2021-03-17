package Utilities;

import Events.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import javax.swing.JFileChooser;

import Elevator.Elevator;
import Elevator.ElevatorState;
import Elevator.ElevatorSubsystem;

import java.util.ArrayList;

/***
 * This class takes in the file and uses the text to fill out the variable needed to
 * generate a floor event, time, current floor, direction and car button.
 * The class is also used as a router for events that are being sent over the network that makes sure that the correct events
 * get sent to the corresponding event listeners in the correct subsystems.
 * 
 * @author lynnmehyou, Aaron Gabor, Marc Angers
 * @version 1.1
 */
public class Parser {
	private File file;
	private ArrayList<FormattedEvent> inputEventsList;
	
	long startTime = 0;
	
	/**
	 * Asks user to pick the text file that condenses the floor events then stores it in a File object.
	 */
	public Parser() {
		//Start of code from https://www.codejava.net/java-se/swing/show-simple-open-file-dialog-using-jfilechooser
		JFileChooser fileChooser = new JFileChooser();
	    int result = fileChooser.showOpenDialog(null);
	    if (result == JFileChooser.APPROVE_OPTION) 
	    {
	      this.file = fileChooser.getSelectedFile();
	    }
	    //end of code from https://www.codejava.net/java-se/swing/show-simple-open-file-dialog-using-jfilechooser
	    inputEventsList = new ArrayList<FormattedEvent>();
	    try 
	    {
	    	readIn();
	    }
	    catch (Exception e) 
	    {
	    	System.out.println("ERROR");
	    	e.printStackTrace();
	    }
	}
	
	/**
	 * Assigns the file input text to the correct variables. Changes their type from string to the type they should have to satisfy the 
	 * FormattedEvent class.
	 * 
	 * @throws ParseException
	 */
	private void readIn() throws ParseException {
		Scanner scanner;
		
		try {
			scanner = new Scanner(file);
			String time;
			String currentFloor;
			String direction;
			String carButton;
			int currentFloor1;
			Direction direction1;
			int carButton1;
			DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss.mmm");
			while(scanner.hasNext()) {
				time = scanner.next().trim();
				currentFloor = scanner.next();
				direction = scanner.next();
				carButton = scanner.next();
				
				long ms = dateFormat.parse(time).getTime();
				if (startTime == 0) {
					startTime = ms;
					ms = 0;
				}
				else
					ms = ms - startTime;
				
				currentFloor1 = Integer.parseInt(currentFloor);
				direction1 = Direction.valueOf(direction.toUpperCase());
				carButton1 = Integer.parseInt(carButton);
				inputEventsList.add(new FormattedEvent(ms, currentFloor1, direction1, carButton1));
			}
			scanner.close();
			
		} catch (FileNotFoundException e) {
			System.out.print("Error file not found");
			e.printStackTrace();
		}	
	}
	
	/**
	 * parseFile send back one FormattedEvent object from the input file.
	 * 
	 * @return FormattedEvent object containing the next command
	 */
	public FormattedEvent parseFile() throws ParseException	{
		FormattedEvent temp;
		
		if (inputEventsList.size() > 0) {
			temp = inputEventsList.get(0);
			inputEventsList.remove(0);
			return temp;
		}
		else
			throw new ParseException("No more events in the input file!", 0);
	}
	
	/**
	 * Method to wrap an object (specifically the event objects within the system) in a datagrampacket that can be sent over the network.
	 */
	public static DatagramPacket packageObject(Object obj) {
		String eventType = getEventType(obj);
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(8191);
		
		try {
			ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
			objectStream.writeObject(obj);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
		
		byte[] data = byteStream.toByteArray();
		
		DatagramPacket packet;
		switch (eventType) {
		case "FBP":
			packet = new DatagramPacket(data, data.length, Settings.FLOOR_SYSTEM_ADDRESS, Settings.FLOOR_BUTTON_PRESS_ECP);
			break;
		case "EBP":
			ElevatorButtonPressEvent elevatorButtonPressEvent = (ElevatorButtonPressEvent) obj;
			int ebp_ecp = ElevatorSubsystem.elevatorButtonPressEventConsumerPorts.get(elevatorButtonPressEvent.elevatorID);
			packet = new DatagramPacket(data, data.length, Settings.ELEVATOR_SYSTEM_ADDRESS, ebp_ecp);
			break;
		case "DU":
			DestinationUpdateEvent destinationUpdateEvent = (DestinationUpdateEvent) obj;
			int du_ecp = ElevatorSubsystem.destinationUpdateEventConsumerPorts.get(destinationUpdateEvent.elevatorID);
			packet = new DatagramPacket(data, data.length, Settings.ELEVATOR_SYSTEM_ADDRESS, du_ecp);
			break;
		case "EA":
			packet = new DatagramPacket(data, data.length, Settings.FLOOR_SYSTEM_ADDRESS, Settings.ELEVATOR_ARRIVAL_ECP);
			break;
		case "EM":
			packet = new DatagramPacket(data, data.length, Settings.SCHEDULER_ADDRESS, Settings.ELEVATOR_MOVEMENT_ECP);
			break;
		case "ER":
			packet = new DatagramPacket(data, data.length, Settings.SCHEDULER_ADDRESS, Settings.ELEVATOR_REGISTRATION_ECP);
			break;
		default:
			// Might want to throw an error here or something idk.
			packet = new DatagramPacket(data, data.length);
			break;
		}
		
		return packet;
	}
	
	/**
	 * Method to unpack an object that was sent over the network.
	 */
	public static Object unpackDatagram(DatagramPacket packet) {
		byte[] data = packet.getData();

		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

		try {
			ObjectInputStream objectStream = new ObjectInputStream(inputStream);
			return objectStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// Might want to throw some sort of error here
		return new Object();
	}
	
	/**
	 * Helper method to determine what type of event was passed to the packageObject method.
	 * @param obj, the event to test
	 * @return the type of the event
	 */
	private static String getEventType(Object obj) {
		FloorButtonPressEvent tempFBPEvent = new FloorButtonPressEvent(System.currentTimeMillis(), 1, Direction.UP);
		ElevatorButtonPressEvent tempEBPEvent = new ElevatorButtonPressEvent(System.currentTimeMillis(), 1, 1);
		DestinationUpdateEvent tempDUEvent = new DestinationUpdateEvent(System.currentTimeMillis(), 1, 1);
		ElevatorArrivalEvent tempEAEvent = new ElevatorArrivalEvent(System.currentTimeMillis(), 1, 1, Direction.UP);
		ElevatorMovementEvent tempEMEvent = new ElevatorMovementEvent(System.currentTimeMillis(), 1, new ElevatorState(1, Direction.UP, 1));
		ElevatorRegistrationEvent tempEREvent = new ElevatorRegistrationEvent(System.currentTimeMillis(), new Elevator());
		
		if (obj.getClass() == tempFBPEvent.getClass())
			return "FBP";
		if (obj.getClass() == tempEBPEvent.getClass())
			return "EBP";
		if (obj.getClass() == tempDUEvent.getClass())
			return "DU";
		if (obj.getClass() == tempEAEvent.getClass())
			return "EA";
		if (obj.getClass() == tempEMEvent.getClass())
			return "EM";
		if (obj.getClass() == tempEREvent.getClass())
			return "ER";
		
		return "not-an-event";
	}
}

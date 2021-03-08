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
import java.util.ArrayList;
import java.sql.Time;

/***
 * This class takes in the file and uses the text to fill out the variable needed to
 * generate a floor event, time, current floor, direction and car button.
 * 
 * @author lynnmehyou, Aaron Gabor
 */
public class Parser {
	private File file;
	private ArrayList<FormattedEvent> inputEventsList ;
	
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
			Time time1;
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
				time1 = new Time(ms);
				currentFloor1 = Integer.parseInt(currentFloor);
				direction1 = Direction.valueOf(direction.toUpperCase());
				carButton1 = Integer.parseInt(carButton);
				inputEventsList.add(new FormattedEvent(time1, currentFloor1, direction1, carButton1));
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
			packet = new DatagramPacket(data, data.length, Settings.ELEVATOR_SYSTEM_ADDRESS, Settings.ELEVATOR_BUTTON_PRESS_ECP);
			break;
		case "DU":
			packet = new DatagramPacket(data, data.length, Settings.ELEVATOR_SYSTEM_ADDRESS, Settings.DESTINATION_UPDATE_ECP);
			break;
		case "EA":
			packet = new DatagramPacket(data, data.length, Settings.FLOOR_SYSTEM_ADDRESS, Settings.ELEVATOR_ARRIVAL_ECP);
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
		FloorButtonPressEvent tempFBPEvent = new FloorButtonPressEvent(new Time(1), 1, Direction.UP);
		ElevatorButtonPressEvent tempEBPEvent = new ElevatorButtonPressEvent(new Time(1), 1, 1);
		DestinationUpdateEvent tempDUEvent = new DestinationUpdateEvent(new Time(1), 1, 1);
		ElevatorArrivalEvent tempEAEvent = new ElevatorArrivalEvent(new Time(1), 1, 1, Direction.UP);
		
		if (obj.getClass() == tempFBPEvent.getClass())
			return "FBP";
		if (obj.getClass() == tempEBPEvent.getClass())
			return "EBP";
		if (obj.getClass() == tempDUEvent.getClass())
			return "DU";
		if (obj.getClass() == tempEAEvent.getClass())
			return "EA";
		
		return "not-an-event";
	}
}

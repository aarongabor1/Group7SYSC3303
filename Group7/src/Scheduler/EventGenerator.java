package Scheduler;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Events.ElevatorButtonPressEvent;
import Events.FloorButtonPressEvent;
import Events.FormattedEvent;
import Utilities.Direction;
import Utilities.Parser;
import Utilities.Settings;
import Elevator.ElevatorState;
import Floor.Floor;

/**
 * Class to generate the floor and elevator button press events that are contained in the input file.
 * 
 * @author Marc Angers
 * @version 1.0
 */
public class EventGenerator implements Runnable {
	private Scheduler parent;
	private Parser parser;
	private Map<ElevatorState, ElevatorButtonPressEvent> elevatorEvents;
	private List<FloorButtonPressEvent> floorEvents;
	
	public EventGenerator(Scheduler scheduler) {
		parent = scheduler;
		parser = new Parser();
		
		elevatorEvents = new HashMap<ElevatorState, ElevatorButtonPressEvent>();
		floorEvents = new LinkedList<FloorButtonPressEvent>();
	}
	
	
	/**
	 * Generates floor events using the parser file.
	 * @throws ParseException
	 */
	private void generateEvent() throws ParseException {
		FormattedEvent currentEventFromInput;
		
		try {
			currentEventFromInput = parser.parseFile();
		} catch (ParseException pe) {
			throw pe;
		}
		
		FloorButtonPressEvent floorButtonEvent = new FloorButtonPressEvent(currentEventFromInput);
		ElevatorButtonPressEvent elevatorButtonEvent = new ElevatorButtonPressEvent(currentEventFromInput);
		ElevatorState requiredState = new ElevatorState(floorButtonEvent.floor, floorButtonEvent.direction, floorButtonEvent.direction == Direction.UP ? Settings.NUMBER_OF_FLOORS : Floor.MINIMUM_FLOOR_NUM);
		
		elevatorEvents.put(requiredState, elevatorButtonEvent);
		floorEvents.add(floorButtonEvent);
	}
	
	/**
	 * Method to check if a floor button press event is ready to be triggered.
	 */
	private void checkForFloorButtonEvent() {
		Iterator<FloorButtonPressEvent> iterator = floorEvents.iterator();
		FloorButtonPressEvent floorButtonEvent;
		
		while (iterator.hasNext()) {
			floorButtonEvent = iterator.next();
			
			if (parent.getTime() >= floorButtonEvent.time) {
				parent.scheduleEvent(floorButtonEvent);
				iterator.remove();
			}
		}
	}
	
	/**
	 * Method to check if an elevator button press event is ready to be triggered.
	 */
	private void checkForElevatorButtonEvent() {
		// Loop through all of the waiting elevator button press events:
		Iterator<Entry<ElevatorState, ElevatorButtonPressEvent>> iterator = elevatorEvents.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<ElevatorState, ElevatorButtonPressEvent> elevatorEvent = (Map.Entry<ElevatorState, ElevatorButtonPressEvent>) iterator.next();
			ElevatorState requiredState = (ElevatorState) elevatorEvent.getKey();
			
			// Check if any of the current elevator states satisfy the requirements to fire the elevator button press event:
			for (Map.Entry<Integer, ElevatorState> elevatorStates : parent.getElevatorStates().entrySet()) {
				ElevatorState stateToCheck = (ElevatorState) elevatorStates.getValue();
				if (stateToCheck.triggersElevatorButtonEvent(requiredState)) {
					// Update the elevator button press event to make sure it's coming from the right elevator:
					elevatorEvent.getValue().updateElevatorID(elevatorStates.getKey());
					
					// Fire the elevator button press event:
					parent.scheduleEvent((ElevatorButtonPressEvent) elevatorEvent.getValue(), (int) elevatorStates.getKey());
					
					// Remove the elevator button press event:
					iterator.remove();
				}	
			}
		}
	}
	
	
	@Override
	public void run() {
		// Load all the events into memory from the input file.
		while (true) {
			try {
				generateEvent();
			} catch (ParseException pe) {
				break;
			}
		}
		// Wait for the elevators to be registered in the system before firing events.
		while (parent.getElevatorCount() < Settings.NUMBER_OF_ELEVATORS) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		
		// Fire the events when they are ready.
		while (elevatorEvents.size() > 0 || floorEvents.size() > 0) {
			checkForFloorButtonEvent();
			checkForElevatorButtonEvent();
		}
	}
}

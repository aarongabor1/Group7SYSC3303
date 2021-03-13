package Scheduler;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
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

public class EventGenerator implements Runnable {
	private Scheduler parent;
	private Parser parser;
	private Map<ElevatorState, ElevatorButtonPressEvent> elevatorEvents;
	
	public EventGenerator(Scheduler scheduler) {
		parent = scheduler;
		parser = new Parser();
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
		
		parent.scheduleEvent(floorButtonEvent);
	}
	
	/**
	 * Method to check if an elevator button press event is ready to be triggered.
	 */
	private void checkForElevatorButtonEvent() {
		// Loop through all of the waiting elevator button press events:
		Iterator<Entry<ElevatorState, ElevatorButtonPressEvent>> iterator = elevatorEvents.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry elevatorEvent = (Map.Entry) iterator.next();
			ElevatorState requiredState = (ElevatorState) elevatorEvent.getKey();
			
			// Check if any of the current elevator states satisfy the requirements to fire the elevator button press event:
			for (Map.Entry elevatorStates : parent.getElevatorStates().entrySet()) {
				ElevatorState stateToCheck = (ElevatorState) elevatorStates.getValue();
				if (stateToCheck.triggersElevatorButtonEvent(requiredState)) {
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
		while (true) {
			try {
				generateEvent();
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
			
			checkForElevatorButtonEvent();
		}
	}
}

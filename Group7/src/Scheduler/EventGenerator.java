package Scheduler;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Events.*;
import Utilities.Direction;
import Utilities.Parser;
import Utilities.Settings;
import Elevator.ElevatorState;
import Floor.Floor;

/**
 * Class to generate the floor and elevator button press events that are contained in the input file.
 * 
 * @author Marc Angers, Momin Mushtaha
 * @version 1.0
 */
public class EventGenerator implements Runnable {
	private Scheduler parent;
	private Parser parser;
	private Map<ElevatorState, ElevatorButtonPressEvent> elevatorEvents;
	private List<FloorButtonPressEvent> floorEvents;
	private List<FailureEvent> failureEvents;
	
	public EventGenerator(Scheduler scheduler) {
		parent = scheduler;
		parser = new Parser();
		
		elevatorEvents = new HashMap<ElevatorState, ElevatorButtonPressEvent>();
		floorEvents = new LinkedList<FloorButtonPressEvent>();
		failureEvents = new LinkedList<FailureEvent>();
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
				
		if (currentEventFromInput.isError) {
			long time = currentEventFromInput.getTime();
			String errorOccurred = currentEventFromInput.getErrorOccurred();
			String errorType = currentEventFromInput.getErrorType();
			int elevator = currentEventFromInput.getElevator();
			
			FailureEvent failureEvent;
			
			if (errorType.equals("HardFailure"))
			{
				failureEvent = new HardFailureEvent(time, errorOccurred, elevator);
			}
			else // errorType.equals("SoftFailure"), otherwise we have a problem
			{
				long duration = currentEventFromInput.getDuration();
				failureEvent = new SoftFailureEvent(time, errorOccurred, elevator, duration);
			}
			
			failureEvents.add(failureEvent);
		} else {
			FloorButtonPressEvent floorButtonEvent = new FloorButtonPressEvent(currentEventFromInput);
			ElevatorButtonPressEvent elevatorButtonEvent = new ElevatorButtonPressEvent(currentEventFromInput);
			ElevatorState requiredState = new ElevatorState(floorButtonEvent.floor, floorButtonEvent.direction, floorButtonEvent.direction == Direction.UP ? Settings.NUMBER_OF_FLOORS : Floor.MINIMUM_FLOOR_NUM);
			
			elevatorEvents.put(requiredState, elevatorButtonEvent);
			floorEvents.add(floorButtonEvent);
		}
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
		Map.Entry<ElevatorState, ElevatorButtonPressEvent> elevatorEvent;
		
		while (iterator.hasNext()) {
			elevatorEvent = (Map.Entry<ElevatorState, ElevatorButtonPressEvent>) iterator.next();
			ElevatorState requiredState = (ElevatorState) elevatorEvent.getKey();
			ElevatorButtonPressEvent pressEvent = (ElevatorButtonPressEvent) elevatorEvent.getValue();
			
			// Check if it is time that the elevator button press event may be fired:
            if (parent.getTime() >= elevatorEvent.getValue().time) {
                // Check if any of the current elevator states satisfy the requirements to fire the elevator button press event:
                for (Map.Entry<Integer, ElevatorState> elevatorStates : parent.getElevatorStates().entrySet()) {
                    ElevatorState stateToCheck = (ElevatorState) elevatorStates.getValue();
                    
                    if (stateToCheck.triggersElevatorButtonEvent(requiredState)) {
                        
                        // Update the elevator button press event to make sure it's coming from the right elevator:
                        elevatorEvent.getValue().updateElevatorID(elevatorStates.getKey());
                        
                        // Fire the elevator button press event:
                        parent.scheduleEvent((ElevatorButtonPressEvent) elevatorEvent.getValue(), (int) elevatorStates.getKey());
                        System.out.println("Elevator floor " + requiredState.getFloor() + " Button pressed " + pressEvent.buttonNumber);
                        
                        // Remove the elevator button press event:
                        iterator.remove();
                        
                        break;
                    }   
                }
            }
		}
	}
	
	/**
	 * Method to check if an error event should be fired
	 */
	private void checkForFailureEvent() {
		Iterator<FailureEvent> iterator = failureEvents.iterator();
		FailureEvent failureEvent;
		
		while (iterator.hasNext()) {
			failureEvent = iterator.next();
			
			if (parent.getTime() >= failureEvent.getTime()) {		
				parent.sendFailure(failureEvent);
				iterator.remove();
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
		while (true) {
			checkForFloorButtonEvent();
			checkForElevatorButtonEvent();
			checkForFailureEvent();
		}
	}
}

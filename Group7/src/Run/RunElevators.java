package Run;

import java.util.LinkedList;
import java.util.List;

import Elevator.Elevator;
import Utilities.Settings;

public class RunElevators {
	public static void main(String args[]) {
		List<Elevator> elevatorList = new LinkedList<>();
		List<Thread> elevatorSubsystemList = new LinkedList<>();
				
		for (int i = 1; i <= Settings.NUMBER_OF_ELEVATORS; i++) {
		    elevatorList.add(new Elevator(i));
		}
		
		for (Elevator e : elevatorList) {
		    elevatorSubsystemList.add(new Thread(e.getElevatorSubsystem(), "Elevator Subsystem #" + e.getID()));
		}
		
		for (Thread e : elevatorSubsystemList) {
		    e.start();
		}
	}
}

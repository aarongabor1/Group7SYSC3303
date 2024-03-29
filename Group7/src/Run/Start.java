package Run; 

import java.util.LinkedList;
import java.util.List;

import Elevator.*;
import Floor.*;
import Scheduler.Scheduler;
import Utilities.Settings;

/**
  * The Start class is the main entry point into the code. This class creates and runs the threads for all the other systems in the code.
  * 
  * @author Marc Angers, Diana Miraflor
  *	@version 1.1
  */
public class Start {
	public static void main(String[] args) {
		List<Elevator> elevatorList = new LinkedList<>();
		List<Thread> elevatorSubsystemList = new LinkedList<>();
	    
	    Scheduler scheduler;
		Thread floorSubsystem,  schedulerThread;
		
		scheduler = new Scheduler();
		
		for (int i = 1; i <= Settings.NUMBER_OF_ELEVATORS; i++) {
		    elevatorList.add(new Elevator(i));
		}
		
		// Generate a thread for each elevator in the system
		for (Elevator e : elevatorList) {
		    elevatorSubsystemList.add(new Thread(e.getElevatorSubsystem(), "Elevator Subsystem #" + e.getID()));
		}
		
		// Generate threads for the other subsystems
		floorSubsystem = new Thread(new FloorSubsystem(), "Floor Subsystem");
		schedulerThread = new Thread(scheduler, "Scheduler");
		
		// Run all the threads
		floorSubsystem.start();
		
		for (Thread e : elevatorSubsystemList) {
		    e.start();
		}
		
		schedulerThread.start();
	}
}

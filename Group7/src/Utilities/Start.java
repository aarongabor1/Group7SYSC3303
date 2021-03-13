package Utilities; 

import java.util.LinkedList;
import java.util.List;

import Elevator.*;
import Floor.*;
import Scheduler.Scheduler;

/**
  * The Start class is the main entry point into the code. This class creates and runs the threads for all the other systems in the code.
  * @author Marc Angers
  *	@version 1.0.0
  */
public class Start {
	public static void main(String[] args) {
		List<Elevator> elevatorList = new LinkedList<>();
		List<Thread> elevatorSubsystemList = new LinkedList<>();
	    
	    Scheduler scheduler;
		Elevator elevator;
		Thread floorSubsystem, elevatorSubsystem, schedulerThread;
		
		scheduler = new Scheduler();
		
		for (int i = 1; i <= Settings.NUMBER_OF_ELEVATORS; i++) {
		    elevatorList.add(new Elevator(scheduler, i));
		}
		
		for (Elevator e : elevatorList) {
		    elevatorSubsystemList.add(new Thread(e.getElevatorSubsystem(), "Elevator Subsystem #" + e.getID()));
		}
		
		// Generate threads for each subsystem
		floorSubsystem = new Thread(new FloorSubsystem(scheduler), "Floor Subsystem");
		//elevatorSubsystem = new Thread(elevator.getElevatorSubsystem(), "Elevator Subsystem");
		schedulerThread = new Thread(scheduler, "Scheduler");
		
		floorSubsystem.start();
		for (Thread e : elevatorSubsystemList) {
		    e.start();
		}
		
		//elevatorSubsystem.start();
		schedulerThread.start();
	}
}

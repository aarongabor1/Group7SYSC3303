package Utilities; 

import Elevator.*;
import Floor.*;

/**
  * The Start class is the main entry point into the code. This class creates and runs the threads for all the other systems in the code.
  * @author Marc Angers
  *	@version 1.0.0
  */
public class Start {
	public static void main(String[] args) {
		Scheduler scheduler;
		Elevator elevator;
		Thread floorSubsystem, elevatorSubsystem, schedulerThread;
		
		scheduler = new Scheduler();
		elevator = new Elevator(scheduler);

		
		// Generate threads for each subsystem
		floorSubsystem = new Thread(new FloorSubsystem(scheduler), "Floor Subsystem");
		elevatorSubsystem = new Thread(elevator.getElevatorSubsystem(), "Elevator Subsystem");
		schedulerThread = new Thread(scheduler, "Scheduler");
		
		floorSubsystem.start();
		elevatorSubsystem.start();
		schedulerThread.start();
	}
}

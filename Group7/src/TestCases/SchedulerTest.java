package TestCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Elevator.Elevator;
import Utilities.Scheduler;

class SchedulerTest {
	Scheduler scheduler;
	Elevator elevator;
	Thread elevatorSubsystem;
	Thread schedulerThread;
	
	@BeforeEach
	public void setUp() {
		scheduler = new Scheduler(); // Causes you to input a file.
		elevator = new Elevator(scheduler);
		schedulerThread = new Thread(scheduler, "Scheduler");
		elevatorSubsystem = new Thread(elevator.getElevatorSubsystem(), "Elevator Subsystem");
	}
	
	@Test
	void test() {
		fail("Not yet implemented");
	}

}

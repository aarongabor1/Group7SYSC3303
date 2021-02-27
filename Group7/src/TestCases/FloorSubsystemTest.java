package TestCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Elevator.Elevator;
import Floor.FloorSubsystem;
import Utilities.Scheduler;

class FloorSubsystemTest {
	Thread floorSubsystem;
	Scheduler scheduler;
	Thread schedulerThread;
	FloorSubsystem fs;
	
	@BeforeAll
	void setUp() {
		scheduler = new Scheduler();
		schedulerThread = new Thread(scheduler, "Scheduler");
		fs = new FloorSubsystem(scheduler);
		floorSubsystem = new Thread(fs, "Floor Subsystem");
		floorSubsystem.start();
		schedulerThread.start();
	}
	
	@Test
	void testGeneratedFloors() {
		assertTrue(!fs.getFloors().isEmpty());
	}
	
}

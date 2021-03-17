package Run;

import Floor.FloorSubsystem;

/**
 * Class to run just the floor subsystem on a given computer.
 * 
 * @author Marc Angers
 * @verion 1.0
 */
public class RunFloorSystem {
	public static void main(String args[]) {
		Thread floorSubsystem;

		floorSubsystem = new Thread(new FloorSubsystem(), "Floor Subsystem");
		
		floorSubsystem.start();
	}
}

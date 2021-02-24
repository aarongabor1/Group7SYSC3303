 /**
  * 
  * @author Marc Angers
  *	@version 1.0.0
  */
public class Start {
	public static void main(String[] args) {
		Scheduler scheduler;
		Elevator elevator;
		Thread floorSubsystem, elevatorSubsystem;
		
		scheduler = new Scheduler();
		elevator = new Elevator(scheduler, 6);
		
		// Generate threads for each subsystem
		floorSubsystem = new Thread(new FloorSubsystem(6, scheduler), "Floor Subsystem");
		elevatorSubsystem = new Thread(elevator.getElevatorSubsystem(), "Elevator Subsystem");
		//scheduler = new Thread(new Scheduler(network), "Scheduler");
		
		floorSubsystem.start();
		elevatorSubsystem.start();
		//scheduler.start();
	}
}

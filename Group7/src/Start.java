 /**
  * 
  * @author Marc Angers
  *	@version 1.0.0
  */
public class Start {
	public static void main(String[] args) {
		Network network;
		Elevator elevator;
		Thread floorSubsystem, elevatorSubsystem, scheduler;
		
		network = new Network();
		elevator = new Elevator(network, 6);
		
		// Generate threads for each subsystem
		floorSubsystem = new Thread(new FloorSubsystem(6, network), "Floor Subsystem");
		elevatorSubsystem = new Thread(elevator.getElevatorSubsystem(), "Elevator Subsystem");
		scheduler = new Thread(new Scheduler(network), "Scheduler");
		
		floorSubsystem.start();
		elevatorSubsystem.start();
		scheduler.start();
	}
}

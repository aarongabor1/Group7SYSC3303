package sysc3303proj;
/***
 * Floor class 
 */
import java.util.List;

public class Floor {
	private int floorNumber;
	private List<FloorButton> floorButtons;
	private boolean maxFloor;
	private boolean isElevatorApproaching;
	private boolean isElevatorAtFloor;
	private FloorEvent fe;
	
	public Floor(int floorNumber, boolean maxFloor) {
		this.floorNumber= floorNumber;
		this.maxFloor = maxFloor;
	}
	public int getFloorID() {
		
		return floorNumber;
		
	}
	public boolean isMax() {
		if(floorNumber== 6) {
			maxFloor = true;
		}
		else {
			maxFloor = false;
		}
		return maxFloor;	
	}

	public boolean elevatorApproachingFloor() {
		if (fe.getCurrentFloor() == fe.getCarButton()+1 || fe.getCurrentFloor() == fe.getCarButton()-1) {
			isElevatorApproaching = true;
			// should ask the motor to start decelerating
		}
		else {
			isElevatorApproaching = false;
		}
		return isElevatorApproaching;
	}
	
	public boolean elevatorAtFloor() {
		if(fe.getCurrentFloor() == fe.getCarButton()) {
			isElevatorAtFloor = true;
			// should send notification to the elevator to stop and open doors
		}
		else {
			isElevatorAtFloor = false;
		}
		return isElevatorAtFloor;
	}
}

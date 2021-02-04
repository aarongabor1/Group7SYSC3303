/***
 * @author lynnmehyou
 * Floor class 
 */

public class Floor {
	private int floorNumber;
	private boolean maxFloor;
	private boolean isElevatorApproaching;
	private boolean isElevatorAtFloor;
	private FloorEvent fe;
	/***
	 * Floor constructor.
	 * @param floorNumber
	 * @param maxFloor
	 */
	public Floor(int floorNumber, boolean maxFloor) {
		this.floorNumber= floorNumber;
		this.maxFloor = maxFloor;
	}
	public int getFloorID() {
		
		return floorNumber;
		
	}
	/***
	 * Returns true if it is the maximum floor number which is 6.
	 * @return
	 */
	public boolean isMax() {
		if(floorNumber == 6) {
			maxFloor = true;
		}
		else {
			maxFloor = false;
		}
		return maxFloor;	
	}

	/***
	 * returns true if the elevator is approaching the current floor.
	 * @return
	 */
	public boolean elevatorApproachingFloor() {
		if (fe.getCurrentFloor() == fe.getCarButton()+1 || fe.getCurrentFloor() == fe.getCarButton()-1) {
			isElevatorApproaching = true;
		}
		else {
			isElevatorApproaching = false;
		}
		return isElevatorApproaching;
	}
	
	/***
	 * returns true if the elevator is at the current floor.
	 * @return
	 */
	
	public boolean elevatorAtFloor() {
		if(fe.getCurrentFloor() == fe.getCarButton()) {
			isElevatorAtFloor = true;
		}
		else {
			isElevatorAtFloor = false;
		}
		return isElevatorAtFloor;
	}
}

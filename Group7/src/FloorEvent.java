//@Author Aaron Gabor
import java.sql.Time;

class FloorEvent {
	private Time time;
	private int currentFloor;
	private boolean direction;//True = Up | False = Down
	private int carButton;
	
	public FloorEvent(Time time, int currentFloor, boolean direction, int carButton)
	{
		this.time = time;
		this.currentFloor = currentFloor;
		this.direction = direction;
		this.carButton = carButton;
	}
	
	public Time getTime()
	{
		return this.time;
	}
	
	public int getCurrentFloor()
	{
		return this.currentFloor;
	}
	
	public boolean getDirection()
	{
		return this.direction;
	}
	
	public int getCarButton()
	{
		return this.carButton;
	}
}


public class FloorButton extends Button {
	private Direction direction;
	
	public FloorButton(FloorLamp floorLamp, Direction direction) {
		super(floorLamp);
		this.direction = direction;
	}
}

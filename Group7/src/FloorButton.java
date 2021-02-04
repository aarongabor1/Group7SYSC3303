/**
 * 
 * @author Marc Angers
 * @version 1.0.0
 */
public class FloorButton extends Button {
	private Direction direction;
	
	public FloorButton(FloorLamp floorLamp, Direction direction) {
		super(floorLamp);
		this.direction = direction;
	}
}

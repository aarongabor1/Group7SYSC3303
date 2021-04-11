package Floor;

import Utilities.*;

/**
 * Class to simulate a button on a floor
 * 
 * @author Marc Angers
 * @version 1.1
 */
public class FloorButton extends Button {
	private static final long serialVersionUID = -2696214633708647490L;
	private Direction direction;
	
	public FloorButton(Direction direction) {
		super(false);
		this.direction = direction;
	}
	
	public Direction getDirection() {
		return direction;
	}
}

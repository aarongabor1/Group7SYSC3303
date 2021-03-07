package Floor;

import Utilities.*;

/**
 * 
 * @author Marc Angers
 * @version 1.0.0
 */
public class FloorButton extends Button {
	private Direction direction;
	
	public FloorButton(Direction direction) {
		super(false);
		this.direction = direction;
	}
	
	/**
	public void press() {
		super.press();
		System.out.println("Floor lamp " + direction + " is off");
	}
	
	public void unPress() {
		super.unPress();
		System.out.println("Floor lamp " + direction + " is off");
	}
	*/
}

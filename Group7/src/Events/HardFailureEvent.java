package Events;

import java.io.Serializable;

/**
 * Event class to notify when a HardFailure occurs and specifies it's cause
 * @author Momin Mushtaha
 * @vertion 1.1
 */
public class HardFailureEvent extends FailureEvent implements Serializable {
    private static final long serialVersionUID = -2277432317605146712L;
    private String whatHappened;

    public HardFailureEvent(long time, String whatHappened, int elevatorID) {
    	super(time, elevatorID);
    	this.whatHappened = whatHappened;
    }
    
    public String getWhatHappened() {
    	return whatHappened;
    }
    
    
}
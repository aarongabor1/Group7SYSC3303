package Events;

import java.io.Serializable;

/**
 * Event class to notify when a SoftFailure occurs and specifies it's cause
 * @author Momin Mushtaha
 * @vertion 1.1
 */
public class SoftFailureEvent extends FailureEvent implements Serializable {
    private static final long serialVersionUID = 7997388758226315463L;
    private String whatHappened;
    private long duration;
    
    public SoftFailureEvent(long time, String whatHappened, int elevatorID, long duration) {
    	super(time, elevatorID);
    	this.whatHappened = whatHappened;
    	this.duration = duration;
    }
    
    // get methods
    public String getWhatHappened() {
    	return whatHappened;
    }
    
    public long getDuration() {
    	return this.duration;
    }
}
package Events;

import java.io.Serial;
import java.io.Serializable;

/**
 * Event class to notify when a SoftFailure occurs and specifies it's cause
 * @author Momin Mushtaha
 * @vertion 1.1
 */
public class SoftFailureEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 7997388758226315463L;

    public SoftFailureEvent(String whatHappened) {
        System.out.println("SoftFailureEvent raised due to " + whatHappened);
    }
}
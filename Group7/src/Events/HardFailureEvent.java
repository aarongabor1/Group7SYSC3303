package Events;

import java.io.Serial;
import java.io.Serializable;

/**
 * Event class to notify when a HardFailure occurs and specifies it's cause
 * @author Momin Mushtaha
 * @vertion 1.1
 */
public class HardFailureEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = -2277432317605146712L;

    public HardFailureEvent(String whatHappened) {
        System.out.println("HardFailureEvent raised due to " + whatHappened);
    }
}
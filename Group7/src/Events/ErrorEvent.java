package Events;

import java.io.Serializable;

/**
 * An event class to notify an elevator about an error
 * 
 * @author Diana Miraflor
 *
 */
public class ErrorEvent implements Serializable  {

    private static final long serialVersionUID = -1164954769235001221L;
    private int elevatorID;
    private boolean errorType; // true if hard failure, false if soft failure
    
    /**
     * Constructor for a new error event
     * 
     * @param elevatorID
     * @param errorType
     */
    public ErrorEvent(int elevatorID, boolean errorType) {
        this.elevatorID = elevatorID;
        this.errorType = errorType;
        
    }
    
    /**
     * Returns an elevator's ID
     * 
     * @return int ID
     */
    public int getElevatorID() {
        return elevatorID;
    }
    
    /**
     * Returns the type of error
     * 
     * @return boolean true if hard failure, false if soft failure
     */
    public boolean getErrorType() {
        return errorType;
    }
}

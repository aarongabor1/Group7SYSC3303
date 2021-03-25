package Events;

import java.io.Serializable;

public class ErrorEvent implements Serializable  {

    private static final long serialVersionUID = -1164954769235001221L;
    private int elevatorID;
    private boolean errorType; // true if hard failure, false if soft failure
    
    public ErrorEvent(int elevatorID, boolean errorType) {
        this.elevatorID = elevatorID;
        this.errorType = errorType;
        
    }
    
    public int getElevatorID() {
        return elevatorID;
    }
    
    public boolean getErrorType() {
        return errorType;
    }
}

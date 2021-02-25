package Utilities;

/**
 * Enum class for the DoorPositons used by Door class
 *
 * @author Momin Mushtaha
 * @version 02 February 2021
 */
public enum DoorPosition {
    OPEN {
        @Override
        public String toString() {
            return "Open";
        }
    },
    CLOSED {
        @Override
        public String toString() {
            return "Closed";
        }
    },
}
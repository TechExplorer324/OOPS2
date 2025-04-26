/**
 * Custom Exception (XI) for invalid vehicle types or operations.
 */
class InvalidVehicleTypeException extends Exception {

    // Default error message
    private static final String DEFAULT_MESSAGE = "Invalid vehicle type or unsupported operation.";

    /**
     * Constructor with custom message.
     *
     * @param message Custom error message.
     */
    public InvalidVehicleTypeException(String message) {
        super(message);
    }

    /**
     * Constructor with default message.
     */
    public InvalidVehicleTypeException() {
        super(DEFAULT_MESSAGE);
    }
}

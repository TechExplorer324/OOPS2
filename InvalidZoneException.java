/**
 * Custom Exception (XI) for when an invalid or non-existent zone is specified.
 */
class InvalidZoneException extends Exception {

    // Default error message
    private static final String DEFAULT_MESSAGE = "Specified zone is invalid or does not exist.";

    /**
     * Constructor with custom message.
     *
     * @param message Custom error message.
     */
    public InvalidZoneException(String message) {
        super(message);
    }

    /**
     * Constructor with default message.
     */
    public InvalidZoneException() {
        super(DEFAULT_MESSAGE);
    }
}

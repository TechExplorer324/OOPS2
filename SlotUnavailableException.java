/**
 * Custom Exception (XI) for when no suitable parking slot is available.
 */
class SlotUnavailableException extends Exception {

    // Default error message
    private static final String DEFAULT_MESSAGE = "No suitable parking slot is available.";

    /**
     * Constructor with custom message.
     *
     * @param message Custom error message.
     */
    public SlotUnavailableException(String message) {
        super(message);
    }

    /**
     * Constructor with default message.
     */
    public SlotUnavailableException() {
        super(DEFAULT_MESSAGE);
    }
}

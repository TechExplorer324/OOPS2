/**
 * Custom Exception (XI) for payment processing errors.
 */
class PaymentException extends Exception {

    // Default error message
    private static final String DEFAULT_MESSAGE = "An error occurred during payment processing.";

    /**
     * Constructor with custom message.
     *
     * @param message Custom error message.
     */
    public PaymentException(String message) {
        super(message);
    }

    /**
     * Constructor with custom message and cause.
     *
     * @param message Custom error message.
     * @param cause   The cause of the exception.
     */
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with default message.
     */
    public PaymentException() {
        super(DEFAULT_MESSAGE);
    }
}

import java.time.LocalDateTime;

/**
 * Concrete implementation of PaymentProcessor for UPI.
 * Implements multiple interfaces (PaymentProcessor, Loggable) to handle payment processing and logging. (VIII)
 */
class UPIProcessor implements PaymentProcessor, Loggable {
    // LogManager instance to handle logging
    private final LogManager logManager = new LogManager();

    /**
     * Processes a UPI payment.
     * Simulates a UPI payment with a random success/failure rate.
     * @param amount The amount to be processed.
     * @return true if the payment is successful, false otherwise.
     */
    @Override
    public boolean processPayment(double amount) {
        log("Processing UPI payment for $" + amount);  // Log payment attempt
        // Simulate a 95% success rate for UPI payments
        boolean success = Math.random() > 0.05;
        if (success) {
            log("UPI payment successful.");  // Log success
        } else {
            log("UPI payment failed.");  // Log failure
        }
        return success;
    }

    /**
     * Returns the name of the payment processor (UPI).
     * @return "UPI"
     */
    @Override
    public String getProcessorName() {
        return "UPI";
    }

    /**
     * Logs a message to the log file using LogManager.
     * The log entry includes a timestamp and the provided message.
     * @param message The message to be logged.
     */
    @Override
    public void log(String message) {
        // Log the message to a file with the current timestamp
        logManager.writeLog("payment_upi.log", "[" + LocalDateTime.now() + "] " + message);
        // Optionally, print to the console (commented out in this case)
        // System.out.println("[UPI Log] " + message);
    }
}

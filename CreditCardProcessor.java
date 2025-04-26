import java.time.LocalDateTime;

/**
 * Concrete implementation of PaymentProcessor for Credit Cards.
 * Implements the PaymentProcessor and Loggable interfaces.
 * This class simulates credit card payment processing and logging (VIII).
 */
class CreditCardProcessor implements PaymentProcessor, Loggable {

    // LogManager instance for logging payment events
    private final LogManager logManager = new LogManager(); // Use shared log manager for consistent logging

    /**
     * Processes the payment for the given amount using a simulated credit card transaction.
     * 
     * @param amount The amount to be processed for payment.
     * @return true if payment is successful, false otherwise (simulated with a 90% success rate).
     */
    @Override
    public boolean processPayment(double amount) {
        // Log the start of the payment processing
        log("Processing credit card payment for $" + amount);

        // Simulate payment success with a 90% success rate
        boolean success = Math.random() > 0.1; // 90% success rate simulation

        // Log the result of the payment attempt
        if (success) {
            log("Credit card payment successful.");
        } else {
            log("Credit card payment failed.");
        }

        // Return the payment success status
        return success;
    }

    /**
     * Returns the name of the payment processor (Credit Card).
     * 
     * @return The name of the payment processor.
     */
    @Override
    public String getProcessorName() {
        return "Credit Card";
    }

    /**
     * Logs payment-specific messages to a dedicated log file.
     * Uses the LogManager to write messages.
     * 
     * @param message The message to be logged.
     */
    @Override
    public void log(String message) {
        // Write log to a specific log file with timestamp
        logManager.writeLog("payment_cc.log", "[" + LocalDateTime.now() + "] " + message);

        // Optionally print the log to the console for immediate feedback (if needed)
        // System.out.println("[CC Log] " + message);
    }
}

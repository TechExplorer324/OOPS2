import java.time.LocalDateTime;

/**
 * Concrete implementation of PaymentProcessor for Credit Cards.
 * Implements multiple interfaces (PaymentProcessor, Loggable) (VIII).
 */
class CreditCardProcessor implements PaymentProcessor, Loggable {
    private final LogManager logManager = new LogManager(); // Use shared log manager

    @Override
    public boolean processPayment(double amount) {
        // Simulate credit card processing
        log("Processing credit card payment for $" + amount);
        boolean success = Math.random() > 0.1; // 90% success rate simulation
        if (success) {
            log("Credit card payment successful.");
        } else {
            log("Credit card payment failed.");
        }
        return success;
    }

    @Override
    public String getProcessorName() {
        return "Credit Card";
    }

    @Override
    public void log(String message) {
        // Log payment-specific messages to a dedicated file or the general log
        logManager.writeLog("payment_cc.log", "[" + LocalDateTime.now() + "] " + message);
        // System.out.println("[CC Log] " + message); // Also print to console if needed
    }
}

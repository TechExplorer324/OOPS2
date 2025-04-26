import java.time.LocalDateTime;

/**
 * Concrete implementation of PaymentProcessor for UPI.
 * Implements multiple interfaces (PaymentProcessor, Loggable) (VIII).
 */
class UPIProcessor implements PaymentProcessor, Loggable {
    private final LogManager logManager = new LogManager();

    @Override
    public boolean processPayment(double amount) {
        // Simulate UPI processing
        log("Processing UPI payment for $" + amount);
        boolean success = Math.random() > 0.05; // 95% success rate simulation
        if (success) {
            log("UPI payment successful.");
        } else {
            log("UPI payment failed.");
        }
        return success;
    }

    @Override
    public String getProcessorName() {
        return "UPI";
    }

    @Override
    public void log(String message) {
        logManager.writeLog("payment_upi.log", "[" + LocalDateTime.now() + "] " + message);
        // System.out.println("[UPI Log] " + message);
    }
}

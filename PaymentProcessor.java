/**
 * Interface (VI) for payment processing gateways.
 * Demonstrates Multiple Inheritance possibility when combined with other interfaces.
 */
interface PaymentProcessor {
    /**
     * Processes a payment transaction.
     * @param amount The amount to be processed.
     * @return true if payment is successful, false otherwise.
     */
    boolean processPayment(double amount);

    /**
     * Gets the name of the payment processor.
     * @return The processor's name.
     */
    String getProcessorName();
}

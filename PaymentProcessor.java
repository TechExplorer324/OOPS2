/**
 * Interface (VI) for payment processing gateways.
 * This interface represents the contract that any payment processor should implement.
 * It demonstrates multiple inheritance possibilities when combined with other interfaces.
 * Implementing this interface ensures that a class will provide the required payment processing functionality.
 */
interface PaymentProcessor {
    /**
     * Processes a payment transaction.
     * This method should handle the logic for processing a payment of the specified amount.
     *
     * @param amount The amount to be processed (e.g., total payment for a parking ticket).
     * @return true if the payment is successfully processed, false otherwise.
     *         The return value indicates whether the payment operation was successful or failed.
     */
    boolean processPayment(double amount);

    /**
     * Gets the name of the payment processor.
     * This method allows the system to identify which payment gateway is being used for the transaction.
     *
     * @return The name of the payment processor (e.g., "Stripe", "PayPal", "Google Pay").
     */
    String getProcessorName();
}

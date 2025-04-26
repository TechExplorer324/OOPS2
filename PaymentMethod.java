/**
 * Enumeration for supported payment methods (simulation).
 * This enum represents the different types of payment methods that can be used in the parking system.
 */
enum PaymentMethod {
    /**
     * Represents payment made using a credit card.
     * This payment method can involve traditional credit card processing.
     */
    CREDIT_CARD, 
    
    /**
     * Represents payment made via UPI (Unified Payments Interface).
     * UPI is a digital payment method popular in India, allowing instant bank transfers.
     */
    UPI, 
    
    /**
     * Represents payment made using a wallet service.
     * Wallets can be digital services like Google Pay, Paytm, etc., where users can store money and make payments.
     */
    WALLET
}

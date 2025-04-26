import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages billing calculations and coordinates payment processing.
 * - Implements Dynamic Pricing based on zones and peak hours.
 * - Supports flexible payment processors (Strategy Pattern).
 * - Minimum 6 classes requirement met (for project structure).
 */
class BillingSystem {

    private PaymentProcessor currentProcessor; // Currently active payment processor (Strategy Pattern)
    private final Map<String, DynamicPricingRule> zonePricingRules; // Zone ID mapped to specific pricing rules
    private final DynamicPricingRule defaultPricingRule; // Fallback pricing rule if no zone-specific rule exists

    /**
     * Constructor for BillingSystem.
     * Initializes default pricing and empty zone-specific pricing.
     */
    public BillingSystem() {
        this.zonePricingRules = new HashMap<>();
        // Set a sensible default pricing rule: $2.5/hr, 1.5x multiplier during peak, $20 daily max
        this.defaultPricingRule = new DynamicPricingRule(2.5, 1.5, 20.0);
        System.out.println("Billing System Initialized.");
    }

    /**
     * Sets or switches the payment processor used for processing payments.
     *
     * @param processor A PaymentProcessor implementation (e.g., CreditCardProcessor, PayPalProcessor).
     */
    public void setPaymentProcessor(PaymentProcessor processor) {
        this.currentProcessor = processor;
        System.out.println("Payment processor set to: " + (processor != null ? processor.getProcessorName() : "None"));
    }

    /**
     * Adds or updates dynamic pricing rules for a specific parking zone.
     *
     * @param zoneId Unique identifier for the parking zone.
     * @param rule   Pricing rule to apply for the zone.
     */
    public void addZonePricingRule(String zoneId, DynamicPricingRule rule) {
        if (zoneId != null && rule != null) {
            this.zonePricingRules.put(zoneId, rule);
            System.out.println("Pricing rule updated for zone: " + zoneId);
        }
    }

    /**
     * Calculates parking fee based on parking duration, vehicle type, spot type, and zone-specific rules.
     *
     * @param vehicle   The parked vehicle.
     * @param spot      The parking spot used.
     * @param entryTime Time of entry into the parking lot.
     * @param exitTime  Time of exit from the parking lot.
     * @param zone      The parking zone where the spot is located.
     * @return Calculated parking fee in dollars.
     */
    public double calculateFee(Vehicle vehicle, ParkingSpot spot, LocalDateTime entryTime, LocalDateTime exitTime, ParkingZone zone) {
        if (entryTime == null || exitTime == null || exitTime.isBefore(entryTime)) {
            System.err.println("Invalid entry/exit times for fee calculation.");
            return 0.0; // Alternatively, you could throw an exception
        }

        Duration duration = Duration.between(entryTime, exitTime);
        long minutesParked = duration.toMinutes();

        // Grace period: No fee if parked for less than 5 minutes
        if (minutesParked < 5) {
            return 0.0;
        }

        // Fetch the applicable pricing rule
        DynamicPricingRule rule = defaultPricingRule;
        if (zone != null && zonePricingRules.containsKey(zone.getZoneId())) {
            rule = zonePricingRules.get(zone.getZoneId());
            System.out.println("Using pricing rule for zone: " + zone.getZoneId());
        } else {
            System.out.println("Using default pricing rule.");
        }

        // Base fee calculation
        double fee = rule.calculate(duration, entryTime.toLocalTime());

        // Vehicle type based adjustment
        if (vehicle.getType() == VehicleType.TRUCK) {
            fee *= 1.2; // 20% surcharge for trucks
        } else if (vehicle.getType() == VehicleType.BIKE) {
            fee *= 0.7; // 30% discount for bikes
        }

        // Extra charging fee for electric vehicles
        if (spot.supportsCharging() && vehicle.getType() == VehicleType.ELECTRIC_VEHICLE) {
            fee += 5.0; // Flat charging fee
        }

        System.out.printf("Calculated fee for %s: $%.2f (Duration: %s)\n", vehicle.getLicensePlate(), fee, formatDuration(duration));
        return fee;
    }

    /**
     * Processes a payment for the given amount using the current payment processor.
     *
     * @param amount Amount to be paid.
     * @return true if payment succeeds, false otherwise.
     * @throws PaymentException If no processor is set or payment fails.
     */
    public boolean processPayment(double amount) throws PaymentException {
        if (currentProcessor == null) {
            throw new PaymentException("No payment processor configured.");
        }
        if (amount <= 0) {
            System.out.println("Payment amount is zero or negative, no processing needed.");
            return true; // Nothing to process
        }

        System.out.printf("Attempting payment of $%.2f using %s...\n", amount, currentProcessor.getProcessorName());
        boolean success = currentProcessor.processPayment(amount);

        if (!success) {
            throw new PaymentException("Payment failed using " + currentProcessor.getProcessorName());
        }

        System.out.println("Payment successful.");
        return true;
    }

    /**
     * Helper method to format Duration into a readable string (e.g., "2h 45m").
     *
     * @param duration Duration to format.
     * @return Formatted string representation.
     */
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }

    /**
     * Static Nested Class.
     * Represents a dynamic pricing rule (different rates based on time and duration).
     */
    public static class DynamicPricingRule {
        private final double hourlyRate;
        private final double peakMultiplier; // Applied during peak hours
        private final double dailyRate;      // Maximum charge allowed per 24-hour period
        private final java.time.LocalTime peakStart = java.time.LocalTime.of(8, 0);  // 8 AM peak start
        private final java.time.LocalTime peakEnd = java.time.LocalTime.of(18, 0);   // 6 PM peak end

        /**
         * Constructor to create a DynamicPricingRule.
         *
         * @param hourlyRate     Base cost per hour.
         * @param peakMultiplier Cost multiplier applied during peak hours.
         * @param dailyRate      Maximum daily cap for charges.
         */
        public DynamicPricingRule(double hourlyRate, double peakMultiplier, double dailyRate) {
            this.hourlyRate = hourlyRate;
            this.peakMultiplier = peakMultiplier;
            this.dailyRate = dailyRate;
        }

        /**
         * Calculates the parking fee based on duration and time of day (peak/off-peak).
         *
         * @param duration       Total parking duration.
         * @param entryLocalTime Time of day when parking started (used for peak check).
         * @return Computed fee.
         */
        public double calculate(Duration duration, java.time.LocalTime entryLocalTime) {
            long totalMinutes = duration.toMinutes();
            if (totalMinutes <= 0) return 0.0;

            double totalHours = totalMinutes / 60.0;

            // Check if entry time falls in peak hours
            boolean isPeak = !entryLocalTime.isBefore(peakStart) && entryLocalTime.isBefore(peakEnd);
            double applicableHourlyRate = isPeak ? hourlyRate * peakMultiplier : hourlyRate;

            double calculatedFee = totalHours * applicableHourlyRate;

            // Apply daily maximum fee cap
            long days = duration.toDays();
            double maxFee = (days + 1) * dailyRate;

            return Math.min(calculatedFee, maxFee);
        }

        /**
         * Returns a human-readable summary of the pricing rule.
         *
         * @return String representation of the rule.
         */
        @Override
        public String toString() {
            return String.format("Rule[Hourly: $%.2f, Peak Multiplier: %.1fx (from %s to %s), Daily Max: $%.2f]",
                    hourlyRate, peakMultiplier, peakStart, peakEnd, dailyRate);
        }
    }
}

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages billing calculations and payment processing coordination.
 * Minimum 6 classes requirement met.
 */
class BillingSystem {
    private PaymentProcessor currentProcessor; // Strategy pattern: Can switch processor
    private final Map<String, DynamicPricingRule> zonePricingRules; // Zone ID -> Rule
    private final DynamicPricingRule defaultPricingRule;

    /**
     * Constructor for BillingSystem. Initializes with default pricing.
     */
    public BillingSystem() {
        this.zonePricingRules = new HashMap<>();
        // Default rule: $2.5/hour, peak multiplier 1.5x, daily max $20
        this.defaultPricingRule = new DynamicPricingRule(2.5, 1.5, 20.0);
        // Example: Set a specific rule for a "VIP" zone if it exists
        // addZonePricingRule("VIP", new DynamicPricingRule(5.0, 1.2, 40.0));
        System.out.println("Billing System Initialized.");
    }

    /**
     * Sets the active payment processor.
     *
     * @param processor The PaymentProcessor implementation to use.
     */
    public void setPaymentProcessor(PaymentProcessor processor) {
        this.currentProcessor = processor;
        System.out.println("Payment processor set to: " + (processor != null ? processor.getProcessorName() : "None"));
    }

    /**
     * Adds or updates a pricing rule for a specific zone.
     *
     * @param zoneId The ID of the zone.
     * @param rule   The DynamicPricingRule for the zone.
     */
    public void addZonePricingRule(String zoneId, DynamicPricingRule rule) {
        if (zoneId != null && rule != null) {
            this.zonePricingRules.put(zoneId, rule);
            System.out.println("Pricing rule updated for zone: " + zoneId);
        }
    }

    /**
     * Calculates the parking fee based on duration, vehicle type, spot type, and pricing rules.
     *
     * @param vehicle   The parked vehicle.
     * @param spot      The parking spot used.
     * @param entryTime Time of entry.
     * @param exitTime  Time of exit.
     * @param zone      The zone where the spot is located.
     * @return The calculated fee.
     */
    public double calculateFee(Vehicle vehicle, ParkingSpot spot, LocalDateTime entryTime, LocalDateTime exitTime, ParkingZone zone) {
        if (entryTime == null || exitTime == null || exitTime.isBefore(entryTime)) {
            System.err.println("Invalid entry/exit times for fee calculation.");
            return 0.0; // Or throw exception
        }

        Duration duration = Duration.between(entryTime, exitTime);
        long minutesParked = duration.toMinutes();

        if (minutesParked < 5) {
            return 0.0; // Grace period
        }

        // Determine pricing rule (zone specific or default)
        DynamicPricingRule rule = defaultPricingRule;
        if (zone != null && zonePricingRules.containsKey(zone.getZoneId())) {
            rule = zonePricingRules.get(zone.getZoneId());
            System.out.println("Using pricing rule for zone: " + zone.getZoneId());
        } else {
            System.out.println("Using default pricing rule.");
        }


        // Calculate fee using the rule
        double fee = rule.calculate(duration, entryTime.toLocalTime()); // Pass time for peak hour check

        // Apply vehicle type surcharge/discount (example)
        if (vehicle.getType() == VehicleType.TRUCK) {
            fee *= 1.2; // 20% surcharge for trucks
        } else if (vehicle.getType() == VehicleType.BIKE) {
            fee *= 0.7; // 30% discount for bikes
        }

        // Apply EV charging fee (example)
        if (spot.supportsCharging() && vehicle.getType() == VehicleType.ELECTRIC_VEHICLE) {
            fee += 5.0; // Flat fee for using charger
        }

        System.out.printf("Calculated fee for %s: $%.2f (Duration: %s)\n", vehicle.getLicensePlate(), fee, formatDuration(duration));
        return fee;
    }

    /**
     * Processes a payment using the currently set payment processor.
     *
     * @param amount The amount to pay.
     * @return true if payment is successful, false otherwise.
     * @throws PaymentException If no payment processor is set or payment fails.
     */
    public boolean processPayment(double amount) throws PaymentException {
        if (currentProcessor == null) {
            throw new PaymentException("No payment processor configured.");
        }
        if (amount <= 0) {
            System.out.println("Payment amount is zero or negative, no processing needed.");
            return true; // Nothing to pay
        }

        System.out.printf("Attempting payment of $%.2f using %s...\n", amount, currentProcessor.getProcessorName());
        boolean success = currentProcessor.processPayment(amount); // Delegate to processor

        if (!success) {
            throw new PaymentException("Payment failed using " + currentProcessor.getProcessorName());
        }

        System.out.println("Payment successful.");
        return true;
    }

    /**
     * Formats Duration into a readable string (e.g., "1h 30m").
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
     * Static Nested Class (IV) defining dynamic pricing rules.
     * Can be instantiated independently of BillingSystem.
     */
    public static class DynamicPricingRule {
        private final double hourlyRate;
        private final double peakMultiplier; // Applied during peak hours
        private final double dailyRate;      // Maximum charge per 24 hours
        private final java.time.LocalTime peakStart = java.time.LocalTime.of(8, 0); // 8 AM
        private final java.time.LocalTime peakEnd = java.time.LocalTime.of(18, 0); // 6 PM

        /**
         * Constructor for DynamicPricingRule.
         *
         * @param hourlyRate     Base rate per hour.
         * @param peakMultiplier Multiplier during peak hours (e.g., 1.5 for 50% extra).
         * @param dailyRate      Maximum daily charge.
         */
        public DynamicPricingRule(double hourlyRate, double peakMultiplier, double dailyRate) {
            this.hourlyRate = hourlyRate;
            this.peakMultiplier = peakMultiplier;
            this.dailyRate = dailyRate;
        }

        /**
         * Calculates the fee based on duration and time of day.
         *
         * @param duration       The parking duration.
         * @param entryLocalTime The time of day the parking started (for peak check).
         * @return Calculated fee.
         */
        public double calculate(Duration duration, java.time.LocalTime entryLocalTime) {
            long totalMinutes = duration.toMinutes();
            if (totalMinutes <= 0) return 0.0;

            double totalHours = totalMinutes / 60.0;

            // Determine applicable rate (peak or off-peak)
            // Simplified: Assume rate based on entry time for the whole duration
            boolean isPeak = !entryLocalTime.isBefore(peakStart) && entryLocalTime.isBefore(peakEnd);
            double applicableHourlyRate = isPeak ? hourlyRate * peakMultiplier : hourlyRate;

            double calculatedFee = totalHours * applicableHourlyRate;

            // Apply daily maximum
            long days = duration.toDays();
            double maxFee = (days + 1) * dailyRate; // Max fee for the number of days spanned

            return Math.min(calculatedFee, maxFee);
        }

        @Override
        public String toString() {
            return String.format("Rule[Hourly: $%.2f, Peak Multiplier: %.1fx (from %s to %s), Daily Max: $%.2f]",
                    hourlyRate, peakMultiplier, peakStart, peakEnd, dailyRate);
        }
    }
}

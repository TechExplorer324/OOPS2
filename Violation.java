import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a parking violation record.
 * Minimum 6 classes requirement met.
 * This class stores details about parking violations including the violation ID, vehicle license plate, spot, zone,
 * violation type, timestamp, penalty amount, and the status of payment.
 */
class Violation {
    private final String violationId;              // Unique identifier for the violation
    private final String vehicleLicensePlate;      // License plate of the vehicle involved in the violation
    private final String spotId;                   // ID of the parking spot where the violation occurred
    private final String zoneId;                   // ID of the parking zone where the violation occurred
    private final ViolationType type;              // Type of violation (e.g., illegal parking, expired meter)
    private final LocalDateTime timestamp;         // Date and time when the violation occurred
    private final Double penaltyAmount;            // Penalty amount for the violation (Wrapper class for handling nulls)
    private boolean isPaid;                        // Flag indicating whether the penalty has been paid

    /**
     * Constructor for creating a Violation object.
     * 
     * @param violationId The unique ID for the violation.
     * @param vehicleLicensePlate The license plate of the vehicle involved.
     * @param spotId The parking spot ID where the violation occurred.
     * @param zoneId The parking zone ID where the violation occurred.
     * @param type The type of the violation.
     * @param timestamp The timestamp when the violation was recorded.
     * @param penaltyAmount The amount of the penalty.
     */
    public Violation(String violationId, String vehicleLicensePlate, String spotId, String zoneId, ViolationType type, LocalDateTime timestamp, Double penaltyAmount) {
        this.violationId = violationId;
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.spotId = spotId;
        this.zoneId = zoneId;
        this.type = type;
        this.timestamp = timestamp;
        this.penaltyAmount = penaltyAmount; // Assigning value of Double wrapper class
        this.isPaid = false; // Initially, violation is not paid
    }

    // --- Getters ---
    
    /**
     * Gets the unique violation ID.
     * 
     * @return violationId
     */
    public String getViolationId() {
        return violationId;
    }

    /**
     * Gets the license plate of the vehicle involved in the violation.
     * 
     * @return vehicleLicensePlate
     */
    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    /**
     * Gets the ID of the parking spot where the violation occurred.
     * 
     * @return spotId
     */
    public String getSpotId() {
        return spotId;
    }

    /**
     * Gets the ID of the parking zone where the violation occurred.
     * 
     * @return zoneId
     */
    public String getZoneId() {
        return zoneId;
    }

    /**
     * Gets the type of the violation.
     * 
     * @return violationType
     */
    public ViolationType getType() {
        return type;
    }

    /**
     * Gets the timestamp when the violation was recorded.
     * 
     * @return timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the penalty amount associated with the violation.
     * 
     * @return penaltyAmount
     */
    public Double getPenaltyAmount() {
        return penaltyAmount; // Wrapper class example
    }

    /**
     * Checks whether the violation penalty has been paid.
     * 
     * @return true if paid, false otherwise
     */
    public boolean isPaid() {
        return isPaid;
    }

    // --- Setter ---
    
    /**
     * Marks the violation as paid.
     */
    public void markAsPaid() {
        this.isPaid = true;
    }

    /**
     * Formats the violation record into a string suitable for file logging.
     * This format includes all relevant violation details like ID, vehicle, spot, zone, type, time, penalty, and payment status.
     * 
     * @return Formatted string representation of the violation.
     */
    public String toLogFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return String.format("ViolationID: %s | Vehicle: %s | Spot: %s | Zone: %s | Type: %s | Time: %s | Penalty: $%.2f | Paid: %s",
                violationId, vehicleLicensePlate, spotId, zoneId, type, timestamp.format(formatter), penaltyAmount, isPaid);
    }

    @Override
    public String toString() {
        return toLogFormat(); // Return formatted string for easy display
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Violation violation = (Violation) o;
        // A violation is considered equal based on the violationId, ensuring unique records
        return Objects.equals(violationId, violation.violationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(violationId); // Hash based on violationId to maintain uniqueness
    }
    // Vararg method to mark multiple violations
    public static void markAllAsPaid(Violation... violations) {
        for (Violation v : violations) {
            v.markAsPaid();
    }
}
    public static class ViolationLogger {
    public static void log(Violation violation) {
        System.out.println("Logging Violation: " + violation.toLogFormat());
    }
}
}

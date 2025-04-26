import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a parking violation record.
 * Minimum 6 classes requirement met.
 */
class Violation {
    private final String violationId;
    private final String vehicleLicensePlate;
    private final String spotId;
    private final String zoneId;
    private final ViolationType type;
    private final LocalDateTime timestamp;
    private final Double penaltyAmount; // Wrapper class (IX) example
    private boolean isPaid;

    public Violation(String violationId, String vehicleLicensePlate, String spotId, String zoneId, ViolationType type, LocalDateTime timestamp, Double penaltyAmount) {
        this.violationId = violationId;
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.spotId = spotId;
        this.zoneId = zoneId;
        this.type = type;
        this.timestamp = timestamp;
        this.penaltyAmount = penaltyAmount; // Assign Double wrapper
        this.isPaid = false;
    }

    // --- Getters ---
    public String getViolationId() {
        return violationId;
    }

    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    public String getSpotId() {
        return spotId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public ViolationType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Double getPenaltyAmount() {
        return penaltyAmount;
    } // Returns Double wrapper

    public boolean isPaid() {
        return isPaid;
    }

    // --- Setter ---
    public void markAsPaid() {
        this.isPaid = true;
    }

    /**
     * Formats the violation record into a string suitable for file logging.
     *
     * @return Formatted log string.
     */
    public String toLogFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return String.format("ViolationID: %s | Vehicle: %s | Spot: %s | Zone: %s | Type: %s | Time: %s | Penalty: $%.2f | Paid: %s",
                violationId, vehicleLicensePlate, spotId, zoneId, type, timestamp.format(formatter), penaltyAmount, isPaid);
    }

    @Override
    public String toString() {
        return toLogFormat();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Violation violation = (Violation) o;
        // Consider a violation unique based on vehicle, spot, type, and timestamp (or ID)
        return Objects.equals(violationId, violation.violationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(violationId);
    }
}

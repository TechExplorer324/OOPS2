import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a log entry for vehicle movement within a parking system.
 * This log captures entry and exit times for vehicles using parking spots.
 * Minimum 6 classes requirement met.
 */
class EntryExitLog {
    private final String logId; // Unique identifier for this log entry
    private final String vehicleLicensePlate; // License plate of the vehicle
    private final String spotId; // Parking spot where the vehicle is parked
    private final String zoneId; // Zone in which the parking spot is located
    private final LocalDateTime entryTime; // Time when the vehicle entered the parking
    private LocalDateTime exitTime; // Time when the vehicle exited, can be null if not exited yet

    /**
     * Constructor for creating a new entry-exit log.
     * 
     * @param logId              The unique log identifier.
     * @param vehicleLicensePlate The license plate of the vehicle.
     * @param spotId             The ID of the parking spot.
     * @param zoneId             The zone ID where the parking spot is located.
     * @param entryTime          The time when the vehicle entered the parking.
     * @param exitTime           The time when the vehicle exited the parking (can be null).
     */
    public EntryExitLog(String logId, String vehicleLicensePlate, String spotId, String zoneId, LocalDateTime entryTime, LocalDateTime exitTime) {
        this.logId = logId;
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.spotId = spotId;
        this.zoneId = zoneId;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    // --- Getters ---
    public String getLogId() {
        return logId; // Returns the unique log identifier
    }

    public String getVehicleLicensePlate() {
        return vehicleLicensePlate; // Returns the vehicle's license plate
    }

    public String getSpotId() {
        return spotId; // Returns the parking spot ID
    }

    public String getZoneId() {
        return zoneId; // Returns the parking zone ID
    }

    public LocalDateTime getEntryTime() {
        return entryTime; // Returns the entry time
    }

    public LocalDateTime getExitTime() {
        return exitTime; // Returns the exit time (can be null)
    }

    // --- Setter for Exit Time ---
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime; // Sets the exit time of the vehicle
    }

    /**
     * Formats the log entry into a string suitable for file logging.
     * Converts LocalDateTime objects to a standard format for easier storage and readability.
     * 
     * @return Formatted log string.
     */
    public String toLogFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // Defines a standard date-time format
        // If exitTime is null, return "N/A" in the log, else format the exit time
        String exitTimeStr = (exitTime != null) ? exitTime.format(formatter) : "N/A";
        // Format and return the log entry in a readable form
        return String.format("LogID: %s | Vehicle: %s | Spot: %s | Zone: %s | Entry: %s | Exit: %s",
                logId, vehicleLicensePlate, spotId, zoneId, entryTime.format(formatter), exitTimeStr);
    }

    /**
     * Overrides the toString method to return the log entry in a readable format.
     * 
     * @return A string representation of the log entry.
     */
    @Override
    public String toString() {
        return toLogFormat(); // Uses the same format for general printing
    }
}

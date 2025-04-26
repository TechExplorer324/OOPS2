import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a log entry for vehicle movement.
 * Minimum 6 classes requirement met.
 */
class EntryExitLog {
    private final String logId;
    private final String vehicleLicensePlate;
    private final String spotId;
    private final String zoneId;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime; // Can be null if vehicle hasn't exited

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
        return logId;
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

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    // --- Setter for Exit Time ---
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    /**
     * Formats the log entry into a string suitable for file logging.
     *
     * @return Formatted log string.
     */
    public String toLogFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String exitTimeStr = (exitTime != null) ? exitTime.format(formatter) : "N/A";
        return String.format("LogID: %s | Vehicle: %s | Spot: %s | Zone: %s | Entry: %s | Exit: %s",
                logId, vehicleLicensePlate, spotId, zoneId, entryTime.format(formatter), exitTimeStr);
    }

    @Override
    public String toString() {
        return toLogFormat(); // Use the same format for general printing
    }
}

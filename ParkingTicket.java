import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a parking ticket issued upon exit.
 * Minimum 6 classes requirement met.
 */
class ParkingTicket {
    private final String ticketId;
    private final String vehicleLicensePlate;
    private final String spotId;
    private final String zoneId;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;
    private final Double fee; // Wrapper class (IX) example

    public ParkingTicket(String ticketId, String vehicleLicensePlate, String spotId, String zoneId, LocalDateTime entryTime, LocalDateTime exitTime, Double fee) {
        this.ticketId = ticketId;
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.spotId = spotId;
        this.zoneId = zoneId;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.fee = fee; // Assign Double wrapper
    }

    // --- Getters ---
    public String getTicketId() {
        return ticketId;
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

    public Double getFee() {
        return fee;
    } // Returns Double wrapper

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Duration duration = Duration.between(entryTime, exitTime);
        return String.format("--- Parking Ticket ---\n" +
                        " Ticket ID: %s\n" +
                        " Vehicle:   %s\n" +
                        " Spot:      %s (Zone: %s)\n" +
                        " Entry:     %s\n" +
                        " Exit:      %s\n" +
                        " Duration:  %s\n" +
                        " Fee:       $%.2f\n" +
                        "----------------------",
                ticketId, vehicleLicensePlate, spotId, zoneId,
                entryTime.format(formatter), exitTime.format(formatter),
                formatDuration(duration), fee);
    }

    /**
     * Helper to format Duration.
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
}

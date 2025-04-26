import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a parking ticket issued upon exit.
 * Minimum 6 classes requirement met.
 */
class ParkingTicket {
    private final String ticketId; // Unique identifier for the ticket
    private final String vehicleLicensePlate; // License plate of the vehicle
    private final String spotId; // ID of the parking spot
    private final String zoneId; // ID of the parking zone
    private final LocalDateTime entryTime; // Time when the vehicle entered the parking spot
    private final LocalDateTime exitTime; // Time when the vehicle exited the parking spot
    private final Double fee; // Parking fee, wrapped in a Double object (Wrapper class IX example)

    /**
     * Constructor to initialize the ParkingTicket with necessary details.
     *
     * @param ticketId            Unique identifier for the ticket
     * @param vehicleLicensePlate License plate of the vehicle
     * @param spotId             Parking spot ID
     * @param zoneId             Zone ID where the spot is located
     * @param entryTime          Time when the vehicle entered the parking spot
     * @param exitTime           Time when the vehicle exited the parking spot
     * @param fee                Parking fee
     */
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
        return ticketId; // Return ticket ID
    }

    public String getVehicleLicensePlate() {
        return vehicleLicensePlate; // Return vehicle's license plate
    }

    public String getSpotId() {
        return spotId; // Return the parking spot ID
    }

    public String getZoneId() {
        return zoneId; // Return the zone ID where the parking spot is located
    }

    public LocalDateTime getEntryTime() {
        return entryTime; // Return the entry time of the vehicle
    }

    public LocalDateTime getExitTime() {
        return exitTime; // Return the exit time of the vehicle
    }

    public Double getFee() {
        return fee; // Return the parking fee
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // Define format for date and time display
        Duration duration = Duration.between(entryTime, exitTime); // Calculate the duration between entry and exit time
        
        // Format and return a string representation of the parking ticket details
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
     * Helper method to format the Duration object into hours and minutes.
     *
     * @param duration The duration between entry and exit time
     * @return A formatted string representing hours and minutes
     */
    private String formatDuration(Duration duration) {
        long hours = duration.toHours(); // Get the hours part of the duration
        long minutes = duration.toMinutesPart(); // Get the minutes part of the duration
        if (hours > 0) {
            return hours + "h " + minutes + "m"; // Return in "h m" format
        } else {
            return minutes + "m"; // Return in "m" format if there are no hours
        }
    }
}

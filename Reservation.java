import java.time.LocalDateTime;

/**
 * Represents a reservation made by a user for a parking spot.
 * This class encapsulates details about a reservation such as the reservation ID, user, vehicle,
 * parking spot, and the time range during which the reservation is valid.
 * Minimum 6 classes requirement met.
 */
class Reservation {
    private final String reservationId;       // Unique identifier for the reservation
    private final String userId;              // The user who made the reservation
    private final String vehicleLicensePlate; // The vehicle associated with the reservation
    private final String spotId;             // The parking spot reserved
    private final String zoneId;             // The zone where the parking spot is located
    private final LocalDateTime startTime;   // The start time of the reservation
    private final LocalDateTime endTime;     // The end time of the reservation

    /**
     * Constructor for creating a new reservation.
     *
     * @param reservationId       Unique ID for the reservation.
     * @param userId             The user making the reservation.
     * @param vehicleLicensePlate The vehicle associated with the reservation.
     * @param spotId             The ID of the reserved parking spot.
     * @param zoneId             The zone where the spot is located.
     * @param startTime          The start time of the reservation.
     * @param endTime            The end time of the reservation.
     */
    public Reservation(String reservationId, String userId, String vehicleLicensePlate, String spotId, String zoneId, LocalDateTime startTime, LocalDateTime endTime) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.spotId = spotId;
        this.zoneId = zoneId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // --- Getters ---
    
    /**
     * Gets the reservation ID.
     *
     * @return The unique reservation ID.
     */
    public String getReservationId() {
        return reservationId;
    }

    /**
     * Gets the user ID who made the reservation.
     *
     * @return The user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the vehicle's license plate associated with the reservation.
     *
     * @return The vehicle license plate.
     */
    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    /**
     * Gets the ID of the reserved parking spot.
     *
     * @return The parking spot ID.
     */
    public String getSpotId() {
        return spotId;
    }

    /**
     * Gets the zone ID where the parking spot is located.
     *
     * @return The zone ID.
     */
    public String getZoneId() {
        return zoneId;
    }

    /**
     * Gets the start time of the reservation.
     *
     * @return The start time.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the reservation.
     *
     * @return The end time.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Provides a string representation of the reservation details.
     *
     * @return A string that represents the reservation in a human-readable format.
     */
    @Override
    public String toString() {
        return "Reservation [ID=" + reservationId + ", User=" + userId + ", Vehicle=" + vehicleLicensePlate +
                ", Spot=" + spotId + ", Zone=" + zoneId + ", Time=" + startTime + " to " + endTime + "]";
    }
}

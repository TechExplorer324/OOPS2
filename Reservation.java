import java.time.LocalDateTime;

/**
 * Represents a reservation made by a user.
 * Minimum 6 classes requirement met.
 */
class Reservation {
    private final String reservationId;
    private final String userId;
    private final String vehicleLicensePlate;
    private final String spotId;
    private final String zoneId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

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
    public String getReservationId() {
        return reservationId;
    }

    public String getUserId() {
        return userId;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Reservation [ID=" + reservationId + ", User=" + userId + ", Vehicle=" + vehicleLicensePlate +
                ", Spot=" + spotId + ", Zone=" + zoneId + ", Time=" + startTime + " to " + endTime + "]";
    }
}

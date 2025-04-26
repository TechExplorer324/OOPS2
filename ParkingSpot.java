import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single parking spot.
 */
class ParkingSpot {
    private final String spotId;         // Unique identifier for the spot (e.g., "A101")
    private final SpotType spotType;     // Type of the spot
    private boolean isOccupied;      // Current occupancy status
    private boolean isReserved;      // Current reservation status
    private Vehicle parkedVehicle;   // Vehicle currently parked (if occupied)
    private LocalDateTime reservedUntil; // Timestamp until which the spot is reserved
    private final boolean isChargingAvailable; // Does the spot have EV charging? (Wrapper IX used implicitly)

    /**
     * Constructor (II) for a standard parking spot.
     *
     * @param spotId Unique ID.
     * @param type   Spot type.
     */
    public ParkingSpot(String spotId, SpotType type) {
        this(spotId, type, type == SpotType.ELECTRIC_CHARGING); // Delegate to overloaded constructor
    }

    /**
     * Overloaded Constructor (II) for specifying charging availability explicitly.
     *
     * @param spotId              Unique ID.
     * @param type                Spot type.
     * @param isChargingAvailable True if EV charging is available.
     */
    public ParkingSpot(String spotId, SpotType type, boolean isChargingAvailable) {
        if (spotId == null || spotId.trim().isEmpty()) {
            throw new IllegalArgumentException("Spot ID cannot be null or empty.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Spot type cannot be null.");
        }
        this.spotId = spotId;
        this.spotType = type;
        this.isChargingAvailable = isChargingAvailable;
        this.isOccupied = false;
        this.isReserved = false;
        this.parkedVehicle = null;
        this.reservedUntil = null;
    }

    /**
     * Checks if a vehicle type is compatible with this spot type.
     *
     * @param vehicleType The type of the vehicle.
     * @return true if compatible, false otherwise.
     */
    public boolean isCompatible(VehicleType vehicleType) {
        switch (this.spotType) {
            case COMPACT:
                return vehicleType == VehicleType.CAR || vehicleType == VehicleType.BIKE;
            case REGULAR:
                return vehicleType == VehicleType.CAR || vehicleType == VehicleType.ELECTRIC_VEHICLE;
            case LARGE:
                return vehicleType == VehicleType.TRUCK || vehicleType == VehicleType.CAR;
            case MOTORBIKE:
                return vehicleType == VehicleType.BIKE;
            case ELECTRIC_CHARGING:
                return vehicleType == VehicleType.ELECTRIC_VEHICLE || vehicleType == VehicleType.CAR; // Assuming any car *could* use it, but EVs preferred
            default:
                return false;
        }
    }

    /**
     * Occupies the spot with a vehicle.
     *
     * @param vehicle The vehicle parking in the spot.
     * @return true if successfully occupied, false otherwise (e.g., already occupied).
     */
    public synchronized boolean occupy(Vehicle vehicle) {
        if (!isOccupied && !isReserved && isCompatible(vehicle.getType())) {
            this.isOccupied = true;
            this.parkedVehicle = vehicle;
            this.isReserved = false; // Occupying cancels any lingering reservation
            this.reservedUntil = null;
            System.out.println("Spot " + spotId + " occupied by " + vehicle.getLicensePlate());
            return true;
        }
        System.out.println("Failed to occupy spot " + spotId + ". Available: " + !isOccupied + ", Reserved: " + isReserved + ", Compatible: " + (vehicle != null && isCompatible(vehicle.getType())));
        return false;
    }

    /**
     * Vacates the spot.
     *
     * @return The vehicle that was parked, or null if it was already empty.
     */
    public synchronized Vehicle vacate() {
        if (isOccupied) {
            Vehicle leavingVehicle = this.parkedVehicle;
            this.isOccupied = false;
            this.parkedVehicle = null;
            System.out.println("Spot " + spotId + " vacated by " + (leavingVehicle != null ? leavingVehicle.getLicensePlate() : "N/A"));
            return leavingVehicle;
        }
        return null;
    }

    /**
     * Reserves the spot until a specific time.
     *
     * @param until The time until which the spot is reserved.
     * @return true if successfully reserved, false otherwise (e.g., occupied or already reserved).
     */
    public synchronized boolean reserve(LocalDateTime until) {
        if (!isOccupied && !isReserved) {
            this.isReserved = true;
            this.reservedUntil = until;
            System.out.println("Spot " + spotId + " reserved until " + until.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            return true;
        }
        return false;
    }

    /**
     * Cancels the current reservation on the spot.
     */
    public synchronized void cancelReservation() {
        if (isReserved) {
            this.isReserved = false;
            this.reservedUntil = null;
            System.out.println("Reservation cancelled for spot " + spotId);
        }
    }

    // --- Getters ---
    public String getId() {
        return spotId;
    }

    public SpotType getType() {
        return spotType;
    }

    public synchronized boolean isOccupied() {
        return isOccupied;
    }

    public synchronized boolean isReserved() {
        return isReserved;
    }

    public synchronized Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public synchronized LocalDateTime getReservedUntil() {
        return reservedUntil;
    }

    public boolean supportsCharging() {
        return isChargingAvailable;
    }

    /**
     * Checks if the spot is currently available (neither occupied nor reserved).
     * Also checks if an existing reservation has expired.
     *
     * @return true if available, false otherwise.
     */
    public synchronized boolean isAvailable() {
        if (isReserved && reservedUntil != null && LocalDateTime.now().isAfter(reservedUntil)) {
            // Reservation expired
            cancelReservation();
        }
        return !isOccupied && !isReserved;
    }

    @Override
    public String toString() {
        String status = isAvailable() ? "Available" : (isOccupied ? "Occupied by " + (parkedVehicle != null ? parkedVehicle.getLicensePlate() : "N/A") : "Reserved");
        return "Spot [ID=" + spotId + ", Type=" + spotType + ", Charging=" + isChargingAvailable + ", Status=" + status + "]";
    }
}

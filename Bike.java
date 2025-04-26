/**
 * Represents a Bike/Motorcycle.
 */
class Bike extends Vehicle {
    public Bike(String licensePlate, User owner) {
        super(licensePlate, VehicleType.BIKE, owner);
    }

    @Override
    public SpotType getRequiredSpotType() {
        return SpotType.MOTORBIKE;
    }
}
/**
 * Represents a Bike or Motorcycle in the system.
 * Inherits from the Vehicle class.
 */
class Bike extends Vehicle {

    /**
     * Constructor for Bike.
     *
     * @param licensePlate The license plate number of the bike.
     * @param owner The User who owns the bike.
     */
    public Bike(String licensePlate, User owner) {
        // Call the parent (Vehicle) constructor with license plate, vehicle type, and owner
        super(licensePlate, VehicleType.BIKE, owner);
    }

    /**
     * Returns the type of parking spot required for this vehicle.
     * Bikes require a MOTORBIKE type spot.
     *
     * @return SpotType.MOTORBIKE
     */
    @Override
    public SpotType getRequiredSpotType() {
        return SpotType.MOTORBIKE;
    }
}

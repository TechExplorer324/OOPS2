/**
 * Represents a Bike or Motorcycle in the parking system.
 * Inherits from the Vehicle class and specifies behavior unique to bikes.
 */
class Bike extends Vehicle {

    /**
     * Constructor to create a Bike object.
     *
     * @param licensePlate The license plate number of the bike.
     * @param owner The owner of the bike (a User object).
     */
    public Bike(String licensePlate, User owner) {
        // Initialize the Bike by calling the parent (Vehicle) constructor
        super(licensePlate, VehicleType.BIKE, owner);
    }

    /**
     * Specifies the type of parking spot required for the bike.
     *
     * @return SpotType.MOTORBIKE indicating that a motorbike spot is needed.
     */
    @Override
    public SpotType getRequiredSpotType() {
        return SpotType.MOTORBIKE;
    }
}

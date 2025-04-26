/**
 * Represents a Truck.
 * Extends the Vehicle class and specifies the required parking spot type as LARGE.
 */
class Truck extends Vehicle {

    /**
     * Constructor for creating a new Truck object.
     * @param licensePlate The license plate of the truck.
     * @param owner The user who owns the truck.
     */
    public Truck(String licensePlate, User owner) {
        super(licensePlate, VehicleType.TRUCK, owner);  // Calls the constructor of the Vehicle class
    }

    /**
     * Returns the required spot type for the truck.
     * Trucks require large parking spots.
     * @return SpotType.LARGE
     */
    @Override
    public SpotType getRequiredSpotType() {
        return SpotType.LARGE;  // Specifies that the truck needs a large spot
    }
}

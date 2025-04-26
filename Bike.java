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

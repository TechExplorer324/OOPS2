/**
 * Represents a Truck.
 */
class Truck extends Vehicle {
    public Truck(String licensePlate, User owner) {
        super(licensePlate, VehicleType.TRUCK, owner);
    }

    @Override
    public SpotType getRequiredSpotType() {
        return SpotType.LARGE;
    }
}

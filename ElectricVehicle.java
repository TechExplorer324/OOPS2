/**
 * Represents an Electric Vehicle.
 */
class ElectricVehicle extends Vehicle {
    public ElectricVehicle(String licensePlate, User owner) {
        super(licensePlate, VehicleType.ELECTRIC_VEHICLE, owner);
    }

    @Override
    public SpotType getRequiredSpotType() {
        return SpotType.ELECTRIC_CHARGING; // Prefers a charging spot
    }
}

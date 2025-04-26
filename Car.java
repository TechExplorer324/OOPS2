/**
 * Represents a Car.
 */
class Car extends Vehicle {
    public Car(String licensePlate, User owner) {
        super(licensePlate, VehicleType.CAR, owner);
    }

    @Override
    public SpotType getRequiredSpotType() {
        // Cars can fit in Compact, Regular, Large, or EV spots if the car is electric (simplified here)
        return SpotType.REGULAR; // Default preference
    }
}

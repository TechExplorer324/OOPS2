/**
 * Represents a Car, a type of Vehicle.
 * Inherits from the Vehicle class.
 */
class Car extends Vehicle {

    /**
     * Constructor for the Car class.
     *
     * @param licensePlate The license plate of the car.
     * @param owner        The owner of the car.
     */
    public Car(String licensePlate, User owner) {
        super(licensePlate, VehicleType.CAR, owner); // Calls the parent constructor to initialize the vehicle type as CAR
    }

    /**
     * Returns the required parking spot type for the car.
     * 
     * @return SpotType The preferred type of parking spot for the car.
     * 
     * Cars can fit in Compact, Regular, Large, or EV spots if the car is electric.
     * Here, we simplify by returning the default preference as REGULAR.
     */
    @Override
    public SpotType getRequiredSpotType() {
        return SpotType.REGULAR; // Default preference for parking spot type (can be modified based on actual requirements)
    }
}

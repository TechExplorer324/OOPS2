/**
 * Represents an Electric Vehicle, a specialized type of Vehicle.
 * Inherits from the Vehicle class.
 */
class ElectricVehicle extends Vehicle {

    /**
     * Constructor for the ElectricVehicle class.
     * 
     * @param licensePlate The license plate of the electric vehicle.
     * @param owner        The owner of the electric vehicle.
     */
    public ElectricVehicle(String licensePlate, User owner) {
        super(licensePlate, VehicleType.ELECTRIC_VEHICLE, owner); // Calls the parent constructor to initialize the vehicle type as ELECTRIC_VEHICLE
    }

    /**
     * Returns the required parking spot type for the electric vehicle.
     * 
     * @return SpotType The preferred type of parking spot for the electric vehicle.
     * 
     * Electric vehicles require a parking spot with charging facilities.
     * This method ensures that the vehicle prefers an Electric Charging spot.
     */
    @Override
    public SpotType getRequiredSpotType() {
        return SpotType.ELECTRIC_CHARGING; // Electric vehicles prefer charging spots to charge their batteries
    }
}

import java.util.Objects;

/**
 * Abstract class (V) representing a generic vehicle.
 * Demonstrates Hierarchical Inheritance (VII) as specific types of vehicles (e.g., Truck, Car) can extend this class.
 */
abstract class Vehicle {
    protected String licensePlate; // Vehicle identifier (license plate number)
    protected VehicleType type;    // Type of vehicle (e.g., Truck, Car, etc.)
    protected User owner;          // The user associated with the vehicle (owner)

    /**
     * Constructor for Vehicle.
     * Initializes the vehicle's license plate, type, and owner.
     * 
     * @param licensePlate The unique license plate number.
     * @param type The type of the vehicle (e.g., TRUCK, CAR).
     * @param owner The user who owns or registers the vehicle.
     */
    public Vehicle(String licensePlate, VehicleType type, User owner) {
        this.licensePlate = licensePlate;
        this.type = type;
        this.owner = owner;
    }

    /**
     * Gets the license plate of the vehicle.
     * 
     * @return The license plate string of the vehicle.
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Gets the type of the vehicle.
     * 
     * @return The VehicleType enum value representing the vehicle's type.
     */
    public VehicleType getType() {
        return type;
    }

    /**
     * Gets the owner of the vehicle.
     * 
     * @return The User object representing the owner of the vehicle.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Abstract method that determines the type of parking spot required for this vehicle.
     * This method must be implemented by subclasses of Vehicle (e.g., Truck, Car) to return the 
     * appropriate SpotType (e.g., SMALL, MEDIUM, LARGE).
     * 
     * @return The required SpotType for the vehicle.
     */
    public abstract SpotType getRequiredSpotType();

    /**
     * Provides a string representation of the vehicle.
     * 
     * @return A string representing the vehicle's type and license plate.
     */
    @Override
    public String toString() {
        return type + " [" + licensePlate + "]";
    }

    /**
     * Checks if two vehicles are equal based on their license plates.
     * 
     * @param o The object to compare this vehicle with.
     * @return True if the license plates are the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(licensePlate, vehicle.licensePlate);
    }

    /**
     * Generates a hash code for the vehicle based on its license plate.
     * 
     * @return The hash code of the vehicle.
     */
    @Override
    public int hashCode() {
        return Objects.hash(licensePlate);
    }
}

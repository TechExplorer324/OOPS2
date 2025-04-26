/**
 * Abstract class (V) representing a generic vehicle.
 * Demonstrates Hierarchical Inheritance (VII).
 */

import java.util.Objects;


abstract class Vehicle {
    protected String licensePlate; // Vehicle identifier
    protected VehicleType type;    // Type of vehicle
    protected User owner;          // User associated with the vehicle

    /**
     * Constructor for Vehicle.
     * @param licensePlate The unique license plate number.
     * @param type The type of the vehicle.
     * @param owner The user who owns/registers the vehicle.
     */
    public Vehicle(String licensePlate, VehicleType type, User owner) {
        this.licensePlate = licensePlate;
        this.type = type;
        this.owner = owner;
    }

    /**
     * Gets the license plate of the vehicle.
     * @return License plate string.
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Gets the type of the vehicle.
     * @return VehicleType enum value.
     */
    public VehicleType getType() {
        return type;
    }

    /**
     * Gets the owner of the vehicle.
     * @return User object.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Abstract method to determine the suitable spot type for this vehicle.
     * Must be implemented by subclasses.
     * @return The required SpotType.
     */
    public abstract SpotType getRequiredSpotType();

    @Override
    public String toString() {
        return type + " [" + licensePlate + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(licensePlate, vehicle.licensePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licensePlate);
    }
}
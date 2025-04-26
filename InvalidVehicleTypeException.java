/**
 * Custom Exception (XI) for invalid vehicle types or operations.
 */
class InvalidVehicleTypeException extends Exception {
    public InvalidVehicleTypeException(String message) {
        super(message);
    }
}

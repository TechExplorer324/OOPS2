/**
 * Enumeration for different types of parking spots.
 */
enum SpotType {
    COMPACT,     // Suitable for CAR, BIKE
    REGULAR,     // Suitable for CAR, ELECTRIC_VEHICLE
    LARGE,       // Suitable for TRUCK, CAR
    MOTORBIKE,   // Suitable only for BIKE
    ELECTRIC_CHARGING // Suitable for ELECTRIC_VEHICLE, CAR (if EV)
}

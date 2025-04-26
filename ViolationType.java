/**
 * Enumeration for different types of parking violations.
 * This enum defines the possible violation types that can occur in the parking system.
 * Each violation type represents a specific rule that can be violated by a vehicle in the parking system.
 */
enum ViolationType {
    OVERSTAY,            // Violation for staying beyond the allowed parking time
    UNAUTHORIZED_ZONE,   // Violation for parking in a restricted or unauthorized zone
    INVALID_SPOT_TYPE    // Violation for parking in a spot not designated for the vehicle's type (e.g., truck in a car spot)
}

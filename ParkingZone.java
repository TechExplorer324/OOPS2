import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a zone within the parking lot (e.g., VIP, Staff, General).
 */
class ParkingZone {
    private final String zoneId;             // Unique identifier for the zone (e.g., "ZoneA")
    private final String zoneName;           // Descriptive name (e.g., "Visitor Parking")
    private final List<SpotType> allowedSpotTypes; // Types of spots allowed in this zone
    private final List<ParkingSpot> spots;   // List of spots within this zone
    private BillingSystem.DynamicPricingRule pricingRule; // Zone-specific pricing

    /**
     * Constructor for ParkingZone.
     *
     * @param zoneId   Unique ID.
     * @param zoneName Descriptive name.
     */
    public ParkingZone(String zoneId, String zoneName) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.spots = new ArrayList<>();
        this.allowedSpotTypes = new ArrayList<>(); // Can be configured later
        this.pricingRule = null; // Default, can be set
    }

    /**
     * Adds a single parking spot to the zone.
     *
     * @param spot The ParkingSpot to add.
     */
    public void addSpot(ParkingSpot spot) {
        if (spot != null) {
            this.spots.add(spot);
            if (!allowedSpotTypes.contains(spot.getType())) {
                allowedSpotTypes.add(spot.getType());
            }
        }
    }

    /**
     * Adds multiple parking spots using varargs (III).
     *
     * @param newSpots Variable number of ParkingSpot objects.
     */
    public void addSpots(ParkingSpot... newSpots) {
        if (newSpots != null) {
            for (ParkingSpot spot : newSpots) {
                addSpot(spot); // Delegate to single addSpot method
            }
        }
    }

    /**
     * Overloaded varargs method (III) to add a specified quantity of a certain spot type.
     * Note: This is a simplified example; real implementation might need more complex ID generation.
     * For demonstration, it uses zoneId + type + index for IDs.
     *
     * @param quantity The number of spots to add.
     * @param type     The type of spots to add.
     */
    public void addSpots(int quantity, SpotType type) {
        if (quantity <= 0) return;
        int currentSize = spots.size();
        for (int i = 0; i < quantity; i++) {
            String spotId = zoneId + "-" + type.name().charAt(0) + (currentSize + i + 1);
            addSpot(new ParkingSpot(spotId, type));
        }
        System.out.println("Added " + quantity + " spots of type " + type + " to zone " + zoneId);
    }


    /**
     * Finds the first available spot of the required type in this zone.
     *
     * @param requiredType The type of spot needed.
     * @return An available ParkingSpot, or null if none found.
     */
    public synchronized ParkingSpot findAvailableSpot(SpotType requiredType) {
        for (ParkingSpot spot : spots) {
            // Check compatibility first, then availability
            if (spot.isCompatible(VehicleType.valueOf(requiredType.name())) && spot.isAvailable()) { // Simplified mapping for check
                // Check if the spot *type* itself matches, or if it's a compatible type (e.g., Regular for Car)
                // This logic might need refinement based on exact rules (e.g., prefer exact match first)
                if (spot.getType() == requiredType || spot.isCompatible(VehicleType.valueOf(requiredType.name()))) { // Basic check
                    return spot;
                }
            }
        }
        // If exact type not found, maybe try compatible types? (More complex logic)
        // For now, return null if exact or basic compatible not found available.
        return null;
    }

    /**
     * Finds the first available spot compatible with the given vehicle type.
     *
     * @param vehicleType The type of the vehicle.
     * @return An available ParkingSpot compatible with the vehicle, or null if none found.
     */
    public synchronized ParkingSpot findAvailableSpot(VehicleType vehicleType) {
        for (ParkingSpot spot : spots) {
            if (spot.isCompatible(vehicleType) && spot.isAvailable()) {
                return spot;
            }
        }
        return null;
    }


    /**
     * Gets a list of all spots in this zone.
     *
     * @return Unmodifiable list of spots.
     */
    public List<ParkingSpot> getAllSpots() {
        return Collections.unmodifiableList(spots);
    }

    /**
     * Gets the zone ID.
     *
     * @return Zone ID string.
     */
    public String getZoneId() {
        return zoneId;
    }

    /**
     * Gets the zone name.
     *
     * @return Zone name string.
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * Sets the pricing rule for this zone.
     *
     * @param rule The DynamicPricingRule object.
     */
    public void setPricingRule(BillingSystem.DynamicPricingRule rule) {
        this.pricingRule = rule;
    }

    /**
     * Gets the pricing rule for this zone.
     *
     * @return DynamicPricingRule object, or null if not set.
     */
    public BillingSystem.DynamicPricingRule getPricingRule() {
        return pricingRule;
    }

    /**
     * Counts the number of available spots of a specific type.
     *
     * @param type The SpotType to count.
     * @return The count of available spots.
     */
    public synchronized long getAvailableCount(SpotType type) {
        return spots.stream()
                .filter(spot -> spot.getType() == type && spot.isAvailable())
                .count();
    }

    /**
     * Counts the total number of available spots compatible with a vehicle type.
     *
     * @param vehicleType The VehicleType to check compatibility for.
     * @return The count of available compatible spots.
     */
    public synchronized long getAvailableCompatibleCount(VehicleType vehicleType) {
        return spots.stream()
                .filter(spot -> spot.isCompatible(vehicleType) && spot.isAvailable())
                .count();
    }


    @Override
    public String toString() {
        long availableCount = spots.stream().filter(ParkingSpot::isAvailable).count();
        return "Zone [ID=" + zoneId + ", Name=" + zoneName + ", Total Spots=" + spots.size() + ", Available=" + availableCount + "]";
    }
}

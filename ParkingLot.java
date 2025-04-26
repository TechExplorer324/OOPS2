import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Represents the entire Parking Lot facility.
 * Manages zones, spots, logs, billing, etc.
 * Minimum 6 classes requirement met (this is one of them).
 */
class ParkingLot {
    private final String name;
    private final Map<String, ParkingZone> zones; // Key: Zone ID
    private final List<EntryExitLog> entryExitLogs;
    private final List<Violation> violations;
    private final Map<String, Reservation> activeReservations; // Key: Reservation ID
    private final Map<String, Queue<User>> waitlist; // Key: Zone ID -> Queue of Users waiting
    private final BillingSystem billingSystem;
    private final LoyaltyProgram loyaltyProgram;
    private final NotificationService notificationService;
    private final LogManager logManager;
    private final Map<String, ParkingSpot> occupiedSpotsMap; // Key: License Plate -> Spot (for quick lookup on exit)
    private final Map<String, LocalDateTime> entryTimeMap; // Key: License Plate -> Entry Time

    /**
     * Constructor for ParkingLot.
     *
     * @param name Name of the parking lot.
     */
    public ParkingLot(String name) {
        this.name = name;
        this.zones = new ConcurrentHashMap<>(); // Use concurrent map for potential future threading
        this.entryExitLogs = Collections.synchronizedList(new ArrayList<>()); // Thread-safe list
        this.violations = Collections.synchronizedList(new ArrayList<>());
        this.activeReservations = new ConcurrentHashMap<>();
        this.waitlist = new ConcurrentHashMap<>();
        this.billingSystem = new BillingSystem(); // Initialize subsystems
        this.loyaltyProgram = new LoyaltyProgram();
        this.notificationService = new NotificationService();
        this.logManager = new LogManager(); // Initialize log manager
        this.occupiedSpotsMap = new ConcurrentHashMap<>();
        this.entryTimeMap = new ConcurrentHashMap<>();

        System.out.println("Parking Lot '" + name + "' initialized.");
        logManager.writeLog("system.log", "Parking Lot '" + name + "' initialized at " + LocalDateTime.now());
    }

    /**
     * Adds a new zone to the parking lot.
     *
     * @param zone The ParkingZone to add.
     */
    public void addZone(ParkingZone zone) {
        if (zone != null) {
            this.zones.put(zone.getZoneId(), zone);
            this.waitlist.putIfAbsent(zone.getZoneId(), new LinkedBlockingQueue<>()); // Initialize waitlist queue
            System.out.println("Zone '" + zone.getZoneName() + "' added to the parking lot.");
        }
    }

    /**
     * Assigns the first available spot compatible with the vehicle across all zones.
     * Overloaded method (I).
     *
     * @param vehicle The vehicle needing a spot.
     * @return The assigned ParkingSpot.
     * @throws SlotUnavailableException    If no suitable spot is available anywhere.
     * @throws InvalidVehicleTypeException If the vehicle type is somehow invalid (less likely with enum).
     */
    public synchronized ParkingSpot assignSpot(Vehicle vehicle) throws SlotUnavailableException, InvalidVehicleTypeException {
        if (vehicle == null) {
            throw new InvalidVehicleTypeException("Vehicle cannot be null.");
        }
        if (occupiedSpotsMap.containsKey(vehicle.getLicensePlate())) {
            System.out.println("Vehicle " + vehicle.getLicensePlate() + " is already parked.");
            return occupiedSpotsMap.get(vehicle.getLicensePlate()); // Return current spot
        }

        // Iterate through zones to find a suitable spot
        for (ParkingZone zone : zones.values()) {
            ParkingSpot spot = zone.findAvailableSpot(vehicle.getType());
            if (spot != null) {
                if (spot.occupy(vehicle)) {
                    logEntry(vehicle, spot); // Log the entry
                    return spot;
                }
            }
        }

        // If no exact type match, try finding any compatible spot
        for (ParkingZone zone : zones.values()) {
            ParkingSpot spot = zone.findAvailableSpot(vehicle.getType()); // Find *any* compatible spot
            if (spot != null) {
                if (spot.occupy(vehicle)) {
                    logEntry(vehicle, spot);
                    return spot;
                }
            }
        }


        throw new SlotUnavailableException("No suitable parking spot available for vehicle type: " + vehicle.getType());
    }

    /**
     * Assigns an available spot compatible with the vehicle, preferably in the specified zone.
     * Overloaded method (I).
     *
     * @param vehicle         The vehicle needing a spot.
     * @param preferredZoneId The ID of the preferred zone.
     * @return The assigned ParkingSpot.
     * @throws SlotUnavailableException    If no suitable spot is available in the preferred zone or any other zone.
     * @throws InvalidVehicleTypeException If the vehicle type is invalid.
     * @throws InvalidZoneException        If the preferredZoneId does not exist.
     */
    public synchronized ParkingSpot assignSpot(Vehicle vehicle, String preferredZoneId) throws SlotUnavailableException, InvalidVehicleTypeException, InvalidZoneException {
        if (vehicle == null) {
            throw new InvalidVehicleTypeException("Vehicle cannot be null.");
        }
        if (occupiedSpotsMap.containsKey(vehicle.getLicensePlate())) {
            System.out.println("Vehicle " + vehicle.getLicensePlate() + " is already parked.");
            return occupiedSpotsMap.get(vehicle.getLicensePlate());
        }

        ParkingZone preferredZone = zones.get(preferredZoneId);
        if (preferredZone == null) {
            throw new InvalidZoneException("Preferred zone ID '" + preferredZoneId + "' not found.");
        }

        // Try preferred zone first
        ParkingSpot spot = preferredZone.findAvailableSpot(vehicle.getType());
        if (spot != null) {
            if (spot.occupy(vehicle)) {
                logEntry(vehicle, spot);
                return spot;
            }
        }
        // Try any compatible spot in the preferred zone
        spot = preferredZone.findAvailableSpot(vehicle.getType());
        if (spot != null) {
            if (spot.occupy(vehicle)) {
                logEntry(vehicle, spot);
                return spot;
            }
        }

        System.out.println("No suitable spot in preferred zone " + preferredZoneId + ". Searching other zones...");
        // If not found in preferred zone, try other zones (delegates to the other overloaded method)
        try {
            return assignSpot(vehicle);
        } catch (SlotUnavailableException e) {
            throw new SlotUnavailableException("No suitable parking spot available in preferred zone '" + preferredZoneId + "' or any other zone for vehicle type: " + vehicle.getType());
        }
    }

    /**
     * Releases a spot occupied by a specific vehicle (identified by license plate).
     * Calculates fee and generates a ticket.
     *
     * @param licensePlate The license plate of the departing vehicle.
     * @return A ParkingTicket with details and fee.
     * @throws IllegalArgumentException If the vehicle is not found parked.
     */
    public synchronized ParkingTicket releaseSpot(String licensePlate) {
        ParkingSpot spot = occupiedSpotsMap.get(licensePlate);
        if (spot == null || !spot.isOccupied() || spot.getParkedVehicle() == null || !spot.getParkedVehicle().getLicensePlate().equals(licensePlate)) {
            // Check all spots if not in map (consistency check)
            for (ParkingZone zone : zones.values()) {
                for (ParkingSpot currentSpot : zone.getAllSpots()) {
                    if (currentSpot.isOccupied() && currentSpot.getParkedVehicle() != null && currentSpot.getParkedVehicle().getLicensePlate().equals(licensePlate)) {
                        spot = currentSpot;
                        break;
                    }
                }
                if (spot != null) break;
            }
            if (spot == null) {
                throw new IllegalArgumentException("Vehicle with license plate '" + licensePlate + "' not found parked.");
            }
        }


        Vehicle vehicle = spot.getParkedVehicle();
        LocalDateTime entryTime = entryTimeMap.get(licensePlate);
        LocalDateTime exitTime = LocalDateTime.now();

        if (entryTime == null) {
            System.err.println("Warning: Entry time not found for vehicle " + licensePlate + ". Using current time as placeholder.");
            entryTime = exitTime.minusHours(1); // Placeholder duration
        }

        // Find the zone the spot belongs to
        ParkingZone currentZone = null;
        for (ParkingZone zone : zones.values()) {
            if (zone.getAllSpots().contains(spot)) {
                currentZone = zone;
                break;
            }
        }
        String zoneId = (currentZone != null) ? currentZone.getZoneId() : "UNKNOWN";

        // Calculate Fee
        double fee = billingSystem.calculateFee(vehicle, spot, entryTime, exitTime, currentZone);

        // Create Ticket
        String ticketId = "TKT-" + System.currentTimeMillis();
        ParkingTicket ticket = new ParkingTicket(ticketId, licensePlate, spot.getId(), zoneId, entryTime, exitTime, fee);

        // Vacate spot and log exit
        spot.vacate();
        logExit(vehicle, spot, ticket); // Log exit after vacating

        // Award loyalty points (example: 1 point per hour)
        if (vehicle.getOwner() != null) {
            long hoursParked = Duration.between(entryTime, exitTime).toHours();
            if (hoursParked < 1) hoursParked = 1; // Minimum 1 point
            loyaltyProgram.addPoints(vehicle.getOwner(), (int) hoursParked);
            notificationService.sendNotification(vehicle.getOwner(), "You earned " + hoursParked + " loyalty points!");
        }

        // Check waitlist for the zone the spot was in
        if (currentZone != null) {
            processWaitlist(currentZone.getZoneId());
        }

        return ticket;
    }

    /**
     * Makes a reservation for a user and vehicle in a specific zone.
     *
     * @param user      The user making the reservation.
     * @param vehicle   The vehicle to be parked.
     * @param zoneId    The target zone ID.
     * @param startTime The start time of the reservation.
     * @param endTime   The end time of the reservation.
     * @return The created Reservation object.
     * @throws SlotUnavailableException If no suitable spot can be reserved in the zone.
     * @throws InvalidZoneException     If the zone ID is invalid.
     */
    public synchronized Reservation makeReservation(User user, Vehicle vehicle, String zoneId, LocalDateTime startTime, LocalDateTime endTime) throws SlotUnavailableException, InvalidZoneException {
        ParkingZone zone = zones.get(zoneId);
        if (zone == null) {
            throw new InvalidZoneException("Zone ID '" + zoneId + "' not found.");
        }

        // Find an available spot suitable for the vehicle in the target zone
        ParkingSpot spotToReserve = zone.findAvailableSpot(vehicle.getType());

        if (spotToReserve != null) {
            if (spotToReserve.reserve(endTime)) {
                String reservationId = "RES-" + System.currentTimeMillis();
                Reservation reservation = new Reservation(reservationId, user.getUserId(), vehicle.getLicensePlate(), spotToReserve.getId(), zoneId, startTime, endTime);
                activeReservations.put(reservationId, reservation);
                System.out.println("Reservation " + reservationId + " created for spot " + spotToReserve.getId() + " in zone " + zoneId);
                notificationService.sendNotification(user, "Reservation confirmed for spot " + spotToReserve.getId() + " from " + startTime + " to " + endTime);
                return reservation;
            } else {
                throw new SlotUnavailableException("Failed to reserve the found spot " + spotToReserve.getId() + " (might have been taken simultaneously).");
            }
        } else {
            // If no spot available, add to waitlist
            addToWaitlist(user, zoneId);
            throw new SlotUnavailableException("No suitable spot available for reservation in zone '" + zoneId + "' for vehicle type " + vehicle.getType() + ". Added to waitlist.");
        }
    }

    /**
     * Cancels an existing reservation.
     *
     * @param reservationId The ID of the reservation to cancel.
     */
    public synchronized void cancelReservation(String reservationId) {
        Reservation reservation = activeReservations.remove(reservationId);
        if (reservation != null) {
            ParkingZone zone = zones.get(reservation.getZoneId());
            if (zone != null) {
                ParkingSpot spot = zone.getAllSpots().stream()
                        .filter(s -> s.getId().equals(reservation.getSpotId()))
                        .findFirst()
                        .orElse(null);
                if (spot != null && spot.isReserved()) {
                    spot.cancelReservation();
                    System.out.println("Reservation " + reservationId + " cancelled successfully.");
                    // Notify user?
                    // Process waitlist for this zone as a spot became available
                    processWaitlist(zone.getZoneId());
                } else {
                    System.out.println("Reservation " + reservationId + " found, but spot " + reservation.getSpotId() + " was not in reserved state or not found.");
                }
            } else {
                System.out.println("Reservation " + reservationId + " found, but its zone " + reservation.getZoneId() + " no longer exists.");
            }
        } else {
            System.out.println("Reservation ID " + reservationId + " not found.");
        }
    }


    /**
     * Logs a vehicle's entry into a spot.
     *
     * @param vehicle The entering vehicle.
     * @param spot    The spot being occupied.
     */
    public void logEntry(Vehicle vehicle, ParkingSpot spot) {
        LocalDateTime entryTime = LocalDateTime.now();
        String zoneId = findZoneIdForSpot(spot); // Helper method to find zone

        EntryExitLog log = new EntryExitLog(
                "LOG-" + System.currentTimeMillis(),
                vehicle.getLicensePlate(),
                spot.getId(),
                zoneId,
                entryTime,
                null // Exit time is null on entry
        );
        entryExitLogs.add(log);
        occupiedSpotsMap.put(vehicle.getLicensePlate(), spot);
        entryTimeMap.put(vehicle.getLicensePlate(), entryTime);

        // Write to file (XII - File Handling)
        logManager.writeLog("entry_exit.log", log.toLogFormat());
        System.out.println("Entry logged: " + log.toLogFormat());
    }

    /**
     * Logs a vehicle's exit.
     *
     * @param vehicle The exiting vehicle.
     * @param spot    The spot being vacated.
     * @param ticket  The generated parking ticket.
     */
    public void logExit(Vehicle vehicle, ParkingSpot spot, ParkingTicket ticket) {
        LocalDateTime exitTime = ticket.getExitTime();
        String licensePlate = vehicle.getLicensePlate();

        // Find the corresponding entry log to update exit time (optional but good practice)
        EntryExitLog entryLog = null;
        for (int i = entryExitLogs.size() - 1; i >= 0; i--) {
            EntryExitLog l = entryExitLogs.get(i);
            if (l.getVehicleLicensePlate().equals(licensePlate) && l.getExitTime() == null) {
                entryLog = l;
                break;
            }
        }

        if (entryLog != null) {
            entryLog.setExitTime(exitTime);
            // Update log file? More complex, easier to just log exit separately
            logManager.writeLog("entry_exit.log", "[UPDATE] " + entryLog.toLogFormat());
            System.out.println("Exit logged (updated entry): " + entryLog.toLogFormat());
        } else {
            // If no matching entry log found (e.g., system restart), log exit as a separate event
            EntryExitLog exitRecord = new EntryExitLog(
                    "LOG-" + System.currentTimeMillis(),
                    licensePlate,
                    spot.getId(),
                    ticket.getZoneId(), // Get zone from ticket
                    ticket.getEntryTime(), // Use entry time from ticket
                    exitTime
            );
            entryExitLogs.add(exitRecord); // Add as a separate record if needed
            logManager.writeLog("entry_exit.log", "[EXIT_EVENT] " + exitRecord.toLogFormat());
            System.out.println("Exit logged (separate event): " + exitRecord.toLogFormat());
        }


        // Clean up maps
        occupiedSpotsMap.remove(licensePlate);
        entryTimeMap.remove(licensePlate);
    }

    /**
     * Helper method to find the Zone ID for a given Parking Spot.
     *
     * @param spot The spot to locate.
     * @return The Zone ID, or "UNKNOWN" if not found.
     */
    private String findZoneIdForSpot(ParkingSpot spot) {
        for (Map.Entry<String, ParkingZone> entry : zones.entrySet()) {
            if (entry.getValue().getAllSpots().contains(spot)) {
                return entry.getKey();
            }
        }
        return "UNKNOWN";
    }


    /**
     * Adds a user to the waitlist for a specific zone.
     *
     * @param user   The user to add.
     * @param zoneId The ID of the desired zone.
     */
    public void addToWaitlist(User user, String zoneId) {
        Queue<User> zoneWaitlist = waitlist.get(zoneId);
        if (zoneWaitlist != null) {
            if (!zoneWaitlist.contains(user)) { // Avoid duplicate entries
                zoneWaitlist.offer(user);
                System.out.println("User " + user.getName() + " added to waitlist for zone " + zoneId);
                notificationService.sendNotification(user, "You've been added to the waitlist for zone " + zoneId);
            } else {
                System.out.println("User " + user.getName() + " is already on the waitlist for zone " + zoneId);
            }
        } else {
            System.err.println("Cannot add to waitlist: Zone " + zoneId + " not found.");
        }
    }

    /**
     * Processes the waitlist for a given zone when a spot becomes available.
     * Notifies the first user in the queue.
     *
     * @param zoneId The ID of the zone where a spot opened up.
     */
    public void processWaitlist(String zoneId) {
        Queue<User> zoneWaitlist = waitlist.get(zoneId);
        ParkingZone zone = zones.get(zoneId);

        if (zoneWaitlist != null && !zoneWaitlist.isEmpty() && zone != null) {
            // Check if there's actually an available spot suitable for *someone* on the waitlist
            // (This is simplified - ideally check spot type against user's potential vehicle)
            boolean spotAvailable = zone.getAllSpots().stream().anyMatch(ParkingSpot::isAvailable);

            if (spotAvailable) {
                User nextUser = zoneWaitlist.poll(); // Remove user from waitlist
                if (nextUser != null) {
                    System.out.println("Processing waitlist for zone " + zoneId + ". Notifying user " + nextUser.getName());
                    notificationService.sendNotification(nextUser, "A spot may be available in zone " + zoneId + "! Please check availability.");
                    // In a real system, might auto-reserve or give a time window
                }
            } else {
                System.out.println("Spot became free in zone " + zoneId + ", but no compatible spot found for waitlist or waitlist empty.");
            }
        }
    }

    /**
     * Records a parking violation.
     *
     * @param violation The Violation object to record.
     */
    public void recordViolation(Violation violation) {
        if (violation != null) {
            violations.add(violation);
            logManager.writeLog("violations.log", violation.toLogFormat());
            System.out.println("Violation recorded: " + violation.toLogFormat());
            // Optionally notify user, integrate with billing for penalty
        }
    }

    /**
     * Simple method to check for potential violations (e.g., wrong vehicle type in spot).
     * This is a basic simulation. Real system would use sensors/LPR.
     */
    public synchronized void checkViolations() {
        System.out.println("\n--- Checking for Violations ---");
        for (ParkingZone zone : zones.values()) {
            for (ParkingSpot spot : zone.getAllSpots()) {
                if (spot.isOccupied()) {
                    Vehicle vehicle = spot.getParkedVehicle();
                    if (!spot.isCompatible(vehicle.getType())) {
                        // Found a violation: Vehicle type not compatible with spot type
                        Violation violation = new Violation(
                                "VIO-" + System.currentTimeMillis(),
                                vehicle.getLicensePlate(),
                                spot.getId(),
                                zone.getZoneId(),
                                ViolationType.INVALID_SPOT_TYPE,
                                LocalDateTime.now(),
                                50.0 // Example penalty
                        );
                        if (!violations.contains(violation)) { // Avoid duplicate logs per check
                            recordViolation(violation);
                            // Optionally notify user/admin
                            if (vehicle.getOwner() != null) {
                                notificationService.sendNotification(vehicle.getOwner(),
                                        "Parking Violation Detected: Vehicle " + vehicle.getLicensePlate() +
                                                " parked in incompatible spot type " + spot.getType() + " (" + spot.getId() + "). Penalty: $" + violation.getPenaltyAmount());
                            }
                        }
                    }
                    // Add checks for overstay based on entryTimeMap and allowed duration if needed
                }
            }
        }
        System.out.println("--- Violation Check Complete ---");
    }


    /**
     * Generates a simple report using the static nested class.
     * Demonstrates usage of Static Nested Class (IV).
     */
    public void generateReport() {
        AnalyticsData.generateParkingReport(this);
    }

    // --- Getters for subsystems ---
    public BillingSystem getBillingSystem() {
        return billingSystem;
    }

    public LoyaltyProgram getLoyaltyProgram() {
        return loyaltyProgram;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public Map<String, ParkingZone> getZones() {
        return Collections.unmodifiableMap(zones);
    }

    public List<EntryExitLog> getEntryExitLogs() {
        return Collections.unmodifiableList(entryExitLogs);
    }

    public List<Violation> getViolations() {
        return Collections.unmodifiableList(violations);
    }


    /**
     * Static Nested Class (IV) for handling analytics or reporting tasks.
     * It can access static members of ParkingLot if needed, but primarily operates on the instance passed to it.
     */
    public static class AnalyticsData {
        public static void generateParkingReport(ParkingLot lot) {
            System.out.println("\n--- Parking Lot Report for: " + lot.name + " ---");
            System.out.println("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            System.out.println("\nZone Summary:");
            for (ParkingZone zone : lot.zones.values()) {
                long total = zone.getAllSpots().size();
                long occupied = zone.getAllSpots().stream().filter(ParkingSpot::isOccupied).count();
                long reserved = zone.getAllSpots().stream().filter(ParkingSpot::isReserved).count();
                long available = total - occupied - reserved;
                System.out.printf("  Zone %s (%s): Total=%d, Occupied=%d, Reserved=%d, Available=%d\n",
                        zone.getZoneId(), zone.getZoneName(), total, occupied, reserved, available);
            }

            System.out.println("\nCurrent Occupancy:");
            if (lot.occupiedSpotsMap.isEmpty()) {
                System.out.println("  No vehicles currently parked.");
            } else {
                lot.occupiedSpotsMap.forEach((plate, spot) -> {
                    LocalDateTime entry = lot.entryTimeMap.get(plate);
                    String entryTimeStr = (entry != null) ? entry.format(DateTimeFormatter.ISO_LOCAL_TIME) : "N/A";
                    System.out.printf("  Vehicle: %s, Spot: %s (%s), Entry Time: %s\n",
                            plate, spot.getId(), lot.findZoneIdForSpot(spot), entryTimeStr);
                });
            }

            System.out.println("\nRecent Violations: " + lot.violations.size());
            // Print last 5 violations for brevity
            int start = Math.max(0, lot.violations.size() - 5);
            for (int i = start; i < lot.violations.size(); i++) {
                System.out.println("  " + lot.violations.get(i).toLogFormat());
            }

            System.out.println("\n--- End of Report ---");
            // Could write this report to a file as well using lot.logManager
        }
    }
}

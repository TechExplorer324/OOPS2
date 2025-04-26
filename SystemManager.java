
import java.time.LocalDateTime;
import java.util.*;

/**
 * Main class to run the simulation and demonstrate system features.
 * Includes Scanner usage (XII) and Exception Handling (XI).
 */
public class SystemManager {

    public static void main(String[] args) {
        // --- Initialization ---
        System.out.println("Starting Smart Parking Management System...");
        ParkingLot mainLot = new ParkingLot("Downtown Central Parking");
        Scanner scanner = new Scanner(System.in); // (XII) Scanner Class

        // Setup Users
        // Creating users with different roles (Regular User, Admin)
        User user1 = new User("U001", "Alice", UserRole.REGULAR_USER);
        User user2 = new User("U002", "Bob", UserRole.REGULAR_USER);
        User user3 = new User("U003", "Charlie (Trucker)", UserRole.REGULAR_USER);
        User user4 = new User("U004", "Diana (EV)", UserRole.REGULAR_USER);
        User admin = new User("A001", "Admin User", UserRole.ADMIN);

        // Setup Vehicles
        // Creating various types of vehicles for users
        Vehicle car1 = new Car("CAR-123", user1);
        Vehicle bike1 = new Bike("BIKE-789", user2);
        Vehicle truck1 = new Truck("TRK-456", user3);
        Vehicle ev1 = new ElectricVehicle("EV-001", user4);
        Vehicle car2 = new Car("CAR-444", user2); // Bob's second car

        // Setup Zones and Spots
        // Creating parking zones (General Zone, EV Zone)
        ParkingZone generalZone = new ParkingZone("G", "General");
        generalZone.addSpots(5, SpotType.REGULAR); // Add regular spots
        generalZone.addSpots(3, SpotType.COMPACT); // Add compact spots
        generalZone.addSpots(new ParkingSpot("G-L1", SpotType.LARGE)); // Add large spot
        generalZone.addSpots(new ParkingSpot("G-M1", SpotType.MOTORBIKE), new ParkingSpot("G-M2", SpotType.MOTORBIKE)); // Add motorbike spots

        ParkingZone evZone = new ParkingZone("EV", "EV Charging Zone");
        evZone.addSpot(new ParkingSpot("EV-C1", SpotType.ELECTRIC_CHARGING, true)); // Add EV charging spots
        evZone.addSpot(new ParkingSpot("EV-C2", SpotType.ELECTRIC_CHARGING, true));
        evZone.addSpot(new ParkingSpot("EV-R1", SpotType.REGULAR)); // Regular spot in EV zone

        mainLot.addZone(generalZone); // Add general zone to parking lot
        mainLot.addZone(evZone); // Add EV zone to parking lot

        // Setup Billing System
        mainLot.getBillingSystem().setPaymentProcessor(new CreditCardProcessor()); // Set default payment processor
        mainLot.getBillingSystem().addZonePricingRule("EV", new BillingSystem.DynamicPricingRule(3.0, 1.8, 25.0)); // Add pricing rule for EV zone

        // --- Simulation Loop ---
        boolean running = true;
        while (running) {
            printMenu(); // Display menu options
            System.out.print("Enter choice: ");
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine()); // Read user input
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            try { // Main try-catch for handling parking exceptions (XI)
                switch (choice) {
                    case 1: // Park Vehicle
                        parkVehicle(scanner, mainLot, car1, bike1, truck1, ev1, car2);
                        break;
                    case 2: // Exit Vehicle
                        exitVehicle(scanner, mainLot);
                        break;
                    case 3: // Check Availability
                        checkAvailability(mainLot);
                        break;
                    case 4: // Make Reservation
                        makeReservation(scanner, mainLot, user1, car1); // Example user/vehicle
                        break;
                    case 5: // Cancel Reservation
                        cancelReservation(scanner, mainLot);
                        break;
                    case 6: // Check Violations
                        mainLot.checkViolations();
                        break;
                    case 7: // Generate Report
                        mainLot.generateReport(); // Generates a parking report
                        break;
                    case 8: // View User Points
                        viewUserPoints(user1, user2, user3, user4, admin);
                        break;
                    case 0: // Exit
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (SlotUnavailableException | InvalidZoneException | PaymentException | InvalidVehicleTypeException | IllegalArgumentException e) {
                // Catching specific custom exceptions and common runtime exceptions (XI)
                System.err.println("Operation failed: " + e.getMessage());
            } catch (Exception e) {
                // Catch any other unexpected exceptions (XI)
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace(); // Print stack trace for debugging unexpected errors
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine(); // Pause
            }
        }

        System.out.println("Exiting Smart Parking System. Goodbye!");
        scanner.close(); // Close scanner after use
    }

    private static void printMenu() {
        // Displaying menu options for the user
        System.out.println("\n--- Smart Parking Menu ---");
        System.out.println("1. Park Vehicle");
        System.out.println("2. Exit Vehicle");
        System.out.println("3. Check Availability");
        System.out.println("4. Make Reservation");
        System.out.println("5. Cancel Reservation");
        System.out.println("6. Check for Violations");
        System.out.println("7. Generate Parking Report");
        System.out.println("8. View User Loyalty Points");
        System.out.println("0. Exit System");
        System.out.println("--------------------------");
    }

    private static void parkVehicle(Scanner scanner, ParkingLot lot, Vehicle... vehicles) throws SlotUnavailableException, InvalidVehicleTypeException, InvalidZoneException {
        // Parking a vehicle
        System.out.println("Select vehicle to park:");
        for(int i=0; i<vehicles.length; i++) {
            System.out.printf("%d. %s (%s)\n", i+1, vehicles[i].getLicensePlate(), vehicles[i].getType());
        }
        System.out.print("Enter vehicle number: ");
        int vChoice = Integer.parseInt(scanner.nextLine()) - 1;
        if(vChoice < 0 || vChoice >= vehicles.length) {
            System.out.println("Invalid vehicle choice.");
            return;
        }
        Vehicle selectedVehicle = vehicles[vChoice];

        System.out.print("Enter preferred zone ID (e.g., G, EV) or leave blank for any: ");
        String zonePref = scanner.nextLine().trim().toUpperCase();

        // Assigning spot based on preference
        ParkingSpot assignedSpot;
        if (zonePref.isEmpty()) {
            assignedSpot = lot.assignSpot(selectedVehicle); // Use overloaded method (I)
        } else {
            assignedSpot = lot.assignSpot(selectedVehicle, zonePref); // Use overloaded method (I)
        }
        System.out.println("Vehicle " + selectedVehicle.getLicensePlate() + " assigned to spot " + assignedSpot.getId() + " in zone " + lot.findZoneIdForSpot(assignedSpot));
    }

    private static void exitVehicle(Scanner scanner, ParkingLot lot) throws PaymentException {
        // Exiting a vehicle
        System.out.print("Enter license plate of vehicle to exit: ");
        String licensePlate = scanner.nextLine().trim().toUpperCase();

        ParkingTicket ticket = lot.releaseSpot(licensePlate);
        System.out.println("\n--- Vehicle Exited ---");
        System.out.println(ticket); // Print ticket details

        // Simulate Payment
        if (ticket.getFee() > 0) {
            System.out.printf("Payment due: $%.2f\n", ticket.getFee());
            System.out.print("Process payment? (y/n): ");
            String payChoice = scanner.nextLine().trim().toLowerCase();
            if (payChoice.equals("y")) {
                // In real system, prompt for payment method, details etc.
                // Here, just use the configured processor.
                System.out.print("Choose Payment Method (1: CC, 2: UPI): ");
                int paymentMethodChoice = Integer.parseInt(scanner.nextLine());
                if(paymentMethodChoice == 1) {
                    lot.getBillingSystem().setPaymentProcessor(new CreditCardProcessor());
                } else if (paymentMethodChoice == 2) {
                    lot.getBillingSystem().setPaymentProcessor(new UPIProcessor());
                } else {
                    System.out.println("Invalid payment method choice, using default.");
                }

                if (lot.getBillingSystem().processPayment(ticket.getFee())) {
                    System.out.println("Payment successful for ticket " + ticket.getTicketId());
                } else {
                    // Payment failed exception would have been thrown and caught by main loop
                    System.out.println("Payment processing initiated but failed (see logs).");
                }
            } else {
                System.out.println("Payment skipped.");
            }
        } else {
            System.out.println("No payment due.");
        }
    }

    private static void checkAvailability(ParkingLot lot) {
        // Checking availability in parking lot
        System.out.println("\n--- Parking Availability ---");
        for (ParkingZone zone : lot.getZones().values()) {
            System.out.println("\nZone: " + zone.getZoneName() + " (" + zone.getZoneId() + ")");
            Map<SpotType, Long> availableCounts = new EnumMap<>(SpotType.class);
            Map<SpotType, Long> totalCounts = new EnumMap<>(SpotType.class);

            for(ParkingSpot spot : zone.getAllSpots()) {
                totalCounts.put(spot.getType(), totalCounts.getOrDefault(spot.getType(), 0L) + 1);
                if(spot.isAvailable()) {
                    availableCounts.put(spot.getType(), availableCounts.getOrDefault(spot.getType(), 0L) + 1);
                }
            }

            if(totalCounts.isEmpty()) {
                System.out.println("  No spots defined in this zone.");
                continue;
            }

            for(Map.Entry<SpotType, Long> entry : totalCounts.entrySet()) {
                SpotType type = entry.getKey();
                long total = entry.getValue();
                long available = availableCounts.getOrDefault(type, 0L);
                System.out.printf("  %-18s: %d / %d available\n", type, available, total);
            }
        }
        System.out.println("--------------------------");
    }

    private static void makeReservation(Scanner scanner, ParkingLot lot, User user, Vehicle vehicle) throws SlotUnavailableException, InvalidZoneException {
        // Making a reservation
        System.out.println("\n--- Make Reservation ---");
        System.out.println("User: " + user.getName() + ", Vehicle: " + vehicle.getLicensePlate());
        System.out.print("Enter desired zone ID (e.g., G, EV): ");
        String zoneId = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter reservation duration in hours (e.g., 2): ");
        int hours = Integer.parseInt(scanner.nextLine());

        LocalDateTime startTime = LocalDateTime.now().plusMinutes(5); // Start in 5 mins
        LocalDateTime endTime = startTime.plusHours(hours);

        Reservation res = lot.makeReservation(user, vehicle, zoneId, startTime, endTime);
        System.out.println("Reservation successful:");
        System.out.println(res);
    }

    private static void cancelReservation(Scanner scanner, ParkingLot lot) {
        // Cancelling a reservation
        System.out.println("\n--- Cancel Reservation ---");
        System.out.print("Enter Reservation ID to cancel: ");
        String resId = scanner.nextLine().trim();
        lot.cancelReservation(resId);
    }

    private static void viewUserPoints(User... users) {
        // Viewing loyalty points of users
        System.out.println("\n--- User Loyalty Points ---");
        for(User user : users) {
            System.out.printf("  User: %-20s Points: %d\n", user.getName() + " (" + user.getUserId() + ")", user.getLoyaltyPoints());
        }
        System.out.println("---------------------------");
    }
}

import java.util.Objects;

/**
 * Represents a user of the parking system.
 * Minimum 6 classes requirement met.
 */
class User {
    private final String userId;
    private final String name;
    private UserRole role;
    private Integer loyaltyPoints; // Wrapper class (IX) example

    /**
     * Constructor for User.
     *
     * @param userId Unique ID for the user.
     * @param name   Name of the user.
     * @param role   Role of the user.
     */
    public User(String userId, String name, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.loyaltyPoints = 0; // Initialize points using wrapper
    }

    // --- Getters ---
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    } // Returns Integer wrapper

    // --- Setters/Modifiers ---
    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Adds loyalty points to the user's account.
     *
     * @param points Points to add.
     */
    public void addLoyaltyPoints(int points) {
        if (points > 0) {
            this.loyaltyPoints += points; // Autoboxing/unboxing handles Integer/int
        }
    }

    /**
     * Redeems loyalty points if sufficient balance exists.
     *
     * @param pointsToRedeem Points to redeem.
     * @return true if redemption was successful, false otherwise.
     */
    public boolean redeemLoyaltyPoints(int pointsToRedeem) {
        if (pointsToRedeem > 0 && this.loyaltyPoints >= pointsToRedeem) {
            this.loyaltyPoints -= pointsToRedeem;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "User [ID=" + userId + ", Name=" + name + ", Role=" + role + ", Points=" + loyaltyPoints + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}

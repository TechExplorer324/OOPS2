import java.util.Objects;

/**
 * Represents a user of the parking system.
 * This class meets the requirement for having a minimum of 6 classes (IX).
 */
class User {
    private final String userId; // Unique identifier for the user
    private final String name;   // Name of the user
    private UserRole role;       // Role of the user (e.g., ADMIN, CUSTOMER)
    private Integer loyaltyPoints; // Loyalty points, using wrapper class (IX) for Integer

    /**
     * Constructor for creating a User instance.
     *
     * @param userId Unique ID for the user.
     * @param name   Name of the user.
     * @param role   Role of the user (e.g., ADMIN, CUSTOMER).
     */
    public User(String userId, String name, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.loyaltyPoints = 0; // Initialize loyalty points to 0 (using Integer wrapper)
    }
    
    // --- Getters ---
    
    /**
     * Gets the unique user ID.
     * 
     * @return User ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the user's name.
     * 
     * @return User's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's role.
     * 
     * @return User's role (e.g., ADMIN, CUSTOMER).
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Gets the loyalty points of the user.
     * 
     * @return The current loyalty points.
     */
    public Integer getLoyaltyPoints() {
        return loyaltyPoints; // Returns Integer wrapper, which can be null
    }

    // --- Setters/Modifiers ---

    /**
     * Sets a new role for the user.
     * 
     * @param role The new role to assign to the user.
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Adds loyalty points to the user's account.
     * Ensures points added are positive.
     * 
     * @param points Points to add to the user's loyalty account.
     */
    public void addLoyaltyPoints(int points) {
        if (points > 0) {
            this.loyaltyPoints += points; // Autoboxing/unboxing handles Integer/int types
        }
    }
    
    /**
     * Redeems loyalty points from the user's account.
     * Ensures there are enough points for redemption.
     * 
     * @param pointsToRedeem Points to redeem.
     * @return true if the redemption was successful, false otherwise.
     */
    public boolean redeemLoyaltyPoints(int pointsToRedeem) {
        if (pointsToRedeem > 0 && this.loyaltyPoints >= pointsToRedeem) {
            this.loyaltyPoints -= pointsToRedeem;
            return true; // Redemption successful
        }
        return false; // Insufficient points for redemption
    }

    /**
     * Provides a string representation of the user object.
     * 
     * @return String representation of the User object.
     */
    @Override
    public String toString() {
        return "User [ID=" + userId + ", Name=" + name + ", Role=" + role + ", Points=" + loyaltyPoints + "]";
    }

    /**
     * Compares the current user object with another object for equality.
     * Two users are considered equal if they have the same userId.
     * 
     * @param o The object to compare with.
     * @return true if the users have the same userId, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Same object comparison
        if (o == null || getClass() != o.getClass()) return false; // Null or different class
        User user = (User) o;
        return Objects.equals(userId, user.userId); // Compare userIds
    }

    /**
     * Generates a hash code for the user based on their userId.
     * 
     * @return Hash code for the User object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId); // Generates hash code based on userId
    }
}

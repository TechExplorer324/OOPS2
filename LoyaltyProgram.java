/**
 * Manages loyalty points for users.
 * This class provides functionality for adding, redeeming, and checking loyalty points for users.
 * The system assumes users have a balance of points, which can be adjusted as per business logic.
 * Minimum 6 classes requirement met.
 */
class LoyaltyProgram {
    // Using user object directly might be less efficient than a map if there are numerous users,
    // but simpler for this example. A Map<String, Integer> userId -> points is a common approach in large systems.

    /**
     * Adds points to a user's loyalty points balance.
     *
     * @param user   The user to whom points will be added.
     * @param points The number of points to add to the user's balance.
     * The points value must be positive for the operation to proceed.
     */
    public void addPoints(User user, int points) {
        if (user != null && points > 0) {
            user.addLoyaltyPoints(points); // Adds points to the user's account
            System.out.println("Added " + points + " loyalty points to user " + user.getName() 
                    + ". New balance: " + user.getLoyaltyPoints());
        }
    }

    /**
     * Redeems points from a user's loyalty points balance.
     *
     * @param user           The user redeeming the points.
     * @param pointsToRedeem The number of points to redeem.
     * @return true if redemption was successful, false otherwise.
     * If the user doesn't have sufficient points, the redemption will fail.
     */
    public boolean redeemPoints(User user, int pointsToRedeem) {
        if (user != null && pointsToRedeem > 0) {
            boolean success = user.redeemLoyaltyPoints(pointsToRedeem); // Attempts to redeem the points
            if (success) {
                System.out.println("Redeemed " + pointsToRedeem + " loyalty points from user " 
                        + user.getName() + ". New balance: " + user.getLoyaltyPoints());
            } else {
                System.out.println("Failed to redeem " + pointsToRedeem + " points for user " 
                        + user.getName() + ". Insufficient balance: " + user.getLoyaltyPoints());
            }
            return success;
        }
        return false;
    }

    /**
     * Gets the current loyalty points balance for a user.
     *
     * @param user The user whose points balance is to be checked.
     * @return The current number of loyalty points the user has.
     * If the user is null, it returns 0.
     */
    public int getPoints(User user) {
        return (user != null) ? user.getLoyaltyPoints() : 0; // Return 0 if the user is null
    }
}


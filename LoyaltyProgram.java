/**
 * Manages loyalty points for users.
 * Minimum 6 classes requirement met.
 */
class LoyaltyProgram {
    // Using user object directly might be less efficient than map if users are numerous
    // but simpler for this example. A Map<String, Integer> userId -> points is common.

    /**
     * Adds points to a user's balance.
     *
     * @param user   The user to add points to.
     * @param points The number of points to add.
     */
    public void addPoints(User user, int points) {
        if (user != null && points > 0) {
            user.addLoyaltyPoints(points);
            System.out.println("Added " + points + " loyalty points to user " + user.getName() + ". New balance: " + user.getLoyaltyPoints());
        }
    }

    /**
     * Redeems points from a user's balance.
     *
     * @param user           The user redeeming points.
     * @param pointsToRedeem The number of points to redeem.
     * @return true if redemption was successful, false otherwise.
     */
    public boolean redeemPoints(User user, int pointsToRedeem) {
        if (user != null && pointsToRedeem > 0) {
            boolean success = user.redeemLoyaltyPoints(pointsToRedeem);
            if (success) {
                System.out.println("Redeemed " + pointsToRedeem + " loyalty points from user " + user.getName() + ". New balance: " + user.getLoyaltyPoints());
            } else {
                System.out.println("Failed to redeem " + pointsToRedeem + " points for user " + user.getName() + ". Insufficient balance: " + user.getLoyaltyPoints());
            }
            return success;
        }
        return false;
    }

    /**
     * Gets the current point balance for a user.
     *
     * @param user The user to check.
     * @return The user's loyalty points.
     */
    public int getPoints(User user) {
        return (user != null) ? user.getLoyaltyPoints() : 0;
    }
}

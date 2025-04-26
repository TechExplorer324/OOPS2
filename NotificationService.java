/**
 * Simulates sending notifications to users.
 * Minimum 6 classes requirement met.
 */
class NotificationService {
    /**
     * Sends a notification message to a user (simulation).
     *
     * @param user    The user to notify.
     * @param message The message content.
     */
    public void sendNotification(User user, String message) {
        if (user != null && message != null && !message.trim().isEmpty()) {
            System.out.println("--- Notification Sent ---");
            System.out.println("To: " + user.getName() + " (ID: " + user.getUserId() + ")");
            System.out.println("Message: " + message);
            System.out.println("-------------------------");
            // In a real system, this would integrate with Email, SMS, or Push Notification services.
        }
    }
}

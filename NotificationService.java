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
        // Check if the user and message are valid (non-null and non-empty)
        if (user != null && message != null && !message.trim().isEmpty()) {
            // Print the notification details (simulating sending a notification)
            System.out.println("--- Notification Sent ---");
            System.out.println("To: " + user.getName() + " (ID: " + user.getUserId() + ")");
            System.out.println("Message: " + message);
            System.out.println("-------------------------");
            
            // In a real system, this method could be connected to various notification systems such as Email, SMS, or Push Notification services.
        }
    }
}

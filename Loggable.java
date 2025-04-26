/**
 * Interface (VI, VIII) for objects that can log messages.
 * This interface is used to demonstrate the concept of multiple interface implementation.
 * Classes implementing this interface must provide a concrete implementation for logging messages.
 */
interface Loggable {
    /**
     * Logs a message.
     * 
     * @param message The message to log.
     * This method is intended to be implemented by classes that need to log specific messages
     * to a logging system, file, or any other medium.
     */
    void log(String message);
}


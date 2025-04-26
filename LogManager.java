import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Handles writing log messages to files.
 * Demonstrates File I/O (XII).
 */
class LogManager {
    private final String logDirectory;

    /**
     * Constructor initializes the log directory.
     */
    public LogManager() {
        this("logs"); // Default log directory
    }

    /**
     * Constructor allowing custom log directory.
     *
     * @param logDirectory Path to the directory for log files.
     */
    public LogManager(String logDirectory) {
        this.logDirectory = logDirectory;
        // Create directory if it doesn't exist (XII - File Handling)
        File dir = new File(this.logDirectory);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                System.out.println("Log directory created: " + dir.getAbsolutePath());
            } else {
                System.err.println("Failed to create log directory: " + dir.getAbsolutePath());
            }
        }
    }

    /**
     * Writes a message to a specified log file within the log directory.
     * Appends to the file if it exists.
     *
     * @param filename The name of the log file (e.g., "system.log").
     * @param message  The message to write.
     */
    public synchronized void writeLog(String filename, String message) {
        // Use try-with-resources for automatic closing of FileWriter/PrintWriter (XII - File Handling)
        try (FileWriter fw = new FileWriter(logDirectory + File.separator + filename, true); // true for append mode
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " - " + message);

        } catch (IOException e) {
            // Basic Exception Handling (XI)
            System.err.println("Error writing to log file '" + filename + "': " + e.getMessage());
            // e.printStackTrace(); // Optionally print stack trace for debugging
        }
    }
}

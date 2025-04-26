/**
 * Custom Exception (XI) for when an invalid or non-existent zone is specified.
 */
class InvalidZoneException extends Exception {
    public InvalidZoneException(String message) {
        super(message);
    }
}

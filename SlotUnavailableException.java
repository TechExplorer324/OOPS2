/**
 * Custom Exception (XI) for when no suitable parking slot is available.
 */
class SlotUnavailableException extends Exception {
    public SlotUnavailableException(String message) {
        super(message);
    }
}

package namara.client.exception;

public class NamaraException extends Exception {
    /**
     * Base exception type for all exceptions generating during
     * connecting and querying
     *
     * @param message
     */
    public NamaraException(String message) { super(message); }
}

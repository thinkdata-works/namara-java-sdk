package namara.client.exception;

public class ConnectionException extends Exception {
    /**
     * The URL that was attempted
     */
    public String connectionUrl;

    /**
     * Exception thrown when unable to connect to
     *
     * @param message
     * @param connectionUrl
     */
    public ConnectionException(String message, String connectionUrl) {
        super(message);
        this.connectionUrl = connectionUrl;
    }
}

package namara.client.exception;

public class ConnectionException extends NamaraException {
    /**
     * The URL that was attempted
     */
    public String connectionUrl;

    /**
     * The body for the request made
     */
    public String requestBody;

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

    public ConnectionException(String message, String connectionUrl, String requestBody) {
        this(message, connectionUrl);
        this.requestBody = requestBody;
    }
}

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
     * Exception thrown when unable to connect to Namara at host
     *
     * @param message The error message
     * @param connectionUrl The url that was attempted
     */
    public ConnectionException(String message, String connectionUrl) {
        super(message);
        this.connectionUrl = connectionUrl;
    }

    /**
     * Exception thrown when untable to connect to Namara at host
     *
     * @param message The error message
     * @param connectionUrl The url that was attempted
     * @param requestBody The request body sent to the url
     */
    public ConnectionException(String message, String connectionUrl, String requestBody) {
        this(message, connectionUrl);
        this.requestBody = requestBody;
    }
}

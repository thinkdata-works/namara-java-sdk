package namara.client.exception;
public class AuthorizationException extends Exception {
    /**
     *  Exception thrown when API Key for user can not be authorized at Namara host
     *
     * @param message
     */
    public AuthorizationException(String message) {
        super(message);
    }
}

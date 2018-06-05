package namara.client.exception;
public class AuthorizationException extends NamaraException {
    /**
     *  Exception thrown when API Key for user can not be authorized at Namara host
     *
     * @param message
     */
    public AuthorizationException(String message) {
        super(message);
    }
}

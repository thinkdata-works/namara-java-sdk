package namara.client.exception;

public class QueryException extends NamaraException {

    /**
     * Exception thrown when there is an error constructing or executing the query
     *
     * @param message - error message
     */
    public QueryException(String message) { super(message); }
}

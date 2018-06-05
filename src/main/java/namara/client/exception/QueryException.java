package namara.client.exception;

public class QueryException extends NamaraException {

    /**
     * Exception thrown when there is an error constructing or executing the query
     *
     * @param message
     */
    public QueryException(String message) { super(message); }
}

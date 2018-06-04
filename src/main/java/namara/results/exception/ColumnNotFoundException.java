package namara.results.exception;

public class ColumnNotFoundException extends Exception {
    /**
     * The column name that was attempted
     */
    public String columnName;

    /**
     * Exception thrown when trying to find a column in a ResultSet
     *
     * @param message
     * @param columnName
     */
    public ColumnNotFoundException(String message, String columnName) {
        super(message);
        this.columnName = columnName;
    }
}

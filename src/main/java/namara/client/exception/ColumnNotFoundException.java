package namara.client.exception;

public class ColumnNotFoundException extends Exception {
    /**
     * The column name that was attempted
     */
    private String columnName;

    /**
     * Exception thrown when trying to find a column in a ResultSet
     *
     * @param message the error exception
     * @param columnName the column name that generated the error
     */
    public ColumnNotFoundException(String message, String columnName) {
        super(message);
        this.columnName = columnName;
    }

    /**
     * @return the column name for the error message
     */
    public String getColumnName() {
        return columnName;
    }
}

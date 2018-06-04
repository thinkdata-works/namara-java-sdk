package namara.results;

import namara.results.exception.ColumnNotFoundException;

import java.io.InputStream;

/*
 * Copied from sql.ResultSet. Want to implement all functionality, but Namara Query backed
 */
public interface ResultSet {

    /**
     * Mapes the given column label to it's column index
     *
     * @param columnLabel column label for value
     * @return the column index of the given column
     * @throws ColumnNotFoundException if the result set row does not contain a value with the given columnLabel
     */
    int findColumn(String columnLabel) throws ColumnNotFoundException;

    // TODO - going to have to do something creative with reading from the socket here...
    /**
     * Retrieves the value of the designated column in the current row as a stream of ASCII characters
     *
     * @param columnIndex
     * @return
     * @throws ColumnNotFoundException
     */
    InputStream getAsciiStream(int columnIndex) throws ColumnNotFoundException;
}

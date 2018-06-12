package namara.client;

import namara.client.exception.ColumnNotFoundException;
import org.json.JSONObject;

import java.util.*;

public class Record {
    /**
     * The record from the response that this object will retrieve values from
     */
    private JSONObject responseObject;

    /**
     * Builds a new responseObject out of a JSON
     *
     * @param responseObject
     */
    Record(JSONObject responseObject) {
        this.responseObject = responseObject;
    }

    @Override
    public String toString() {
        return responseObject.toString();
    }

    /**
     * Get an iterator for all Values in this Record.
     *
     * Order of Values will not be guarenteed
     *
     * @return - an iterator of Values
     */
    public Iterator<Value> iterator() {
        List<Value> valueList = new ArrayList();

        for(Map.Entry<String, Object> entry : responseObject.toMap().entrySet()) {
            valueList.add(new Value(entry.getKey(), responseObject));
        }

        return valueList.iterator();
    }

    /**
     * Gets all keys/column names for the record
     *
     * @return The set of column names
     */
    public Set<String> columnsNames() {
        return responseObject.keySet();
    }

    /**
     * @return - the number of columns
     */
    public int length() {
        return responseObject.length();
    }

    /**
     * Gets the Value at a particular column name
     *
     * @param key the column name
     * @return the Value if it exists at that column name
     * @throws ColumnNotFoundException Column does not exist in Record
     */
    public Value getValue(String key) throws ColumnNotFoundException {
        if(!responseObject.has(key)) {
            throw new ColumnNotFoundException("Column not found at " + key, key);
        }

        return new Value(key, responseObject);
    }
}

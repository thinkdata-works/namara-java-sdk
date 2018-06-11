package namara.client;

import namara.client.exception.ColumnNotFoundException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
     * @return - an iterator of values
     */
    public Iterator<Value> iterator() {
        List<Value> valueList = new ArrayList();

        for(Map.Entry<String, Object> entry : responseObject.toMap().entrySet()) {
            valueList.add(new Value(entry.getKey(), responseObject));
        }

        return valueList.iterator();
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
     * @param key - the column key
     * @return - the value if found at key
     * @throws ColumnNotFoundException - if column does not exist or
     */
    public Value getValue(String key) throws ColumnNotFoundException {
        if(!responseObject.has(key)) {
            throw new ColumnNotFoundException("Column not found at " + key, key);
        }

        return new Value(key, responseObject);
    }
}

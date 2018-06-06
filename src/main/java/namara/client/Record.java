package namara.client;

import org.json.JSONObject;

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
}

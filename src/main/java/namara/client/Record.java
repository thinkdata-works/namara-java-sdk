package namara.client;

import org.json.JSONObject;

public class Record {
    private JSONObject responseObject;

    Record(JSONObject responseObject) {
        this.responseObject = responseObject;
    }

    @Override
    public String toString() {
        return responseObject.toString();
    }
}

package namara.client;

public class NamaraResponse {
    private int responseCode;
    private String responseBody;

    public NamaraResponse(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public int getResponseCode() { return responseCode; }

    public String getResponseBody() { return responseBody; }
}

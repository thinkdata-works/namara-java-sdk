package namara.client;

import namara.client.exception.AuthorizationException;
import namara.client.exception.ConnectionException;
import namara.client.exception.NamaraException;
import namara.client.exception.QueryException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientTest {
    /*
     * NOTE: update this with necessary API keys and URLs
     */
    String API_KEY = "67902e8fe69efa4d82348de8bf5df077552cc66221f15c7593fb381113f69147";
    String NAMARA_HOST = "https://namara-api-chris.ngrok.io/";
    String DATA_SET_ID = "e276a9a8-d06f-49a0-bd69-482920006b53";
    String DATA_SET_VERSION = "en-0";

    @Test
    public void testTestConnection() throws NamaraException {
        Client client = new Client(NAMARA_HOST, API_KEY);
        assertEquals(client.testConnection(), true);
    }

    @Test(expected = ConnectionException.class)
    public void testTestConnectionMalformedURL() throws NamaraException {
        Client client = new Client("somemalformedUrl//://", API_KEY);
        client.testConnection();
    }

    @Test(expected = ConnectionException.class)
    public void testTestConnectionHostNotFound() throws NamaraException {
        Client client = new Client("https://example.namara.io", API_KEY);
        client.testConnection();
    }

    @Test(expected = AuthorizationException.class)
    public void testTestConnectionUserNotFound() throws NamaraException {
        Client client = new Client(NAMARA_HOST, "XXXX");
        client.testConnection();
    }

    @Test
    public void testGetNamaraHost() {
        Client client = new Client(NAMARA_HOST, API_KEY);
        assertEquals(client.getNamaraHost(), "namara-api-chris.ngrok.io");
    }

    @Test
    public void testQuerySuccess() throws NamaraException {
        Client client = new Client(NAMARA_HOST, API_KEY);
        String queryString = "SELECT * FROM " + DATA_SET_ID + "/" + DATA_SET_VERSION;
        JSONObject response = client.query(queryString);
        assertEquals(250, response.getJSONArray("results").length());
    }

    @Test(expected = QueryException.class)
    public void testQueryBadQuery() throws NamaraException {
        Client client = new Client(NAMARA_HOST, API_KEY);
        String queryString = "SELECT * FROM A BIG DATA SET";
        client.query(queryString);
    }

    @Test
    public void testQueryNoResults() throws NamaraException {
        Client client = new Client(NAMARA_HOST, API_KEY);
        String queryString = "SELECT * FROM " + DATA_SET_ID + "/" + DATA_SET_VERSION + " LIMIT 0 OFFSET 0";
        JSONObject response = client.query(queryString);
        assertEquals(0, response.getJSONArray("results").length());
    }
}
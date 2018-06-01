package namara.client;

import namara.client.exception.AuthorizationException;
import namara.client.exception.ConnectionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientTest {

    // TODO - update this with needed info
    String API_KEY = "67902e8fe69efa4d82348de8bf5df077552cc66221f15c7593fb381113f69147";
    String NAMARA_HOST = "https://namara-api-chris.ngrok.io/";

    // TODO - some VCR counterpart? Will need to mock http interactions

    @Test
    public void testTestConnection() throws AuthorizationException, ConnectionException{
        Client client = new Client(NAMARA_HOST, API_KEY);
        assertEquals(client.testConnection(), true);
    }

    @Test(expected = ConnectionException.class)
    public void testTestConnectionMalformedURL() throws AuthorizationException, ConnectionException {
        Client client = new Client("somemalformedUrl//://", API_KEY);
        client.testConnection();
    }

    @Test(expected = ConnectionException.class)
    public void testTestConnectionHostNotFound() throws AuthorizationException, ConnectionException {
        Client client = new Client("https://example.namara.io", API_KEY);
        client.testConnection();
    }

    @Test(expected = AuthorizationException.class)
    public void testTestConnectionUserNotFound() throws AuthorizationException, ConnectionException {
        Client client = new Client(NAMARA_HOST, "XXXX");
        client.testConnection();
    }

    @Test
    public void testGetNamaraHost() {
        Client client = new Client(NAMARA_HOST, API_KEY);
        assertEquals(client.getNamaraHost(), "namara-api-chris.ngrok.io");
    }
}
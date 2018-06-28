package namara.client;

import namara.client.exception.AuthorizationException;
import namara.client.exception.ConnectionException;
import namara.client.exception.NamaraException;
import namara.client.exception.QueryException;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;


import java.io.IOException;

import static org.junit.Assert.*;

public class ClientTest {
    String NAMARA_HOST = "https://example.namara.io/";
    String API_KEY = "XXXXX";
    String DATA_SET_ID = "733934b4-5434-43a6-a487-cdf8091b3a493";
    String DATA_SET_VERSION = "en-0";


    @Test
    public void testTestConnection() throws NamaraException, IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"status\":\"true\"}"));

        HttpUrl baseUrl = server.url(Client.Endpoints.TESTING_ENDPOINT);

        Client client = new Client(NAMARA_HOST, API_KEY);
        assertEquals(client.testConnection(baseUrl), true);

        server.shutdown();
    }


    @Test(expected = ConnectionException.class)
    public void testTestConnectionHostNotFound() throws NamaraException, IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(0).setBody(""));

        try {
            HttpUrl baseUrl = server.url(Client.Endpoints.TESTING_ENDPOINT);

            Client client = new Client(NAMARA_HOST, API_KEY);
            client.testConnection(baseUrl);

            server.shutdown();
        } catch(NamaraException e) { // catch and rethrow to shut down mock server
            server.shutdown();
            throw e;
        }
    }

    @Test(expected = AuthorizationException.class)
    public void testTestConnectionUserNotFound() throws NamaraException, IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(422).setBody("{\"status\":\"false\"}"));

        try {
            HttpUrl baseUrl = server.url(Client.Endpoints.TESTING_ENDPOINT);

            Client client = new Client(NAMARA_HOST, API_KEY);
            client.testConnection(baseUrl);

            server.shutdown();
        } catch(NamaraException e) { // catch and rethrow to shut down mock server
            server.shutdown();
            throw e;
        }
    }

    @Test
    public void testQuerySuccess() throws NamaraException, IOException {
        MockWebServer server = new MockWebServer();
        JSONArray resultsCollection = new JSONArray()
                .put(new JSONObject().put("c0", "xx").put("c1", "yy"))
                .put(new JSONObject().put("c0", "zz").put("c1", "tt"))
                .put(new JSONObject().put("c0", "hh").put("c1", "aa"));

        server.enqueue(new MockResponse().setResponseCode(200).setBody(
           new JSONObject().put("results", resultsCollection).toString()
        ));

        try {
            HttpUrl baseUrl = server.url(Client.Endpoints.QUERY_ENDPOINT);

            Client client = new Client(NAMARA_HOST, API_KEY);
            String queryString = "SELECT * FROM " + DATA_SET_ID + "/" + DATA_SET_VERSION;

            JSONObject response = client.query(baseUrl, queryString);
            assertEquals(3, ((JSONArray) response.get("results")).length());

            server.shutdown();
        } catch(NamaraException e) { // catch and rethrow to shut down mock server
            server.shutdown();
            throw e;
        }
    }

    @Test(expected = QueryException.class)
    public void testQueryBadQuery() throws NamaraException, IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(422).setBody("{\"error\": \"Your query is bad and you should feel bad\""));

        try {
            HttpUrl baseUrl = server.url(Client.Endpoints.QUERY_ENDPOINT);

            Client client = new Client(NAMARA_HOST, API_KEY);
            String queryString = "SELECT * FROM A BIG DATA SET";

            JSONObject response = client.query(baseUrl, queryString);
        } catch(NamaraException e) {
            server.shutdown();
            throw e;
        }
    }

    @Test
    public void testQueryNoResults() throws NamaraException, IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"results\":[]}"));
        try {
            HttpUrl baseUrl = server.url(Client.Endpoints.QUERY_ENDPOINT);

            Client client = new Client(NAMARA_HOST, API_KEY);
            String queryString = "SELECT * FROM " + DATA_SET_ID + "/" + DATA_SET_VERSION + " LIMIT 0 OFFSET 0";

            JSONObject response = client.query(baseUrl, queryString);
            assertEquals(0, response.getJSONArray("results").length());
        } catch(NamaraException e) {
            server.shutdown();
            throw e;
        }
    }
}
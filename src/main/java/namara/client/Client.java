package namara.client;

import namara.client.exception.AuthorizationException;
import namara.client.exception.ConnectionException;
import namara.client.exception.QueryException;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Client {

    private class Endpoints {
        /**
         * Endpoint for check if a user exists
         */
        public static final String TESTING_ENDPOINT = "/v0/static/authenticated";

        public static final String QUERY_ENDPOINT = "/v0/query";
    }

    private class Connection {
        private static final String PROTOCOL = "https";

        /**
         * The namara instance that will be connected to.
         */
        private String namaraHost;

        /**
         * The apiKey for the connecting user
         */
        private String apiKey;

        private Connection(String namaraHost, String apiKey) {
            this.namaraHost = processHostString(namaraHost);
            this.apiKey = apiKey;
        }

        private String getNamaraHost() {
            return namaraHost;
        }

        private String processHostString(String hostString) {
            // Remove http(s):// if present. Will be forcing https later
            hostString = hostString.replaceFirst("http(s)?:\\/\\/", "");

            // Chomp any trailing "/", this will be added in
            hostString = StringUtils.stripEnd(hostString, "/");

            return hostString;
        }

        private NamaraResponse getJSON(String connectionString) throws ConnectionException, IOException {
            OkHttpClient httpClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .addHeader("X-Api-Key", apiKey)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .url(connectionString)
                    .build();

            Response response = httpClient.newCall(request).execute();
            return new NamaraResponse(response.code(), response.body().string());
        }

        private NamaraResponse postJSON(String connectionString, String requestBody) throws QueryException, IOException {
            OkHttpClient httpClient = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody);

            Request request = new Request.Builder()
                    .addHeader("X-API-Key", apiKey)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .url(connectionString)
                    .post(body)
                    .build();

            Response response = httpClient.newCall(request).execute();
            return new NamaraResponse(response.code(), response.body().string());
        }

        private String constructUrl(String endpoint) throws ConnectionException {
            String urlString = PROTOCOL + "://" + namaraHost + endpoint;
            try {
                return new URL(urlString).toString();
            } catch(MalformedURLException e) {
                throw new ConnectionException("Unable to parse url \"" + urlString +"\": " + e.getMessage(), urlString);
            }
        }
    }

    /**
     * Connection to namara that can be used to interface
     */
    private Connection connection;

    /**
     * Builds a new client for interfacing with Namara
     * Will ping the host with the apiKey to ensure that the connection is valid
     *
     * @param namaraHost The host name that will be connected to
     * @param apiKey The API Key for the connecting user
     */
    public Client(String namaraHost, String apiKey) {
        this.connection = new Connection(namaraHost, apiKey);
    }

    /**
     * Tests the namaraHost and apiKey to ensure that they can connect with the instance.
     * If connection is successful, returns true. Otherwise, errors will be raised containing
     * details.
     *
     * @throws AuthorizationException when API Key is not found or not valid for a user at that host
     * @throws ConnectionException when namara host can not be reached
     */
    public boolean testConnection() throws AuthorizationException, ConnectionException {
        // Will throw ConnectionException if it can't be parsed
        String connectionString = connection.constructUrl(Endpoints.TESTING_ENDPOINT);

        try {
            NamaraResponse response = connection.getJSON(connectionString);
            int statusCode = response.getResponseCode();

            switch(Integer.valueOf(statusCode / 100)) {
                case 2: // status 2xx
                    return true;
                case 4:
                    throw new AuthorizationException("Unable to authorize user account with API Key at given hostName. " +
                            "[" + statusCode * 100 + "] " + response.getResponseBody());
                default:
                    throw new ConnectionException("Something went wrong connecting to Namara. " +
                            "[" + statusCode * 100 + "] " + response.getResponseBody(), connectionString);
            }

        } catch(IOException e) {
            // Something went wrong with connecting, no response found
            throw new ConnectionException("Encountered error when connecting: " + e.getMessage(), connectionString);
        }
    }

    /**
     * Performs a query request on Namara and returns all results from the query
     *
     * @param queryString
     * @return The resulting collection of records
     * @throws AuthorizationException
     * @throws ConnectionException
     * @throws QueryException
     */
    public JSONObject query(String queryString) throws AuthorizationException, ConnectionException, QueryException {
        String connectionString = connection.constructUrl(Endpoints.QUERY_ENDPOINT);
        String jsonString = new JSONObject().put("query", queryString).toString();

        try {
            NamaraResponse response = connection.postJSON(connectionString, jsonString);
            switch(Integer.valueOf(response.getResponseCode())) {
                case 200:
                    return new JSONObject(response.getResponseBody());
                case 401:
                case 403:
                    throw new AuthorizationException("Unauthorized: " + response.getResponseBody());
                case 422:
                    throw new QueryException("Error executing query. Got response: " + response.getResponseBody());
                default:
                    throw new ConnectionException("Something went wrong conneting to Namara. [" +
                            response.getResponseCode() + "] " + response.getResponseBody(), connectionString, jsonString);
            }
        } catch(IOException e) {
            throw new ConnectionException("Encountered error when connecting: " + e.getMessage(), connectionString);
        }
    }


    /**
     * Gets the processed host string for the client
     *
     * @return The host name
     */
    public String getNamaraHost() {
        return connection.getNamaraHost();
    }
}

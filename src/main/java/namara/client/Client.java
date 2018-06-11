package namara.client;

import namara.client.exception.AuthorizationException;
import namara.client.exception.ConnectionException;
import namara.client.exception.QueryException;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.IOException;

public class Client {

    class Endpoints {
        /**
         * Endpoint for check if a user exists
         */
        public static final String TESTING_ENDPOINT = "v0/users/verify_token";

        /**
         * Endpoint for querying
         */
        public static final String QUERY_ENDPOINT = "v0/query";
    }

    class NamaraResponse {
        /**
         * HTTP response code
         */
        public int responseCode;

        /**
         * Stringified response body
         */
        public String responseBody;

        /**
         * Initialize a new Namara response to be read by client
         *
         * @param responseCode
         * @param responseBody
         */
        public NamaraResponse(int responseCode, String responseBody) {

            this.responseCode = responseCode;
            this.responseBody = responseBody;
        }
    }

    class Connection {

        /**
         * The namara instance that will be connected to.
         */
        private String namaraHost;

        /**
         * The apiKey for the connecting user
         */
        private String apiKey;

        public Connection(String namaraHost, String apiKey) {
            this.namaraHost = processHostString(namaraHost);
            this.apiKey = apiKey;
        }

        public String getNamaraHost() {
            return namaraHost;
        }

        /**
         * Takes host string and enforces https as protocol. Removes any trailing "/"
         *
         * @param hostString
         * @return
         */
        private String processHostString(String hostString) {
            // Remove http(s):// if present. Will be forcing https later
            hostString = hostString.replaceFirst("http(s)?:\\/\\/", "");

            // Chomp any trailing "/", this will be added in
            hostString = StringUtils.stripEnd(hostString, "/");

            return hostString;
        }

        /**
         * Perform a GET.json request to namara,
         *
         * @param url
         * @return The response body + response code
         * @throws IOException
         */
        private NamaraResponse getJSON(HttpUrl url) throws IOException {
            OkHttpClient httpClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .addHeader("X-Api-Key", apiKey)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .url(url)
                    .build();

            Response response = httpClient.newCall(request).execute();
            return new NamaraResponse(response.code(), response.body().string());
        }

        /**
         * Perform a POST.json request to namara
         *
         * @param url
         * @param requestBody
         * @return The response body + response code
         * @throws IOException
         */
        private NamaraResponse postJSON(HttpUrl url, String requestBody) throws IOException {
            OkHttpClient httpClient = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody);

            Request request = new Request.Builder()
                    .addHeader("X-API-Key", apiKey)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .url(url)
                    .post(body)
                    .build();

            Response response = httpClient.newCall(request).execute();
            return new NamaraResponse(response.code(), response.body().string());
        }
    }

    /**
     * Connection to namara that can be used to interface
     */
    private Connection connection;

    /**
     * Making package private so this can be set in unit tests
     */
    private final String PROTOCOL = "https";

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
     * @return true if the client is authorized
     */
    public boolean testConnection() throws AuthorizationException, ConnectionException {
        // Will throw ConnectionException if it can't be parsed
        HttpUrl url = new HttpUrl.Builder()
                .scheme(PROTOCOL)
                .host(connection.getNamaraHost())
                .addPathSegments(Endpoints.TESTING_ENDPOINT)
                .build();

        return testConnection(url);
    }

    /*
     * Isolated for testing/mocking
     */
    boolean testConnection(HttpUrl connectionUrl) throws AuthorizationException, ConnectionException {
        try {
            NamaraResponse response = connection.getJSON(connectionUrl);
            int statusCode = response.responseCode;

            switch(Integer.valueOf(statusCode / 100)) {
                case 2: // status 2xx
                    return true;
                case 4:
                    throw new AuthorizationException("Unable to authorize user account with API Key at given hostName. " +
                            "[" + statusCode * 100 + "] " + response.responseBody);
                default:
                    throw new ConnectionException("Something went wrong connecting to Namara. " +
                            "[" + statusCode * 100 + "] " + response.responseBody, connectionUrl.toString());
            }

        } catch(IOException e) {
            // Something went wrong with connecting, no response found
            throw new ConnectionException("Encountered error when connecting: " + e.getMessage(), connectionUrl.toString());
        }
    }

    /**
     * Performs a query request on Namara and returns all results from the query
     *
     * @param queryString - query to issue to namara
     * @return The resulting collection of records
     * @throws AuthorizationException - when unable to authorize client for namara
     * @throws ConnectionException - when unable to connect to namara
     * @throws QueryException - when unable to build or execute query on namara
     */
    public JSONObject query(String queryString) throws AuthorizationException, ConnectionException, QueryException {
        String jsonString = new JSONObject().put("query", queryString).toString();

        HttpUrl url = new HttpUrl.Builder()
                .scheme(PROTOCOL)
                .host(connection.getNamaraHost())
                .addPathSegments(Endpoints.QUERY_ENDPOINT)
                .build();
        return query(url, jsonString);
    }

    /*
     * Isolated for testing/mocking
     */
    JSONObject query(HttpUrl connectionUrl, String jsonString) throws AuthorizationException, ConnectionException, QueryException {
        try {
            NamaraResponse response = connection.postJSON(connectionUrl, jsonString);
            switch(Integer.valueOf(response.responseCode)) {
                case 200:
                    return new JSONObject(response.responseBody);
                case 401:
                case 403:
                    throw new AuthorizationException("Unauthorized: " + response.responseBody);
                case 422:
                    throw new QueryException("Error executing query. Got response: " + response.responseBody);
                default:
                    throw new ConnectionException("Something went wrong conneting to Namara. [" +
                            response.responseCode + "] " + response.responseBody, connectionUrl.toString(), jsonString);
            }
        } catch(IOException e) {
            throw new ConnectionException("Encountered error when connecting: " + e.getMessage(), connectionUrl.toString());
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

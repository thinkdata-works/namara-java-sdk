package namara.client;

import namara.client.exception.AuthorizationException;
import namara.client.exception.ConnectionException;
import namara.client.exception.NamaraException;
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

        /**
         * Endpoint for getting query meta
         */
        public static final String META_ENDPOINT = "v0/query/meta";
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

    class Meta {
        private Integer queryLimit;

        public Meta(JSONObject metaObject) {
            if(metaObject.has("query_limit_maximum")) {
                Object limit = metaObject.get("query_limit_maximum");
                if(limit instanceof Integer) {
                    queryLimit = (Integer) limit;
                } else { // String
                    queryLimit = Integer.valueOf((String) limit);
                }
            } else {
                // If unavailable, default on 250
                queryLimit = DEFAULT_QUERY_LIMIT;
            }
        }

        public Integer getQueryLimit() { return queryLimit; }
    }

    /**
     * Default query value
     */
    static final Integer DEFAULT_QUERY_LIMIT = 250;

    /**
     * Connection to namara that can be used to interface
     */
    private Connection connection;

    /**
     * Any meta information about the Namara
     */
    private Meta meta;

    /**
     * Making package private so this can be set in unit tests
     */
    private final String PROTOCOL = "https";

    /**
     * Builds a new client for interfacing with Namara.
     *
     * This must point to a Namara API instance, but can be used to
     * connect to any other host.
     *
     * The API Key can be an account API key, or one generated for an organization or project
     *
     * @param namaraHost The host name for Namara, such as "https://api.namara.io"
     * @param apiKey The API Key for the connecting user
     */
    public Client(String namaraHost, String apiKey) {
        this.connection = new Connection(namaraHost, apiKey);
    }

    /**
     * Tests the namaraHost and apiKey to ensure that they can connect with the instance.
     *
     * If connection is successful, returns true. Otherwise, errors will be raised containing
     * details.
     *
     * @throws AuthorizationException API Key is not found or not valid for a user at that host
     * @throws ConnectionException Unable to connect to Namara at all
     * @return True if the host exists and the API Key is authorized for the host. Otherwise, an exception is thrown
     */
    public boolean testConnection() throws AuthorizationException, ConnectionException {
        HttpUrl url = buildUrl(Endpoints.TESTING_ENDPOINT);
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
     * @param queryString query to issue to namara
     * @return The resulting collection of records
     * @throws AuthorizationException when unable to authorize client for namara
     * @throws ConnectionException when unable to connect to namara
     * @throws QueryException when unable to build or execute query on namara
     */
    JSONObject query(String queryString) throws AuthorizationException, ConnectionException, QueryException {
        String jsonString = new JSONObject().put("query", queryString).toString();
        HttpUrl url = buildUrl(Endpoints.QUERY_ENDPOINT);
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
                    throw new ConnectionException("Something went wrong connecting to Namara. [" +
                            response.responseCode + "] " + response.responseBody, connectionUrl.toString(), jsonString);
            }
        } catch(IOException e) {
            throw new ConnectionException("Encountered error when connecting: " + e.getMessage(), connectionUrl.toString());
        }
    }

    /**
     * Retrieves the query limit for Namara
     * @return The maximum number of records that can be queried
     */
    Integer getQueryLimit() {
        /*
         * Build meta object from client
         */
        if(meta == null) {
            // If we aren't able to resolve this endpoint, just use a default
            try {
                setMeta();
            } catch(NamaraException e) {
                return DEFAULT_QUERY_LIMIT;
            }
        }

        return meta.getQueryLimit();
    }


    /**
     * Gets the processed host string for the client that will be used on connecting
     *
     * @return The host name
     */
    public String getNamaraHost() {
        return connection.getNamaraHost();
    }

    /**
     * Builds a url for the given endpoint
     *
     * @param endpoint Endpoint enum value for connecting
     * @return The constructed URL object
     */
    HttpUrl buildUrl(String endpoint) {
        return new HttpUrl.Builder()
                .scheme(PROTOCOL)
                .host(connection.getNamaraHost())
                .addPathSegments(endpoint)
                .build();
    }

    /**
     * Retrieves the meta information for the query endpoint and constructs the meta object
     *
     * @throws ConnectionException when unable to connect to Namara
     * @throws AuthorizationException when apiKey is not authorized
     */
    void setMeta() throws ConnectionException, AuthorizationException {
        HttpUrl url = buildUrl(Endpoints.META_ENDPOINT);

        try {
            NamaraResponse response = connection.getJSON(url);
            switch(Integer.valueOf(response.responseCode)) {
                case 200:
                    this.meta = new Meta(new JSONObject(response.responseBody));
                case 401:
                case 403:
                    throw new AuthorizationException("Unauthorized: " + response.responseBody);
                    // Endpoint does not return 422
                default:
                    throw new ConnectionException("Something went wrong connecting to Namara. [" +
                            response.responseCode + "] " + response.responseBody, url.toString());
            }
        } catch (IOException e) {
            throw new ConnectionException("Encountered error when connecting " + e.getMessage(), url.toString());
        }
    }
}

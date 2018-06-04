package namara.client;

import namara.client.exception.AuthorizationException;
import namara.client.exception.ConnectionException;
import namara.query.Query;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Client {

    private class Endpoints {
        /**
         * Endpoint for check if a user exists
         */
        public static final String TESTING_ENDPOINT = "/v0/static/authenticated";
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

        private HttpResponse getJSON(String connectionString) throws ConnectionException, IOException {
            return Request.Get(connectionString)
                    .addHeader("X-API-Key", apiKey)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .execute().returnResponse();
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
            HttpResponse response = connection.getJSON(connectionString);
            int statusCode = response.getStatusLine().getStatusCode();

            switch(Integer.valueOf(statusCode / 100)) {
                case 2: // status 2xx
                    return true;
                case 4:
                    throw new AuthorizationException("Unable to authorize user account with API Key at given hostName. " +
                            "[" + statusCode + "] " + EntityUtils.toString(response.getEntity(), "UTF-8"));
                default:
                    throw new ConnectionException("Something went wrong connecting to Namara. " +
                            "[" + statusCode + "] " + EntityUtils.toString(response.getEntity(), "UTF-8"), connectionString);
            }

        } catch(IOException e) {
            // Something went wrong with connecting, no response found
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

package namara.client;

import namara.client.exception.AuthorizationException;
import namara.client.exception.ConnectionException;
import namara.client.exception.NamaraException;
import namara.client.exception.QueryException;
import namara.query.QueryBuilder;
import okhttp3.HttpUrl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class ResultSet implements Iterator<Record> {
    /**
     * The constructed query. This will be used for result pagination
     */
    private QueryBuilder queryBuilder;

    /**
     * The namara client to send the query to
     */
    private Client client;

    /**
     * Current instance of the result iterator
     */
    private Iterator<Object> recordIterator;

    /**
     * Holds any exception that has been raised during querying.
     * Since the interface does not raise any errors, they need to be
     * retained and checked by the user
     */
    private NamaraException exception;

    /**
     * Number of records to fetch for each iteration. This is the maximum for Namara
     */
    private static final int FETCH_SIZE = 250;

    /**
     * Offset to start searching for records at
     */
    private static final int DEFAULT_OFFSET = 0;

    /**
     * Reference to the current limit for requesting results
     */
    private int currentLimit;

    /**
     * Reference to the current offset for requesting results
     */
    private int currentOffset;

    /**
     * Initialize a new result set with a constructed query and a Namara client.
     *
     * It is recommended that the client be tested before use.
     *
     * @see Client#testConnection()
     * @param queryBuilder the query builder
     * @param client the namara client
     */
    public ResultSet(QueryBuilder queryBuilder, Client client) {
        this.queryBuilder = queryBuilder;
        this.client = client;

        /*
         * We need to ensure that we are window-ing the results properly based on the limit given in the query (if any)
         * Seed the currentLimit to begin querying
         */
        if (queryBuilder.getLimit() == null || queryBuilder.getLimit() >= FETCH_SIZE) {
            this.currentLimit = FETCH_SIZE;
        } else {
            this.currentLimit = queryBuilder.getLimit();
        }

        if (queryBuilder.getOffset() == null) {
            this.currentOffset = DEFAULT_OFFSET;
        } else {
            this.currentOffset = queryBuilder.getOffset();
        }
    }

    /**
     * Checks whether getting next result has generated an error
     *
     * @return true if an error has been raised
     */
    public boolean hasException() {
        return exception != null;
    }

    /**
     * Throws the exception generated during while getting the next ResultSet.
     * If none is recorded, it will just return.
     *
     * @throws AuthorizationException Unable to authorize Namara user
     * @throws ConnectionException Unable to connect to Namara
     * @throws QueryException Unable to build query or execute query on Namara
     * @throws NamaraException Base error class. One of the above will likely be thrown, but this can catch all of them
     */
    public void throwException() throws AuthorizationException, ConnectionException, QueryException, NamaraException {
        if(exception == null) return;

        throw exception;
    }

    /**
     * Gets the next record
     *
     * @return the next Record in the iterator
     */
    public Record next() {
        return new Record((JSONObject) recordIterator.next());
    }

    /**
     * Returns true if there are more Records to be read.
     * Will return false if there are no more records to be read OR an exception has been thrown by the client
     * Please use `hasException()` and `throwException()` for access
     *
     * @see ResultSet#hasException()
     * @see ResultSet#throwException()
     * @return True if the iterator holds more Records
     */
    @Override
    public boolean hasNext() {
        try {
            // Check if list of records exists OR if the current iterator has run out of records
            if(recordIterator == null || !recordIterator.hasNext()) {
                // Attempt to getBuilder a new list of them
                recordIterator = retrieveNewIterator();
            }
            return recordIterator.hasNext();
        } catch (NamaraException e) {
            // Catch any exception and record them here
            this.exception = e;
            // This may be a false positive and should be checked by whoever is using the iterator on each pass
            return false;
        }
    }

    /**
     * Executes the query with the set limit and offset and creates an iterator out of the response records
     * Adjusts limit and offest for any subsequent queries.
     *
     * @return An iterator of response objects
     * @throws AuthorizationException
     * @throws QueryException
     * @throws ConnectionException
     */
    private Iterator<Object> retrieveNewIterator() throws AuthorizationException, QueryException, ConnectionException {
        // If our limit is below 0, return an empty iterator
        // since the query API will mark it as invalid SQL
        if(currentLimit <= 0) {
            return new ArrayList().iterator();
        }

        JSONObject responseObject = client.query(queryBuilder.buildQuery(currentLimit, currentOffset));
        JSONArray responseRecords = responseObject.getJSONArray("results");

        // Update limit and offset for next query
        currentOffset += currentLimit;

        // Check if we need to fetch less than the full window size
        if(queryBuilder.getLimit() != null && (queryBuilder.getLimit() - currentOffset) <= FETCH_SIZE) {
            // If the remaining rows fit in the fetching window
            currentLimit = queryBuilder.getLimit() - currentOffset;
        } else {
            // Otherwise, use default window size
            currentLimit = FETCH_SIZE;
        }

        return responseRecords.iterator();
    }
}

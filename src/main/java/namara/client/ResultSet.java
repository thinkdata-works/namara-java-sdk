package namara.client;

import namara.client.exception.AuthorizationException;
import namara.client.exception.ConnectionException;
import namara.client.exception.NamaraException;
import namara.client.exception.QueryException;
import namara.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class ResultSet implements Iterator<Record> {
    private QueryBuilder queryBuilder;
    private Client client;
    private Iterator<Object> recordIterator;

    private NamaraException exception;

    private static final int FETCH_SIZE = 250;
    private static final int DEFAULT_OFFSET = 0;

    private int currentLimit;
    private int currentOffset;

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
     * Throws
     * @throws Throwable
     */
    public void throwException() throws NamaraException {
        throw exception;
    }

    public Record next() {
        return new Record((JSONObject) recordIterator.next());
    }

    /**
     * Returns true if there are more Records to be read.
     * Will return false if there are no more records to be read OR an exception has been thrown by the client
     * Please use `hasException()` and `throwException()` for access
     *
     * @return
     */
    @Override
    public boolean hasNext() {
        try {
            // Check if list of records exists OR if the current iterator has run out of records
            if(recordIterator == null || !recordIterator.hasNext()) {
                // Attempt to build a new list of them
                recordIterator = retrieveNewIterator();
            }
            return recordIterator.hasNext();
        } catch (NamaraException e) {
            this.exception = e;
            return false;
        }
    }

    private Iterator<Object> retrieveNewIterator() throws AuthorizationException, QueryException, ConnectionException {
        JSONObject responseObject = client.query(queryBuilder.buildQuery(currentLimit, currentOffset));
        JSONArray responseRecords = responseObject.getJSONArray("results");

        // Update limit and offset for next query
        currentOffset += currentLimit;

        if(queryBuilder.getLimit() != null && (queryBuilder.getLimit() - currentOffset) <= FETCH_SIZE) {
            // If the remaining rows fit in the fetching window
            currentLimit = queryBuilder.getLimit() - currentOffset;
        } else {
            // Otherwise decrease the window size
            currentLimit = FETCH_SIZE;
        }

        return responseRecords.iterator();
    }
}

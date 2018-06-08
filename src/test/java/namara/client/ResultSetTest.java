package namara.client;

import namara.client.exception.NamaraException;
import namara.client.exception.QueryException;
import namara.query.Identifier;
import namara.query.QueryBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ResultSetTest {
    String DATA_SET_ID = "e276a9a8-d06f-49a0-bd69-482920006b53";
    String DATA_SET_VERSION = "en-0";

    Identifier identifier = new Identifier(DATA_SET_ID, DATA_SET_VERSION);
    Identifier falseIdentifier = new Identifier("data-set-uuid", "en-0");

    @Test
    public void testResultIterator() throws NamaraException {
        // Set up responses
        JSONArray resultsCollection = new JSONArray()
                .put(new JSONObject().put("c0", "xx").put("c1", "yy"))
                .put(new JSONObject().put("c0", "zz").put("c1", "tt"))
                .put(new JSONObject().put("c0", "hh").put("c1", "aa"));
        JSONObject responseObject = new JSONObject().put("results", resultsCollection);
        JSONObject emptyResponse = new JSONObject().put("results", new JSONArray());

        // Set up mocks
        Client client = mock(Client.class);
        when(client.query(anyString())).thenReturn(responseObject).thenReturn(emptyResponse);

        QueryBuilder builder = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier)
                .getBuilder();

        ResultSet resultSet = new ResultSet(builder, client);
        List<Record> recordList = new ArrayList();
        while(resultSet.hasNext()) {
            recordList.add(resultSet.next());
        }
        assertEquals(3, recordList.size());
    }

    @Test(expected = QueryException.class)
    public void testResultIteratorWithError() throws NamaraException {
        // Set up mocks
        Client client = mock(Client.class);
        when(client.query(anyString())).thenThrow(new QueryException("You did a bad query"));

        QueryBuilder builder = new QueryBuilder(5)
                .select().all()
                .from().dataSet(falseIdentifier)
                .getBuilder();

        ResultSet resultSet = new ResultSet(builder, client);
        List<Record> recordList = new ArrayList();

        while(resultSet.hasNext()) {
            recordList.add(resultSet.next());
        }

        assertEquals(0, recordList.size());
        assertTrue(resultSet.hasException());
        resultSet.throwException();
    }

    @Test
    public void testResultIteratorNoResults() throws NamaraException {
        // Set up responses
        JSONObject emptyResponse = new JSONObject().put("results", new JSONArray());

        // Set up mocks
        Client client = mock(Client.class);
        when(client.query(anyString())).thenReturn(emptyResponse);

        QueryBuilder builder = new QueryBuilder(0)
                .select().all()
                .from().dataSet(identifier)
                .getBuilder();

        ResultSet resultSet = new ResultSet(builder, client);

        List<Record> recordList = new ArrayList();

        while(resultSet.hasNext()) {
            recordList.add(resultSet.next());
        }

        assertEquals(0, recordList.size());
        assertFalse(resultSet.hasException());
    }
}
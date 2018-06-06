package namara.client;

import namara.client.exception.NamaraException;
import namara.client.exception.QueryException;
import namara.query.Identifier;
import namara.query.QueryBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ResultSetTest {
    /*
     * NOTE: update this with necessary API keys and URLs
     */
    String API_KEY = "67902e8fe69efa4d82348de8bf5df077552cc66221f15c7593fb381113f69147";
    String NAMARA_HOST = "https://namara-api-chris.ngrok.io/";
    String DATA_SET_ID = "e276a9a8-d06f-49a0-bd69-482920006b53";
    String DATA_SET_VERSION = "en-0";

    Identifier identifier = new Identifier(DATA_SET_ID, DATA_SET_VERSION);
    Identifier falseIdentifier = new Identifier("data-set-uuid", "en-0");
    Client client = new Client(NAMARA_HOST, API_KEY);

    @Test
    public void testResultIterator() {
        QueryBuilder builder = new QueryBuilder(5).select().all().from().dataSet(identifier).getBuilder();
        ResultSet resultSet = new ResultSet(builder, client);
        List<Record> recordList = new ArrayList();
        while(resultSet.hasNext()) {
            recordList.add(resultSet.next());
        }

        assertEquals(5, recordList.size());
    }

    @Test(expected = QueryException.class)
    public void testResultIteratorWithError() throws NamaraException {
        QueryBuilder builder = new QueryBuilder(5).select().all().from().dataSet(falseIdentifier).getBuilder();
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
    public void testResultIteratorNoResults() {
        QueryBuilder builder = new QueryBuilder(0).select().all().from().dataSet(identifier).getBuilder();
        ResultSet resultSet = new ResultSet(builder, client);
        List<Record> recordList = new ArrayList();

        while(resultSet.hasNext()) {
            recordList.add(resultSet.next());
        }

        assertEquals(0, recordList.size());
        assertFalse(resultSet.hasException());
    }
}
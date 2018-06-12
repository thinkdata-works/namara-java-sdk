package namara.client;

import namara.client.exception.ColumnNotFoundException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class RecordTest {
    JSONObject json = new JSONObject().put("foo", "test").put("bar", "string");
    Record entity = new Record(json);

    @Test
    public void testToString() {
        assertEquals("{\"bar\":\"string\",\"foo\":\"test\"}", entity.toString());
    }

    @Test
    public void testIterator() {
        Iterator<Value> valueIterator = entity.iterator();

        assertTrue(valueIterator.hasNext());
        Value value = valueIterator.next();
        assertEquals(value.getKey(), "bar");
        assertEquals(value.asString(), "string");

        assertTrue(valueIterator.hasNext());
        value = valueIterator.next();
        assertEquals(value.getKey(), "foo");
        assertEquals(value.asString(), "test");
    }

    @Test
    public void testColumnNames() {
        Set<String> columns = entity.columnsNames();
        Set<String> testSet = new HashSet();
        testSet.add("bar");
        testSet.add("foo");

        assertEquals(testSet, columns);
    }

    @Test
    public void testLength() { assertEquals(2, entity.length()); }

    @Test
    public void testGetValue() throws ColumnNotFoundException {
        assertEquals("string", entity.getValue("bar").asString());
    }

    @Test(expected = ColumnNotFoundException.class)
    public void testGetValueNotFound() throws ColumnNotFoundException {
        entity.getValue("Doesnt' exist");
    }
}
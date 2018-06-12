package namara.client;

import namara.client.exception.ValueConversionException;
import org.json.JSONObject;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class ValueTest {
    @Test
    public void testGet() {
      JSONObject object = new JSONObject().put("val", "Test string");
      Value value = new Value("val", object);
      assertEquals("Test string", value.get());
    }

    @Test
    public void testIsNull() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        assertTrue(value.isNull());
    }

    /*
     * == Testing asBigDecimal ==
     */
    @Test
    public void testIntToBigDecimal() {
        JSONObject object = new JSONObject().put("val", 12);
        Value value = new Value("val", object);
        assertEquals(new BigDecimal(12), value.asBigDecimal());
    }

    @Test
    public void testStringToBigDecimal() {
        JSONObject object = new JSONObject().put("val", "12");
        Value value = new Value("val", object);
        assertEquals(new BigDecimal(12), value.asBigDecimal());
    }

    @Test
    public void testDecimalToBigDecimal() {
        JSONObject object = new JSONObject().put("val", 12.4);
        Value value = new Value("val", object);
        assertEquals(new BigDecimal("12.4"), value.asBigDecimal());
    }

    @Test(expected = ValueConversionException.class)
    public void testEmptyStringToBigDecimal() {
        JSONObject object = new JSONObject().put("val", "");
        Value value = new Value("val", object);
        value.asBigDecimal();
    }

    @Test(expected = ValueConversionException.class)
    public void testNullToBigDecimal() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        value.asBigDecimal();
    }

    /*
     * == Testing tryBigDecimal ==
     */
    @Test
    public void testTryEmptyStringToBigDecimal() {
        JSONObject object = new JSONObject().put("val", "");
        Value value = new Value("val", object);
        BigDecimal defaultValue = new BigDecimal(0);
        assertEquals(defaultValue, value.tryBigDecimal(defaultValue));
    }

    @Test
    public void testTryNullToBigDecimal() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        BigDecimal defaultValue = new BigDecimal(0);
        assertEquals(defaultValue, value.tryBigDecimal(defaultValue));
    }

    /*
     * == Testing asBigInteger ==
     */
    @Test
    public void testIntToBigInteger() {
        JSONObject object = new JSONObject().put("val", 12);
        Value value = new Value("val", object);
        assertEquals(new BigInteger("12"), value.asBigInteger());
    }

    @Test
    public void testStringToBigInteger() {
        JSONObject object = new JSONObject().put("val", "12");
        Value value = new Value("val", object);
        assertEquals(new BigInteger("12"), value.asBigInteger());
    }

    @Test(expected = ValueConversionException.class)
    public void testDecimalToBigInteger() {
        JSONObject object = new JSONObject().put("val", 12.4);
        Value value = new Value("val", object);
        value.asBigInteger();
    }

    @Test(expected = ValueConversionException.class)
    public void testEmptyStringToBigInteger() {
        JSONObject object = new JSONObject().put("val", "");
        Value value = new Value("val", object);
        value.asBigInteger();
    }

    @Test(expected = ValueConversionException.class)
    public void testNullToBigInteger() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        value.asBigInteger();
    }

    /*
     * == Testing tryBigInteger ==
     */

    @Test
    public void testTryDecimalToBigInteger() {
        JSONObject object = new JSONObject().put("val", 12.4);
        Value value = new Value("val", object);
        BigInteger defaultValue = new BigInteger("12");
        assertEquals(new BigInteger("12"), value.tryBigInteger(defaultValue));
    }


    @Test
    public void testTryEmptyStringToBigInteger() {
        JSONObject object = new JSONObject().put("val", "");
        Value value = new Value("val", object);
        BigInteger defaultValue = new BigInteger("12");
        assertEquals(new BigInteger("12"), value.tryBigInteger(defaultValue));
    }

    @Test
    public void testTryNullToBigInteger() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        BigInteger defaultValue = new BigInteger("12");
        assertEquals(new BigInteger("12"), value.tryBigInteger(defaultValue));
    }

    /*
     * == Testing asBoolean ==
     */
    @Test
    public void testTrueAsBoolean() {
        JSONObject object = new JSONObject().put("val", true);
        Value value = new Value("val", object);
        assertTrue(value.asBoolean());
    }

    @Test
    public void testFalseAsBoolean() {
        JSONObject object = new JSONObject().put("val", false);
        Value value = new Value("val", object);
        assertFalse(value.asBoolean());
    }

    @Test
    public void testTrueStringAsBoolean() {
        JSONObject object = new JSONObject().put("val", "true");
        Value value = new Value("val", object);
        assertTrue(value.asBoolean());
    }

    @Test
    public void testFalseStringAsBoolean() {
        JSONObject object = new JSONObject().put("val", "false");
        Value value = new Value("val", object);
        assertFalse(value.asBoolean());
    }

    @Test(expected = ValueConversionException.class)
    public void testIntAsBoolean() {
        JSONObject object = new JSONObject().put("val", 1);
        Value value = new Value("val", object);
        value.asBoolean();
    }

    @Test(expected = ValueConversionException.class)
    public void testEmptyStringAsBoolean() {
        JSONObject object = new JSONObject().put("val", "");
        Value value = new Value("val", object);
        value.asBoolean();
    }

    @Test(expected = ValueConversionException.class)
    public void testNullAsBoolean() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        value.asBoolean();
    }

    /*
     * == Testing tryBoolean ==
     */
    @Test
    public void testTryNullAsBoolean() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        assertTrue(value.tryBoolean(Boolean.TRUE));
    }

    /*
     * == Testing asDate ==
     */

    @Test
    public void testDateFromInt() {
        JSONObject object = new JSONObject().put("val", 20150205);
        Value value = new Value("val", object);
        Date date = value.asDate(new SimpleDateFormat("yyyyMMdd"));
        assertEquals("2015-02-05", new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    @Test
    public void testDateFromString() {
        JSONObject object = new JSONObject().put("val", "20150205");
        Value value = new Value("val", object);
        Date date = value.asDate(new SimpleDateFormat("yyyyMMdd"));
        assertEquals("2015-02-05", new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    @Test(expected = ValueConversionException.class)
    public void testDateFromNull() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        value.asDate(new SimpleDateFormat("yyyyMMdd"));
    }

    @Test(expected = ValueConversionException.class)
    public void testDateFromBadFormat() {
        JSONObject object = new JSONObject().put("val", 1002);
        Value value = new Value("val", object);
        value.asDate(new SimpleDateFormat("yyyyMMdd"));
    }

    /*
     * == Testing tryDate ==
     */

    @Test
    public void testTryDateFromNull() throws ParseException {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        Date defaultValue = new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-01");
        Date date = value.tryDate(new SimpleDateFormat("yyyyMMdd"), defaultValue);
        assertEquals("1970-01-01", new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    @Test
    public void testTryDateFromString() throws ParseException {
        JSONObject object = new JSONObject().put("val", "This Is a test");
        Value value = new Value("val", object);
        Date defaultValue = new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-01");
        Date date = value.tryDate(new SimpleDateFormat("yyyyMMdd"), defaultValue);
        assertEquals("1970-01-01", new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    /*
     * == Testing asDouble ==
     */
    @Test
    public void testDecimalAsDouble() {
        JSONObject object = new JSONObject().put("val", 12.4);
        Value value = new Value("val", object);
        assertEquals((Double) 12.4, value.asDouble());
    }

    @Test
    public void testIntAsDouble() {
        JSONObject object = new JSONObject().put("val", 12);
        Value value = new Value("val", object);
        assertEquals((Double) 12.0, value.asDouble());
    }

    @Test(expected = ValueConversionException.class)
    public void testStringAsDouble() {
        JSONObject object = new JSONObject().put("val", "test");
        Value value = new Value("val", object);
        value.asDouble();
    }

    @Test(expected = ValueConversionException.class)
    public void testNullAsDouble() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        value.asDouble();
    }

    /*
     * == Testing tryDouble ==
     */
    @Test
    public void testTryNullAsDouble() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        assertEquals(new Double(14.0), value.tryDouble(new Double(14.0)));
    }

    /*
     * == Testing toEnum ==
     */

    enum TestEnum { VALUE1, VALUE2 }

    @Test
    public void testAsEnum() {
        JSONObject object = new JSONObject().put("val", "VALUE1");
        Value value = new Value("val", object);
        assertEquals(TestEnum.VALUE1, value.asEnum(TestEnum.class));
    }

    @Test(expected = ValueConversionException.class)
    public void testIntAsEnum() {
        JSONObject object = new JSONObject().put("val", 1);
        Value value = new Value("val", object);
        value.asEnum(TestEnum.class);
    }

    @Test(expected = ValueConversionException.class)
    public void testStringAsEnum() {
        JSONObject object = new JSONObject().put("val", "VALUE3");
        Value value = new Value("val", object);
        value.asEnum(TestEnum.class);
    }

    /*
     * == Testing tryEnum ==
     */
    @Test
    public void testTryIntAsEnum() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        assertEquals(TestEnum.VALUE1, value.tryEnum(TestEnum.class, TestEnum.VALUE1));
    }

    /*
     * == Testing asInt ==
     */

    @Test
    public void testIntAsInt() {
        JSONObject object = new JSONObject().put("val", 1);
        Value value = new Value("val", object);
        assertEquals((Integer) 1, value.asInt());
    }

    @Test
    public void testStringAsInt() {
        JSONObject object = new JSONObject().put("val", "1");
        Value value = new Value("val", object);
        assertEquals((Integer) 1, value.asInt());
    }

    @Test
    public void testDecimalAsInt() {
        JSONObject object = new JSONObject().put("val", 1.5);
        Value value = new Value("val", object);
        assertEquals((Integer) 1, value.asInt());
    }

    @Test(expected = ValueConversionException.class)
    public void testNullAsInt() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        value.asInt();
    }

    /*
     * == Testing tryInt ==
     */

    @Test
    public void testTryNullInt() {
        JSONObject object = new JSONObject().put("val", JSONObject.NULL);
        Value value = new Value("val", object);
        assertEquals((Integer) 4, value.tryInt(4));
    }

    /*
     * Not going to test other fields, all the same...
     */

    /*
     * == Testing getKey ==
     */
    @Test
    public void testGetKey() {
        JSONObject object = new JSONObject().put("val", 14);
        Value value = new Value("val", object);
        assertEquals("val", value.getKey());
    }

    /*
     * == Testing toString ==
     */
    @Test
    public void testToString() {
        JSONObject object = new JSONObject().put("val", 14);
        Value value = new Value("val", object);
        assertEquals("val: 14", value.toString());
    }
}
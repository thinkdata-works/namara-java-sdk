package namara.client;

import namara.client.exception.ValueConversionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Value {
    private String key;
    private JSONObject valueHolder;

    /**
     * Creates a Value, based on the key on the JSONObject holding that value
     * Passing the whole JSONObject in because we can use all the methods on JSONObject
     * for getting and converting the value at the key
     *
     * @param key         - the key that referenced the value
     * @param valueHolder - the JSONObject containing the value
     */
    Value(String key, JSONObject valueHolder) {
        this.key = key;
        this.valueHolder = valueHolder;
    }

    /**
     * @return - the raw value
     */
    public Object get() {
        try {
            return valueHolder.get(key);
        } catch (JSONException e) {
            // This should never happen, we only instantiate the Value because it exists at a key
            return (Object) throwIt("Object");
        }
    }

    /**
     * @return - true if the value is said to be null
     */
    public boolean isNull() {
        return valueHolder.isNull(key);
    }


    /*
     * ======================= Conversion Functions =====================================
     */

    /**
     * @return - the value as BigDecimal
     * @throws ValueConversionException if unable to convert
     */
    public BigDecimal asBigDecimal() throws ValueConversionException {
        try {
            return valueHolder.getBigDecimal(key);
        } catch (JSONException e) {
            return (BigDecimal) throwIt("BigDecimal");
        }
    }

    /**
     * @param defaultValue - default
     * @return - value as BigDecimal or defaultValue if unable to convert
     */
    public BigDecimal tryBigDecimal(BigDecimal defaultValue) {
        try {
            return valueHolder.optBigDecimal(key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * @return - the value as BigInteger
     * @throws ValueConversionException if unable to convert
     */
    public BigInteger asBigInteger() throws ValueConversionException {
        try {
            return valueHolder.getBigInteger(key);
        } catch (JSONException e) {
            return (BigInteger) throwIt("BigInteger");
        }
    }

    /**
     * @param defaultValue - default BigInteger
     * @return - value as BigInteger or defaultValue if unable to convert
     */
    public BigInteger tryBigInteger(BigInteger defaultValue) {
        try {
            return valueHolder.optBigInteger(key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * @return - the value as Boolean
     * @throws ValueConversionException if unable to convert
     */
    public Boolean asBoolean() throws ValueConversionException {
        try {
            return valueHolder.getBoolean(key);
        } catch (JSONException e) {
            return (Boolean) throwIt("boolean");
        }
    }

    /**
     * @param defaultValue - default Boolean
     * @return - the value as Boolean or defaultValue if unable to convert
     */
    public Boolean tryBoolean(Boolean defaultValue) {
        try {
            return valueHolder.optBoolean(key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * @param format - format for parsing the stored value into a Date
     * @return - the value as a formatted Date
     * @throws ValueConversionException if unable to convert
     */
    public Date asDate(SimpleDateFormat format) throws ValueConversionException {
        try {
            return format.parse(get().toString());
        } catch(ParseException e) {
            return (Date) throwIt("Date");
        }
    }

    /**
     * @param format - format for parsing the stored value into a Date
     * @param defaultValue - default Date
     * @return - the value as a formatted date or the defaultValue if unable to parse
     */
    public Date tryDate(SimpleDateFormat format, Date defaultValue) {
        try {
            if(isNull()) {
                return defaultValue;
            } else {
                return format.parse(get().toString());
            }
        } catch(ParseException e) {
            return defaultValue;
        }
    }

    /**
     * @return - the value as Double
     * @throws ValueConversionException if unable to convert
     */
    public Double asDouble() throws ValueConversionException {
        try {
            return valueHolder.getDouble(key);
        } catch (JSONException e) {
            return (Double) throwIt("double");
        }
    }

    /**
     * @param defaultValue - default double
     * @return - the value as Double or defaultValue if unable to convert
     */
    public Double tryDouble(Double defaultValue) {
        try {
            return valueHolder.optDouble(key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * @param clazz - type fo Enum to retrieve
     * @param <E> - enum type
     * @return - enum value associated with key
     * @throws ValueConversionException if unable to convert
     */
    public <E extends Enum<E>> E asEnum(Class<E> clazz) throws ValueConversionException {
        try {
            return valueHolder.getEnum(clazz, key);
        } catch (JSONException e) {
            return (E) throwIt("enum type " + clazz.getSimpleName());
        }
    }

    /**
     * @param clazz - class for converting
     * @param defaultValue - default Enum to return if converting fails
     * @param <E> - enum type
     * @return - enum value associated with key, or defaultValue if unable to convert
     */
    public <E extends Enum<E>> E tryEnum(Class<E> clazz, E defaultValue) {
        try {
            return valueHolder.optEnum(clazz, key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * @return - value as Integer
     * @throws ValueConversionException if unable to convert
     */
    public Integer asInt() throws ValueConversionException {
        try {
            return valueHolder.getInt(key);
        } catch (JSONException e) {
            return (Integer) throwIt("int");
        }
    }

    /**
     * @param defaultValue - default Integer
     * @return - the Integer value associated with key, or defaultValue if unable to convert
     */
    public Integer tryInt(Integer defaultValue) {
        try {
            return valueHolder.optInt(key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * @return - the value as JSONArray
     * @throws ValueConversionException if unable to convert
     */
    public JSONArray asJSONArray() throws ValueConversionException {
        try {
            return valueHolder.getJSONArray(key);
        } catch (JSONException e) {
            return (JSONArray) throwIt("JSONArray");
        }
    }

    /**
     * @param defaultValue - default JSONArray
     * @return - the value as JSONArray or defaultValue if unable to convert
     */
    public JSONArray tryJSONArray(JSONArray defaultValue) {
        try {
            return valueHolder.optJSONArray(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * @return - the value as JSONObject
     * @throws ValueConversionException if unable to convert
     */
    public JSONObject asJSONObject() throws ValueConversionException {
        try {
            return valueHolder.getJSONObject(key);
        } catch (JSONException e) {
            return (JSONObject) throwIt("JSONObject");
        }
    }

    /**
     * @param defaultValue - default JSONObject
     * @return - the value converted to JSONObject or defaultValue if unable to convert
     */
    public JSONObject tryJSONObject(JSONObject defaultValue) {
        try {
            return valueHolder.optJSONObject(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * @return - the value as Long
     * @throws ValueConversionException if unable to convert
     */
    public Long asLong() throws ValueConversionException {
        try {
            return valueHolder.getLong(key);
        } catch (JSONException e) {
            return (Long) throwIt("Long");
        }
    }

    /**
     * @param defaultValue - default Long
     * @return - the value as Long or defaultValue if unable to convert
     */
    public Long tryLong(Long defaultValue) {
        try {
            return valueHolder.optLong(key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * @return - the value as String
     * @throws ValueConversionException if unable to convert
     */
    public String asString() throws ValueConversionException {
        try {
            return valueHolder.getString(key);
        } catch (JSONException e) {
            return (String) throwIt("String");
        }
    }

    /**
     * @param defaultValue - default String
     * @return - the value as String or defaultValue if unable to convert
     */
    public String tryString(String defaultValue) {
        try {
            return valueHolder.optString(key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * @return the key that this object was referenced by
     */
    public String getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return key + ": " + valueHolder.get(key);
    }

    private Object throwIt(String type) throws ValueConversionException {
        throw new ValueConversionException("Record[" + valueHolder.quote(key) +
                "] => [" + valueHolder.get(key) + "] can not be converted to " + valueHolder.quote(type),
                valueHolder.get(key));
    }
}

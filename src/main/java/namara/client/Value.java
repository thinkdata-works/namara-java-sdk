package namara.client;

import namara.client.exception.ValueConversionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Value {
    private String key;
    private JSONObject valueHolder;

    /**
     * Creates a Value, based on the key on the JSONObject holding that value
     * Passing the whole JSONObject in because we can use all the methods on JSONObject
     * for getting and converting the value at the key
     *
     * @param key - the key that referenced the value
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
        return valueHolder.get(key);
    }

    /**
     * @return - true if the value is said to be null
     */
     public boolean isNull() {
         return valueHolder.isNull(key);
     }

     // TODO - all of the OPT calls
    
    /**
     * @return - the value as BigDecimal
     * @throws ValueConversionException
     */
    public BigDecimal asBigDecimal() throws ValueConversionException{
        try {
            return valueHolder.getBigDecimal(key);
        } catch(JSONException e) {
            return (BigDecimal) throwIt("BigDecimal");
        }
    }

    /**
     * @return - the value as BigInteger
     * @throws ValueConversionException
     */
    public BigInteger asBigInteger() throws ValueConversionException {
        try {
            return valueHolder.getBigInteger(key);
        } catch(JSONException e) {
            return (BigInteger) throwIt("BigInteger");
        }
    }

    /**
     * @return - the value as Boolean
     * @throws ValueConversionException
     */
    public Boolean asBoolean() throws ValueConversionException {
        try {
            return valueHolder.getBoolean(key);
        } catch(JSONException e) {
            return (Boolean) throwIt("boolean");
        }
    }

    /**
     * @return - the value as Double
     */
    public Double asDouble() {
        try {
            return valueHolder.getDouble(key);
        } catch(JSONException e) {
            return (Double) throwIt("double");
        }
    }

    /**
     *
     * @param clazz - type fo Enum to retrieve
     * @return - enum value associated with key
     */
    public <E extends Enum<E>> E asEnum(Class<E> clazz) {
        try {
            return valueHolder.getEnum(clazz, key);
        } catch(JSONException e) {
            return (E) throwIt("enum type " + clazz.getSimpleName());
        }
    }

    /**
     * @return - value as Integer
     */
    public Integer asInt() {
        try {
            return valueHolder.getInt(key);
        } catch(JSONException e) {
            return (Integer) throwIt("int");
        }
    }

    /**
     * @return - the value as JSONArray
     */
    public JSONArray asJSONArray() {
        try {
            return valueHolder.getJSONArray(key);
        } catch(JSONException e) {
            return (JSONArray) throwIt("JSONArray");
        }
    }

    /**
     * @return - the value as JSONObject
     */
    public JSONObject asJSONObject() {
        try {
            return valueHolder.getJSONObject(key);
        } catch(JSONException e) {
            return (JSONObject) throwIt("JSONObject");
        }
    }

    /**
     * @return - the value as Long
     */
    public Long asLong() {
        try {
            return valueHolder.getLong(key);
        } catch(JSONException e) {
            return (Long) throwIt("Long");
        }
    }

    /**
     * @return - the value as String
     */
    public String asString() {
        try {
            return valueHolder.getString(key);
        } catch(JSONException e) {
            return (String) throwIt("String");
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
                "] can not be converted to " + valueHolder.quote(type),
                valueHolder.get(key));
    }
}

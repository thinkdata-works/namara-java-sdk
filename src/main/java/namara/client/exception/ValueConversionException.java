package namara.client.exception;

public class ValueConversionException extends RuntimeException {
    private Object value;
    private Class clazz;

    /**
     * Error when converting value type
     * @param message - error message
     * @param value - value where converting errored
     */
    public ValueConversionException(String message, Object value) {
        super(message);
        this.value = value;
        this.clazz = value.getClass();
    }

    /**
     * @return - the assigned class for the object before converting
     */
    public Class getAssumedClass() {
        return clazz;
    }
}

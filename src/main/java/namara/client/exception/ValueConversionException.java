package namara.client.exception;

public class ValueConversionException extends RuntimeException {
    private Object value;
    private Class clazz;

    public ValueConversionException(String message, Object value) {
        super(message);
        this.value = value;
        this.clazz = value.getClass();
    }
}

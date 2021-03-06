package namara.query;

public class Any extends Expression {
    Any(Context context) {
        super(context);
        this.context.append("ANY");
    }

    /**
     * Creates an "ANY" query node
     *
     * @param expression the evaluating expression
     * @return the resulting expression
     */
    public Any any(Expression expression) {
        context.append("(" + expression.toString().trim() + ")");
        return this;
    }

    /**
     * Adds a SELECT expression to the ANY query node
     *
     * @return the resulting expression
     */
    Select select() { return new Select(context); }
}

package namara.query;

public class All extends Expression {

    /**
     * Creates an "ALL" query node
     * @param context
     */
    All(Context context) {
        super(context);
        this.context.append("ALL");
    }

    /**
     * Creates an "ALL" query node
     *
     * @param expression the expression argument
     * @return the resulting expression
     */
    public All all(Expression expression) {
        context.append("(" + expression.toString().trim() + ")");
        return this;
    }

    /**
     * Adds a SELECT expression to the ALL query node
     * @return the resulting expression
     */
    public Select select() { return new Select(context); }
}

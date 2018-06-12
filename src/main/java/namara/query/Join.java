package namara.query;

abstract class Join extends Expression {

    Join(Context context) {
        super(context);
        context.append(expression());
    }

    Join(Context context, Identifier identifier) {
        this(context);
        context.append(identifier.getTableName());
    }

    /**
     * Specifies the condition for joining
     *
     * @param condition - condition for joining on
     * @return the resulting expression
     */
    public Join on(String condition) {
        context.append("ON");
        context.append(condition);
        return this;
    }

    /**
     * Creates an ORDER BY node for the JOIN clause
     *
     * @return the resulting expression
     */
    public OrderBy orderBy() {
        return new OrderBy(context);
    }

    /**
     * Creates an empty WHERE node for the JOIN clause
     *
     * @return the resulting expression
     */
    public Where where() {
        return new Where(context);
    }

    /**
     * Creates a WHERE node with a condition for the JOIN clause
     *
     * @param condition - condition for filtering joined table
     * @return the resulting expression
     */
    public Where where(String condition) {
        return new Where(context, condition);
    }

    protected abstract String expression();

    @Override
    public String toString() {
        return context.toString();
    }
}

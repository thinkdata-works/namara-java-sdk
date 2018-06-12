package namara.query;

abstract public class Expression {
    protected final Context context;

    Expression(Context context) {
        this.context = context;
    }

    /**
     * Gets the queryBuilder for the constructed expression. This will be needed for passing to the ResultSet
     *
     * @see namara.client.ResultSet
     * @return the resulting QueryBuilder
     */
    public QueryBuilder getBuilder() {
        return context.getQueryBuilder();
    }
}
